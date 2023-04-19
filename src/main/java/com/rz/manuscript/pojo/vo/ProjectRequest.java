package com.rz.manuscript.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectRequest {
    private String name;
    private Integer projectId;
    private Date beginDate;
    private Date endDate;
    private Integer pageSize;
    private Integer pageIndex;

}
