package com.rz.manuscript.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class OrderSaveRequest {

    private Integer projectId;

    private Integer supplierId;

    private List<Integer> manuscriptIdList;
}
