package com.rz.manuscript.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("热文选题请求")
public class SelectTopicGetListRequest {
    @ApiModelProperty("选题是否被使用")
    private Boolean selectType;

    @ApiModelProperty("热文选题标题")
    private String title;

    @ApiModelProperty("行业")
    private String industry;

    @ApiModelProperty("来源")
    private String source;


    @ApiModelProperty("最小字数")
    private Integer charCountLimit;

    @ApiModelProperty("最大字数")
    private Integer charCountMax;


    //分页
    private Integer pageSize;
    private Integer pageIndex;
    private Integer startIndex;
}

