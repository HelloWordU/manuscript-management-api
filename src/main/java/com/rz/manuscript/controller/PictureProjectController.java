package com.rz.manuscript.controller;

import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rz.manuscript.common.*;
import com.rz.manuscript.entity.*;
import com.rz.manuscript.exception.RZException;
import com.rz.manuscript.pojo.request.QueryPictureRequest;
import com.rz.manuscript.service.IPictureProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * <p>
 * 项目图片 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-05-25
 */
@RestController
@RequestMapping("/pictureProject")
@Slf4j
@Api(tags = "项目图片")
public class PictureProjectController {
    @Resource
    private HttpServletRequest request;

    @Resource
    private IPictureProjectService iPictureProjectService;

    @GetMapping("/getProjectPicture")
    @ApiOperation(value = "获取项目图片")
    public ResultEntityList<PictureProject> getProjectPicture(@RequestParam @ApiParam(value = "类型", required = true) Integer projectId, @RequestParam Integer page) {
        //获取当前用户项目的图片
        User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
        if (currentLoginUser == null)
            return new ResultEntityList<>(500, null, "无效的登录用户");
        LambdaQueryWrapper<PictureProject> wrapper = new LambdaQueryWrapper<>();
        if (projectId != null) {
            wrapper.eq(PictureProject::getProjectId, projectId);
        }
        Page<PictureProject> s = new Page<>(page, 10L);
        Page<PictureProject> page1 = iPictureProjectService.page(s, wrapper);
        ResultEntityList<PictureProject> res = new ResultEntityList<>(200, page1.getRecords(), "获取成功");
        res.setPageIndex(page);
        res.setPageSize(10);
        res.setTotal(page1.getTotal());
        return res;
    }

    @PostMapping("/query")
    @ApiOperation(value = "获取项目图片")
    public ResultEntityList<PictureProject> query(@RequestBody QueryPictureRequest queryPictureRequest) {
        //获取当前用户项目的图片
        User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
        if (currentLoginUser == null)
            return new ResultEntityList<>(500, null, "无效的登录用户");
        LambdaQueryWrapper<PictureProject> wrapper = new LambdaQueryWrapper<>();
        Long projectId = queryPictureRequest.getProjectId();
        if (projectId != null) {
            wrapper.eq(PictureProject::getProjectId, projectId);
        }
        Page<PictureProject> s = new Page<>(queryPictureRequest.getPageIndex(), queryPictureRequest.getPageSize());
        Page<PictureProject> page1 = iPictureProjectService.page(s, wrapper);
        ResultEntityList<PictureProject> res = new ResultEntityList<>(200, page1.getRecords(), "获取成功");
        res.setPageIndex(queryPictureRequest.getPageIndex());
        res.setPageSize(queryPictureRequest.getPageSize());
        res.setTotal(page1.getTotal());
        return res;
    }


    @PostMapping("/delete")
    @ApiOperation(value = "删除")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            AssertUtil.notNull(id, "无效的数据");
            iPictureProjectService.removeById(id);
        } catch (RZException e) {
            res.setCode(500);
            res.setMessage(e.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("保存网络图片异常", e);
        }
        return res;
    }


    @PostMapping("/upload")
    @ApiOperation("图片上传")
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
                try {
                    String imageDate = new String(Base64.getEncoder().encode(uploadFile.getBytes()));
                    imageDate = "data:" + Html2WordUtil.getImageToBase64MimeType(fileName) + ";base64," + imageDate;
                    PictureProject entity = new PictureProject();
                    entity.setCreateTime(new Date());
                    entity.setProjectId(projectId);
                    entity.setImageData(imageDate);
                    iPictureProjectService.save(entity);
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
