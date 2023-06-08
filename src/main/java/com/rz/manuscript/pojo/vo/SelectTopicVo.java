package com.rz.manuscript.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("热文选题视图")
public class SelectTopicVo {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("热文选题标题")
    private String title;

    @ApiModelProperty("热文选题字数")
    private Integer charCount;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("热文选题来源")
    private String source;

    @ApiModelProperty("行业")
    private String industry;

    @ApiModelProperty("链接地址")
    private String url;

    @ApiModelProperty("发布时间")
    private Date publishTime;

    @ApiModelProperty("是否已选")
    private Boolean isSelected;
}
