package com.rz.manuscript.service;

import com.rz.manuscript.entity.MonitoringReachingStandardCount;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 监控达标率 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-06-18
 */
public interface IMonitoringReachingStandardCountService extends IService<MonitoringReachingStandardCount> {

    MonitoringReachingStandardCount getSelfTodayMonitoring(int categoryId);

    List<MonitoringReachingStandardCount> getWeekMonitoring(int categoryId);
}
