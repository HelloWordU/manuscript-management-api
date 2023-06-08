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
 * 项目图片
 * </p>
 *
 * @author baomidou
 * @since 2023-05-25
 */
@Getter
@Setter
@TableName("t_picture_project")
@ApiModel(value = "PictureProject对象", description = "项目图片")
public class PictureProject implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("项目")
    private Integer projectId;

    @ApiModelProperty("产品")
    private Integer productId;

    @ApiModelProperty("数据")
    private String imageData;

    @ApiModelProperty("创建时间")
    private Date createTime;


}
