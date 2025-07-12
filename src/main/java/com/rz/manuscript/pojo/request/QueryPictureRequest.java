package com.rz.manuscript.pojo.request;

import lombok.Data;

@Data
public class QueryPictureRequest {
    private Long projectId;
    private Integer pageIndex;
    private Integer pageSize;
}
