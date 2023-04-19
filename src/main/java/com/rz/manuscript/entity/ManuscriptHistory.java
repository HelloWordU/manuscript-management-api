package com.rz.manuscript.entity;

import cn.hutool.core.date.DateTime;
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
 * @since 2022-08-14
 */
@Getter
@Setter
@TableName("manuscript_history")
@ApiModel(value = "ManuscriptHistory对象", description = "")
public class ManuscriptHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer manuscriptId;

    private String title;

    private Integer charCount;

    private String fileName;

    private String savePath;

    private Date createTime;

    private Date updateTime;


}
