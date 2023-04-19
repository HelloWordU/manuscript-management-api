package com.rz.manuscript.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 监控平台统计
 * </p>
 *
 * @author baomidou
 * @since 2022-06-18
 */
@Getter
@Setter
@TableName("monitoring_plantform_statistic")
@ApiModel(value = "MonitoringPlantformStatistic对象", description = "监控平台统计")
public class MonitoringPlantformStatistic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer plantformId;

    private Boolean isReachingStandard;


}
