package com.rz.manuscript.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.entity.MonitoringPlantformConfig;
import com.rz.manuscript.entity.MonitoringPlantformStatistic;
import com.rz.manuscript.entity.MonitoringReachingStandardCount;
import com.rz.manuscript.entity.ProjectCategory;
import com.rz.manuscript.mapper.MonitoringPlantformStatisticMapper;
import com.rz.manuscript.pojo.vo.MonitoringPlantPageDataVo;
import com.rz.manuscript.pojo.vo.MonitoringPlantformStatisticVo;
import com.rz.manuscript.service.IMonitoringPlantformConfigService;
import com.rz.manuscript.service.IMonitoringPlantformStatisticService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rz.manuscript.service.IMonitoringReachingStandardCountService;
import com.rz.manuscript.service.IProjectCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 监控平台统计 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-06-18
 */
@Service
public class MonitoringPlantformStatisticServiceImpl extends ServiceImpl<MonitoringPlantformStatisticMapper, MonitoringPlantformStatistic> implements IMonitoringPlantformStatisticService {

    @Resource
    private MonitoringPlantformStatisticMapper monitoringPlantformStatisticMapper;
    @Resource
    private IMonitoringPlantformConfigService iMonitoringPlantformConfigService;
    @Resource
    private IMonitoringReachingStandardCountService iMonitoringReachingStandardCountService;

    @Resource
    private IProjectCategoryService iProjectCategoryService;

    @Override
    public List<MonitoringPlantformStatisticVo> getByCategoryId(int categoryId) {
        return monitoringPlantformStatisticMapper.getByCategoryId(categoryId);
    }

    @Override
    public MonitoringPlantPageDataVo getPageDataByCategoryId(int categoryId) {
        LambdaQueryWrapper<MonitoringPlantformConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringPlantformConfig::getCategoryId, categoryId);
        //获取所有平台数据
        List<MonitoringPlantformConfig> list = iMonitoringPlantformConfigService.list(wrapper);
        //获取自身达标数据
        MonitoringReachingStandardCount selfTodayMonitoring = iMonitoringReachingStandardCountService.getSelfTodayMonitoring(categoryId);//
        //获取自身及竞品 七日达标情况
        List<MonitoringReachingStandardCount> weekMonitoring = iMonitoringReachingStandardCountService.getWeekMonitoring(categoryId);
        ProjectCategory byId = iProjectCategoryService.getById(categoryId);
        // 1 自身 2 竞品 3 行业 4 自身竞品对比"
        Map<String, Integer> zsData = new HashMap<>();
        Map<String, Integer> jpData = new HashMap<>();
        Map<String, Integer> hyData = new HashMap<>();
        Map<String, Integer> dbData = new HashMap<>();
        List<String> timeDataRes = new ArrayList<>();
        Random r = new Random();
        Integer totalNum = 6;
        for (int j = 0; j < 7; j++) {
            String Key = LocalDate.now().plusDays(0 - j).format(DateTimeFormatter.ofPattern("MM月dd日"));

            zsData.put(Key, 0);
            jpData.put(Key, 0);
            hyData.put(Key,0);
            dbData.put(Key, 0);
            timeDataRes.add(Key);
        }
        for (MonitoringReachingStandardCount item : weekMonitoring) {
            String currentKey = item.getMonitoringDate().format(DateTimeFormatter.ofPattern("MM月dd日"));
            if (item.getType() == 1) {
                if (zsData.containsKey(currentKey)) {
                    zsData.put(currentKey, item.getReachingStandardCount());
                }
            }
            if (item.getType() == 2) {
                if (jpData.containsKey(currentKey)) {
                    jpData.put(currentKey, item.getReachingStandardCount());
                }
            }
            if (item.getType() == 3) {
                if (hyData.containsKey(currentKey)) {
                    hyData.put(currentKey, item.getReachingStandardCount());
                }
            }
            if (item.getType() == 4) {
                if (dbData.containsKey(currentKey)) {
                    dbData.put(currentKey, item.getReachingStandardCount());
                }
            }

        }
        MonitoringPlantPageDataVo res = new MonitoringPlantPageDataVo();

        Collections.reverse(timeDataRes);
        List<Integer> dbDataRes  = new ArrayList<Integer>();
        List<Integer> hyDataRes  = new ArrayList<Integer>();
        List<Integer> jpDataRes  = new ArrayList<Integer>();
        List<Integer> zsDataRes  = new ArrayList<Integer>();
        for (String date : timeDataRes)
        {
            dbDataRes.add(dbData.get(date));
            hyDataRes.add(hyData.get(date));
            jpDataRes.add(jpData.get(date));
            zsDataRes.add(zsData.get(date));
        }
        res.setDbData(dbDataRes);
        res.setHyData(hyDataRes);
        res.setJpData(jpDataRes);
        res.setTimeData(timeDataRes);
        res.setZsData(zsDataRes);

        int randomReachNum = 6;//r.nextInt(totalNum) + 1;
        res.setNoReachNum(totalNum - (selfTodayMonitoring == null ? randomReachNum : selfTodayMonitoring.getReachingStandardCount()));
        res.setPlantformData(list);
        res.setTotalNum(totalNum);
        res.setReachNum(selfTodayMonitoring == null ? randomReachNum : selfTodayMonitoring.getReachingStandardCount());
        res.setCategoryName(byId.getName());
        return res;
    }
}
