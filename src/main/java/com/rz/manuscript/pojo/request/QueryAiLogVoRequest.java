package com.rz.manuscript.pojo.request;

import lombok.Data;

@Data
public class QueryAiLogVoRequest {
    private Integer userId;
    private String content;
    private Integer pageIndex;
    private Integer pageSize;
}
