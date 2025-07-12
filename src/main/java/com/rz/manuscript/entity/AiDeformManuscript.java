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
 * 
 * </p>
 *
 * @author baomidou
 * @since 2023-07-30
 */
@Getter
@Setter
@TableName("t_ai_deform_manuscript")
@ApiModel(value = "AiDeformManuscript对象", description = "")
public class AiDeformManuscript implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("保存路径")
    private String savePath;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty("创建人")
    private Integer createUserId;

    @ApiModelProperty("修改人")
    private Integer updateUserId;

    private Integer  charCount;

}
