package com.rz.manuscript.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("范文列表视图")
public class WriteSelectedTopicVo {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("原文标题")
    private String originalTitle;

    @ApiModelProperty("范文标题")
    private String title;

    @ApiModelProperty("范文字数")
    private Integer charCount;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("范文内容")
    private String content;

    @ApiModelProperty("是否完成")
    private boolean isComplete;

    @ApiModelProperty("最后更新时间")
    private Date lastModifyDate;

    @ApiModelProperty("项目id")
    private Integer projectId;

    @ApiModelProperty("项目名称")
    private String projectName;

}
