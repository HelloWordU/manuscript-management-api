package com.rz.manuscript.controller;

import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.MonitoringPlantformArticle;
import com.rz.manuscript.pojo.vo.GetByPlantformIdAndKeyRequest;
import com.rz.manuscript.pojo.vo.MonitoringPlantformArticleVo;
import com.rz.manuscript.service.IMonitoringPlantformArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 监控平台文章 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-06-18
 */
@RestController
@RequestMapping("/monitoringPlantformArticle")
@Slf4j
@Api("监控平台文章")
public class MonitoringPlantformArticleController {

    @Resource
    private IMonitoringPlantformArticleService iMonitoringPlantformArticleService;

    @GetMapping("/getByPlantformId")
    public ResultEntityList<MonitoringPlantformArticleVo> getByPlantformId(@RequestParam int plantformId)
    {
        List<MonitoringPlantformArticleVo> res = iMonitoringPlantformArticleService.getByPlantformId(plantformId);
        return new ResultEntityList(200, res, "获取成功");
    }

    @GetMapping("/get")
    public ResultEntityList<MonitoringPlantformArticleVo> getByCategoryId(@RequestParam int categoryId)
    {
        List<MonitoringPlantformArticleVo> res = iMonitoringPlantformArticleService.getByCategoryId(categoryId);
        return new ResultEntityList(200, res, "获取成功");
    }

    @PostMapping("/getByPlantformIdAndKey")
    public ResultEntityList<MonitoringPlantformArticleVo> getByPlantformIdAndKey(@RequestBody GetByPlantformIdAndKeyRequest request)
    {
        List<MonitoringPlantformArticleVo> res = iMonitoringPlantformArticleService.getByPlantformId(request.getOptionValue());
        for (MonitoringPlantformArticleVo item :res) {
            item.setTitle(item.getTitle().replace(item.getCategoryName(),"<span style=\"color:#5ED9F1;\">"+item.getCategoryName()+"</span>"));
        }
        return new ResultEntityList(200, res, "获取成功");
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除监控文章")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        iMonitoringPlantformArticleService.removeById(id);
        return new ResultEntity<>(200, true, "保存");
    }


    @PostMapping("/upload")
    public ResultEntity<Boolean> upload(@RequestParam Integer categoryId,@RequestParam Integer plantformId, @RequestParam MultipartFile uploadFile) {
        if (categoryId == null || categoryId < 1) {
            return new ResultEntity<>(0, true, "无效的关键词");
        }
        if (plantformId == null || plantformId < 1) {
            return new ResultEntity<>(0, true, "无效的监控平台");
        }
        if (uploadFile.isEmpty())
            return new ResultEntity<>(0, true, "无效的文件");
        if (!FileNameUtil.extName(uploadFile.getOriginalFilename()).equals("xlsx") && !FileNameUtil.extName(uploadFile.getOriginalFilename()).equals("xls")) {
            return new ResultEntity<>(0, true, "无效的文件格式，只支持xlsx、xls");
        }

        try {
            ExcelReader reader = ExcelUtil.getReader(uploadFile.getInputStream());
            List<MonitoringPlantformArticle> list = new ArrayList<>();
            List<Map<String, Object>> maps = reader.readAll();
            for (Map<String, Object> item : maps) {
                MonitoringPlantformArticle newCategoryArticle = new MonitoringPlantformArticle();
                newCategoryArticle.setPlantformId(plantformId);

                newCategoryArticle.setTitle(item.get("标题") == null ? "" : item.get("标题").toString());
                newCategoryArticle.setUrl(item.get("网址") == null ? "" : item.get("网址").toString());
                list.add(newCategoryArticle);
            }
            if (list.size() > 0) {
                LambdaQueryWrapper<MonitoringPlantformArticle> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(MonitoringPlantformArticle::getPlantformId, plantformId);
                iMonitoringPlantformArticleService.remove(wrapper);
                iMonitoringPlantformArticleService.saveBatch(list);
            }
        } catch (IOException e) {
            log.error("读取文件失败", e);
        }
        return new ResultEntity<>(200, true, "上传成功");
    }



}
