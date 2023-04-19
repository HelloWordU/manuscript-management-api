package com.rz.manuscript.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class OrderVo {

    private Integer id;

    private Integer state;

    private Integer supplierId;

    private Integer projectId;

    private Integer createUser;

    private Date createTime;

    private Date updateTime;

    private Integer manuscriptCount;

    private Integer manuscriptPublishCount;

    private Boolean isBeginOrder;

    private Boolean isComplete;

    private String supplierName;

    private String projectName;
}
