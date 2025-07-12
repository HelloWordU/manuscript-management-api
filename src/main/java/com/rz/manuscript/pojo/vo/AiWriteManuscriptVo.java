package com.rz.manuscript.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AiWriteManuscriptVo {
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

    private String createUserName;

    @ApiModelProperty("修改人")
    private Integer updateUserId;


    private Integer  charCount;
}
