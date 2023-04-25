package com.rz.manuscript.pojo.vo;

import lombok.Data;

@Data
public class SelectTopicGetListRequest {
    private Boolean selectType;
    private String title;
    private Integer typeId;
    private String projectName;
    private String productName;
    private Integer charCountLimit;
    private Integer charCountMax;


    //分页
    private Integer pageSize;
    private Integer pageIndex;
    private Integer startIndex;
}

