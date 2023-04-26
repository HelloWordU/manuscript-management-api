package com.rz.manuscript.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("范文列表视图")
public class WriteSelectedTopicVo {
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
}
