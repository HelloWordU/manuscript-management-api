package com.rz.manuscript.pojo.vo;

import io.swagger.models.auth.In;
import lombok.Data;

import java.util.Date;

@Data
public class ExamineSettleVo {

    private Integer id;

    private Integer projectId;

    private Integer orderId;

    private Boolean isChecked;

    private Date createTime;

    private Integer createUserId;

    private Integer checkUserId;

    private Date checkTime;

    private String projectName;

    private String supplierName;

    private Integer manuscriptCount;
}
