package com.rz.manuscript.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.MonitoringReachingStandardCount;
import com.rz.manuscript.pojo.vo.MonitoringReachingStandardCountVo;
import com.rz.manuscript.service.IMonitoringReachingStandardCountService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 监控达标率 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-06-18
 */
@RestController
@RequestMapping("/monitoringReachingStandardCount")
@Slf4j
@Api(tags ="监控达标")
public class MonitoringReachingStandardCountController {

    @Resource
    private IMonitoringReachingStandardCountService iMonitoringReachingStandardCountService;

    @GetMapping("/getSelfTodayMonitoring")
    public ResultEntity<MonitoringReachingStandardCount> getSelfTodayMonitoring(@RequestParam int categoryId) {

        MonitoringReachingStandardCount res = null;
        try {
            res = iMonitoringReachingStandardCountService.getSelfTodayMonitoring(categoryId);

        } catch (Exception e) {

        }

        return new ResultEntity(200, res, "获取成功");
    }

    @GetMapping("/getWeekMonitoring")
    public ResultEntityList<MonitoringReachingStandardCountVo> getWeekMonitoring(@RequestParam int categoryId) {

        List<MonitoringReachingStandardCountVo> res = new ArrayList<>();
        try {
            List<MonitoringReachingStandardCount> dataRes = iMonitoringReachingStandardCountService.getWeekMonitoring(categoryId);
            if (dataRes != null) {
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                for (MonitoringReachingStandardCount data : dataRes) {
                    MonitoringReachingStandardCountVo item = new MonitoringReachingStandardCountVo();
                    BeanUtil.copyProperties(data, item);
                    item.setMonitoringDate(data.getMonitoringDate().format(fmt));
                    switch (item.getType()) {
                        case 1:
                            item.setTypeName("自身");
                            break;
                        case 2:
                            item.setTypeName("竞品");
                            break;
                        case 3:
                            item.setTypeName("行业");
                            break;
                        case 4:
                            item.setTypeName("自身竞品对比");
                            break;
                    }
                    res.add(item);
                }
            }
        } catch (Exception e) {

        }
        return new ResultEntityList(200, res, "获取成功");
    }

    @PostMapping("/save")
    public ResultEntity<Boolean> save(@RequestBody MonitoringReachingStandardCount data) {

        if (data.getCategoryId() == null || data.getCategoryId() < 1) {
            return new ResultEntity(0, false, "无效的关键词");
        }
        try {
            if(data.getId()==null || data.getId()<1)
            {
                LambdaQueryWrapper<MonitoringReachingStandardCount> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(MonitoringReachingStandardCount::getType, data.getType());
                wrapper.eq(MonitoringReachingStandardCount::getMonitoringDate,data.getMonitoringDate());
                iMonitoringReachingStandardCountService.remove(wrapper);
            }
            iMonitoringReachingStandardCountService.saveOrUpdate(data);
            return new ResultEntity(200, true, "保存成功");
        } catch (Exception e) {
            log.error("CategoryHotWord 保存失败", e);
            return new ResultEntity(0, false, "保存失败");
        }

    }

    @PostMapping("/delete")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        try {
            iMonitoringReachingStandardCountService.removeById(id);
            return new ResultEntity(200, true, "删除成功");
        } catch (Exception e) {
            log.error("iMonitoringReachingStandardCountService 删除失败", e);
            return new ResultEntity(0, false, "删除失败");
        }

    }

    @PostMapping("/upload")
    public ResultEntity<Boolean> upload(@RequestParam Integer categoryId,@RequestParam Integer type, @RequestParam MultipartFile uploadFile) {
        if (categoryId == null || categoryId < 1) {
            return new ResultEntity<>(0, true, "无效的关键词");
        }
        if (type == null || type < 1) {
            return new ResultEntity<>(0, true, "无效的监控类型");
        }
        if (uploadFile.isEmpty())
            return new ResultEntity<>(0, true, "无效的文件");
        if (!FileNameUtil.extName(uploadFile.getOriginalFilename()).equals("xlsx") && !FileNameUtil.extName(uploadFile.getOriginalFilename()).equals("xls")) {
            return new ResultEntity<>(0, true, "无效的文件格式，只支持xlsx、xls");
        }

        try {
            ExcelReader reader = ExcelUtil.getReader(uploadFile.getInputStream());
            List<MonitoringReachingStandardCount> list = new ArrayList<>();
            List<Map<String, Object>> maps = reader.readAll();
            List<LocalDate> allDate = new ArrayList<>();
           // DateFormat fmt = DateFormat.getInstance().("yyyy-MM-dd");
            for (Map<String, Object> item : maps) {
                MonitoringReachingStandardCount entity = new MonitoringReachingStandardCount();
                entity.setType(type);
                entity.setCategoryId(categoryId);

                String data = item.get("监控日期") == null ? "" : ((DateTime)item.get("监控日期")).toString("yyyy-MM-dd");
                if(data.equals(""))
                {
                    continue;
                }
                LocalDate monitoringDate = LocalDate.parse(data);
                if(allDate.contains(monitoringDate))
                    continue;
                allDate.add(monitoringDate);
                entity.setMonitoringDate(monitoringDate);

                Integer reachingStandardCount = 0 ;
                try{
                    reachingStandardCount = Integer.parseInt(item.get("达标数量") == null ? "0" : item.get("达标数量").toString());
                   if(reachingStandardCount>6)
                       reachingStandardCount = 6;
                }catch(Exception e) {}
                entity.setReachingStandardCount(reachingStandardCount);
                list.add(entity);
            }
            if (list.size() > 0) {
                LambdaQueryWrapper<MonitoringReachingStandardCount> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(MonitoringReachingStandardCount::getType, type);
                wrapper.in(MonitoringReachingStandardCount::getMonitoringDate,allDate);
                iMonitoringReachingStandardCountService.remove(wrapper);
                iMonitoringReachingStandardCountService.saveBatch(list);
            }
        } catch (IOException e) {
            log.error("读取文件失败", e);
        }
        return new ResultEntity<>(200, true, "上传成功");
    }



}
