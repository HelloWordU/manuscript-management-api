package com.rz.manuscript.pojo.vo;

import lombok.Data;

@Data
public class WriteSelectedTopicGetListRequest {
    private Boolean isComplete;

    //分页
    private Integer pageSize;
    private Integer pageIndex;
    private Integer startIndex;
}

