package com.rz.manuscript.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
 * @since 2022-08-07
 */
@Getter
@Setter
@ApiModel(value = "Project对象", description = "")
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Date beginDate;

    private Date endDate;

    private Integer manuscriptCount;

    private Integer manuscriptUploadCount;

    private Integer manuscriptPublishCount;

    private Integer createUserId;

    private Date createTime;

    private Date modifyTime;

    private Integer orderCount;

    private Integer orderCompleteCount;


}
