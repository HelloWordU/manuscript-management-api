package com.rz.manuscript.service;

import com.rz.manuscript.entity.MonitoringPlantformStatistic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.pojo.vo.MonitoringPlantPageDataVo;
import com.rz.manuscript.pojo.vo.MonitoringPlantformStatisticVo;

import java.util.List;

/**
 * <p>
 * 监控平台统计 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-06-18
 */
public interface IMonitoringPlantformStatisticService extends IService<MonitoringPlantformStatistic> {

    List<MonitoringPlantformStatisticVo> getByCategoryId(int categoryId);

    MonitoringPlantPageDataVo getPageDataByCategoryId(int categoryId);
}
