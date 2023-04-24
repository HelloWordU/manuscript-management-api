package com.rz.manuscript.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@TableName("t_write_selected_topic")
@ApiModel(value = "WriteSelectTopic对象", description = "")
public class WriteSelectedTopic implements Serializable  {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String title;
    private Integer productId;
    private Integer charCount;
    private String url;
    private Integer typeId;
    private Boolean is_complete;
    private String content;
    private Integer toptic_id;

    // getter and setter methods for all fields
}

