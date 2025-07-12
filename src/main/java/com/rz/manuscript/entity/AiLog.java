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
 * @since 2023-07-06
 */
@Getter
@Setter
@TableName("t_ai_log")
@ApiModel(value = "AiLog对象", description = "")
public class AiLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("数据主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("请求内容")
    private String content;

    @ApiModelProperty("返回结果")
    private String responseContent;


}
