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
 * 资料库
 * </p>
 *
 * @author baomidou
 * @since 2023-05-25
 */
@Getter
@Setter
@TableName("t_material")
@ApiModel(value = "Material对象", description = "资料库")
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("项目")
    private Integer projectId;

    @ApiModelProperty("产品")
    private Integer productId;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("文件路径")
    private String fileUrl;

    @ApiModelProperty("创建时间")
    private Date createTime;


}
