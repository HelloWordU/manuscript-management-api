package com.rz.manuscript.pojo.vo;

import lombok.Data;

@Data
public class MonitoringPlantformStatisticVo {
    private Integer id;

    private Integer plantformId;

    private String plantformName;

    private Boolean isReachingStandard;

    private String categoryName;
}
