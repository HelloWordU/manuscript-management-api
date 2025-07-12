package com.rz.manuscript.controller;

import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.Manuscript;
import com.rz.manuscript.entity.Material;
import com.rz.manuscript.entity.Picture;
import com.rz.manuscript.entity.Project;
import com.rz.manuscript.pojo.request.MaterialGetListRequest;
import com.rz.manuscript.pojo.vo.MaterialVo;
import com.rz.manuscript.service.IMaterialService;
import com.rz.manuscript.service.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 资料库 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/material")
@Api(tags = "资料")
@Slf4j
public class MaterialController {
    @Resource
    private IMaterialService iMaterialService;
    @Resource
    private IProjectService iProjectService;

    @PostMapping("/getList")
    @ApiOperation("获取资料列表")
    public ResultEntityList<MaterialVo> getList(@RequestBody MaterialGetListRequest materialGetListRequest) {
        LambdaQueryWrapper<Material> wrapper = new LambdaQueryWrapper<>();
        if (materialGetListRequest.getProjectId() != null) {
            wrapper.eq(Material::getProjectId, materialGetListRequest.getProjectId());
        }
        if (!StringUtils.isEmpty(materialGetListRequest.getFileName())) {
            wrapper.like(Material::getOriginName, materialGetListRequest.getFileName());
        }
        if (materialGetListRequest.getPageSize() == null)
            materialGetListRequest.setPageSize(20);
        Page<Material> s = new Page<>(materialGetListRequest.getPageIndex(), materialGetListRequest.getPageSize());
        Page<Material> page1 = iMaterialService.page(s, wrapper);
        List<MaterialVo> materialVoList = new ArrayList<>();
        if (!page1.getRecords().isEmpty()) {
            List<Integer> projectIds = page1.getRecords().stream().map(i -> i.getProjectId()).distinct().collect(Collectors.toList());
            LambdaQueryWrapper<Project> projectLambdaQueryWrapper = new LambdaQueryWrapper<>();
            projectLambdaQueryWrapper.in(Project::getId, projectIds);
            List<Project> projectList = iProjectService.list(projectLambdaQueryWrapper);
            for (Material item : page1.getRecords()) {
                MaterialVo newItem = new MaterialVo();
                BeanUtils.copyProperties(item, newItem);
                Project currentP = projectList.stream().filter(m -> m.getId().equals(newItem.getProjectId())).findFirst().orElse(null);
                if (currentP != null) {
                    newItem.setProjectName(currentP.getName());
                }
                materialVoList.add(newItem);
            }

        }

        ResultEntityList<MaterialVo> res = new ResultEntityList<>(200, materialVoList, "获取成功");
        res.setPageIndex(materialGetListRequest.getPageIndex());
        res.setPageSize(materialGetListRequest.getPageSize());
        res.setTotal(page1.getTotal());
        return res;
    }

    @PostMapping("/delete")
    @ApiOperation("删除资料")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            iMaterialService.removeById(id);
        } catch (Exception e) {
            log.error("删除资料", e);
        }
        return res;
    }

    @PostMapping("/save")
    @ApiOperation("保存资料")
    public ResultEntity<Boolean> save(@RequestBody Material entity) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            iMaterialService.saveOrUpdate(entity);
        } catch (Exception e) {
            log.error("删除资料", e);
        }
        return res;
    }

    @PostMapping("/upload")
    @ApiOperation("附件上传")
    public ResultEntity<Boolean> upload(MultipartHttpServletRequest request) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            Integer projectId = Integer.parseInt(request.getParameter("projectId"));
            Map<String, MultipartFile> fileMap = request.getFileMap();
            if (fileMap == null || fileMap.isEmpty()) {
                return new ResultEntity<>(0, true, "无效的文件");
            }
            String uploadInfo = "";
            for (String key : fileMap.keySet()) {
                MultipartFile uploadFile = fileMap.get(key);
                if (uploadFile.isEmpty())
                    uploadInfo += key + "无效的文件";
                //return new ResultEntity<>(0, true, "无效的文件");
                String fileName = uploadFile.getOriginalFilename();
                String fileSuffix = FileNameUtil.extName(fileName);
                try {
                    //保存到数据库和文件
                    String filePath = "D:\\test" + "\\" + projectId;
                    if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                        filePath = "/home/file/" + projectId;
                    }
                    File file = new File(filePath);
                    if (!file.exists()) {

                        file.mkdir();

                    }//重新设置文件名为 UUID，以确保唯一
                    String saveName = UUID.randomUUID() + "." + fileSuffix;

                    file = new File(filePath, saveName);
                    uploadFile.transferTo(file);
                    // String title = fileName.replace("." + fileSuffix, "");
                    Material entity = new Material();
                    entity.setCreateTime(new Date());
                    entity.setOriginName(fileName);
                    entity.setProjectId(projectId);
                    entity.setFileUrl(saveName);
                    iMaterialService.save(entity);
                } catch (Exception e) {
                    uploadInfo += key + uploadFile.getOriginalFilename() + "读取文件失败" + e.getMessage();
                    log.error("读取文件失败", e);
                    return new ResultEntity<>(500, false, uploadInfo);
                }
            }
            return new ResultEntity<>(200, true, "上传成功!" + uploadInfo);
            // iMaterialService.saveOrUpdate(entity);
        } catch (Exception e) {
            log.error("删除资料", e);
        }
        return res;
    }


}
