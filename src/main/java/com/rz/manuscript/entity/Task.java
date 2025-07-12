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
 * @since 2023-07-21
 */
@Getter
@Setter
@TableName("t_task")
@ApiModel(value = "Task对象", description = "")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("关键词")
    private String keyWord;

    @ApiModelProperty("项目")
    private Integer projectId;

    @ApiModelProperty("最后更新时间")
    private Date createTime;

    @ApiModelProperty("最后更新时间")
    private Date lastModifyTime;

    @ApiModelProperty("创建人")
    private Integer createUserId;

    private Boolean result;

    private String message;

    private Integer status;

}
