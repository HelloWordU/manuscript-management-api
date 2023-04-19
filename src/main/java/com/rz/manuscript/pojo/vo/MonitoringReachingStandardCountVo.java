package com.rz.manuscript.pojo.vo;

import lombok.Data;

@Data
public class MonitoringReachingStandardCountVo {
    private Integer id;
    private Integer type;
    private String typeName;
    private String monitoringDate;
    private Integer reachingStandardCount;
    private Integer categoryId;
}
