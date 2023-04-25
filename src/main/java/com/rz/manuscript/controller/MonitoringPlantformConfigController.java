package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.MonitoringPlantformConfig;
import com.rz.manuscript.service.IMonitoringPlantformConfigService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-06-18
 */
@RestController
@RequestMapping("/monitoringPlantform")
@Slf4j
@Api(tags ="监控平台配置")
public class MonitoringPlantformConfigController {

    @Resource
    private IMonitoringPlantformConfigService iMonitoringPlantformConfigService;

    @GetMapping("/getByCategoryId")
    public ResultEntityList<MonitoringPlantformConfig> getByCategoryId(@RequestParam int categoryId) {

        LambdaQueryWrapper<MonitoringPlantformConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringPlantformConfig::getCategoryId, categoryId);
        List<MonitoringPlantformConfig> list = iMonitoringPlantformConfigService.list(wrapper);
        return new ResultEntityList(200, list, "获取成功");
    }

    @PostMapping("/save")
    public ResultEntity<Boolean> save(@RequestBody MonitoringPlantformConfig data) {

        if (data.getCategoryId() == null || data.getCategoryId() < 1) {
            return new ResultEntity(0, false, "无效的关键词");
        }
        try {
            iMonitoringPlantformConfigService.saveOrUpdate(data);
            return new ResultEntity(200, true, "保存成功");
        } catch (Exception e) {
            log.error("CategoryHotWord 保存失败", e);
            return new ResultEntity(0, false, "保存失败");
        }

    }

    @PostMapping("/delete")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        try {
            iMonitoringPlantformConfigService.removeById(id);
            return new ResultEntity(200, true, "删除成功");
        } catch (Exception e) {
            log.error("MonitoringPlantformConfig 删除失败", e);
            return new ResultEntity(0, false, "删除失败");
        }

    }
}
