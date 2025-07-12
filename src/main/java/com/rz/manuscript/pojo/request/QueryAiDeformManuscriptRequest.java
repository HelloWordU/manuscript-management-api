package com.rz.manuscript.pojo.request;

import lombok.Data;

@Data
public class QueryAiDeformManuscriptRequest {
    private String title;
    private Integer pageIndex;
    private Integer pageSize;
}
