package com.rz.manuscript.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author baomidou
 * @since 2022-08-05
 */
@Getter
@Setter
@ApiModel(value = "Manuscript对象", description = "")
public class Manuscript implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private Integer projectId;

    private String auth;

    private Integer typeId;

    private Integer productId;

    private Integer charCount;

    private String fileName;

    private String url;

    private String shotScreenUrl;

    private Boolean isPublish;

    private String content;

    private Integer originalPresent;

    private String savePath;

    private Boolean isChecked;

    private Integer checkUserId;

    private Date checkTime;

    private Date createTime;

    private Date updateTime;

    /**
     * 0 未提交 1 提交待审核  2审核通过 3未通过审核
     */
    private Integer checkState;

    private Boolean isCommitCheck;

    private Double repeatRate;

    private Integer maxRepeatRateManuscriptId;

    /**
     * 0未发布 1 提交订单  2 已发布 3 已确认发布
     */
    private Integer publishState;
    private Integer createUserId;
}
