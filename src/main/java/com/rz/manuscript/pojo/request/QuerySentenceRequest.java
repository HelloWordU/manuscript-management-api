package com.rz.manuscript.pojo.request;

import lombok.Data;

import java.util.List;

@Data
public class QuerySentenceRequest {
    private List<String> tags;
    private List<String> categories;
    private Long projectId;
    private Integer pageIndex;
    private Integer pageSize;
}
