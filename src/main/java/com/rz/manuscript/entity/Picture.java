package com.rz.manuscript.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 图片
 * </p>
 *
 * @author baomidou
 * @since 2023-05-25
 */
@Getter
@Setter
@TableName("t_picture")
@ApiModel(value = "Picture对象", description = "图片")
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("行业")
    private String industry;

    @ApiModelProperty("数据")
    private String imageData;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("图片地址")
    private String imageUrl;


    @ApiModelProperty("项目id")
    private Long projectId;



}
