package com.rz.manuscript.pojo.vo;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class  GetManuscriptRequest {
    private String project;
    private Integer projectId;
    private String title;
    private Integer type;
    private Integer product;
    private Integer checkState;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer startIndex;
}