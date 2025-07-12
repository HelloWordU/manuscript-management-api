package com.rz.manuscript.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@TableName("t_select_topic")
@ApiModel(value = "SelectTopic对象", description = "")
public class SelectTopic implements Serializable {

    private static final long serialVersionUID = 5321660990208890000L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private String auth;
    private String source;
    private Integer productId;
    private Integer charCount;
    private String url;
    private Date publishTime;
    private Integer typeId;
    private Boolean isSelected;
    private String industry;
    private String content;
    private Boolean isDownload;
    private Integer createUser;

    // getter and setter methods for all fields
}

