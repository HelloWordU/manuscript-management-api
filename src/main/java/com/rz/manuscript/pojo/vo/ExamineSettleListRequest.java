package com.rz.manuscript.pojo.vo;

import lombok.Data;

@Data
public class ExamineSettleListRequest {
    private Integer projectId;
    private Integer supplierId;
    private Boolean isChecked;
    private Integer orderId;
    private Integer pageSize;
    private Integer pageIndex;
    private Integer startIndex;
}
