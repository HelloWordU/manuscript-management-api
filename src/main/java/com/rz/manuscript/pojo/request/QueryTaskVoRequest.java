package com.rz.manuscript.pojo.request;

import lombok.Data;

@Data
public class QueryTaskVoRequest {
    private Long projectId;
    private String keyword;
    private Integer pageIndex;
    private Integer pageSize;
}
