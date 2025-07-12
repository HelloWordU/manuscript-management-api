package com.rz.manuscript.pojo.request;

import lombok.Data;

@Data
public class QuerySentenceByContentRequest {
    private String content;
    private Long projectId;
    private Integer pageIndex;
    private Integer pageSize;
}
