package com.rz.manuscript.pojo.request;

import lombok.Data;

@Data
public class MaterialGetListRequest {
    private Integer projectId;
    private String fileName;
    private Integer pageSize;
    private Integer pageIndex ;
}
