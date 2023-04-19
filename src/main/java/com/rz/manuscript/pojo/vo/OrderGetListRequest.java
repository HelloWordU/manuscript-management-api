package com.rz.manuscript.pojo.vo;

import lombok.Data;

@Data
public class OrderGetListRequest {
    private Integer projectId;
    private Integer supplierId;
    private Boolean isBeginOrder;
    private Boolean isComplete;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer startIndex;
    private Integer userId;
}
