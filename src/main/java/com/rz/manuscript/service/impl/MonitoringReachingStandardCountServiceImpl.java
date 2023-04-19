package com.rz.manuscript.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.entity.MonitoringReachingStandardCount;
import com.rz.manuscript.mapper.MonitoringReachingStandardCountMapper;
import com.rz.manuscript.service.IMonitoringReachingStandardCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 监控达标率 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-06-18
 */
@Service
public class MonitoringReachingStandardCountServiceImpl extends ServiceImpl<MonitoringReachingStandardCountMapper, MonitoringReachingStandardCount> implements IMonitoringReachingStandardCountService {
    @Resource
    private IMonitoringReachingStandardCountService iMonitoringReachingStandardCountService;

    @Override
    public MonitoringReachingStandardCount getSelfTodayMonitoring(int categoryId) {
        LambdaQueryWrapper<MonitoringReachingStandardCount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringReachingStandardCount::getCategoryId, categoryId);
        wrapper.eq(MonitoringReachingStandardCount::getType, 1);
        wrapper.eq(MonitoringReachingStandardCount::getMonitoringDate, LocalDate.now());
        return iMonitoringReachingStandardCountService.getOne(wrapper);
    }

    @Override
    public List<MonitoringReachingStandardCount> getWeekMonitoring(int categoryId) {
        LambdaQueryWrapper<MonitoringReachingStandardCount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MonitoringReachingStandardCount::getCategoryId, categoryId);
        wrapper.le(MonitoringReachingStandardCount::getMonitoringDate, LocalDate.now());
        wrapper.ge(MonitoringReachingStandardCount::getMonitoringDate, LocalDate.now().plusDays(-7));
        return iMonitoringReachingStandardCountService.list(wrapper);
    }
}
