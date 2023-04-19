package com.rz.manuscript.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ManuscriptVo {

    private Integer id;

    private String title;

    private Integer projectId;

    private String  project;

    private String auth;

    private Integer typeId;

    private String type;

    private Integer productId;

    private String product;

    private Integer charCount;

    private String fileName;

    private String savePath;

    private String url;

    private String shotScreenUrl;

    private Boolean isPublish;

    private String content;

    private Integer originalPresent;

    private Boolean isChecked;

    private  Integer checkUserId;

    private Date checkTime;

    private Date createTime;

    private Date updateTime;

    //0 未提交 1 提交待审核  2审核通过 3未通过审核
    private  Integer checkState;

    private Boolean isCommitCheck;

    private Double repeatRate;

    private Integer maxRepeatRateManuscriptId;

    //0 未发布 1 提交订单  2 已发布 3 已确认发布
    private  Integer publishState;


}
