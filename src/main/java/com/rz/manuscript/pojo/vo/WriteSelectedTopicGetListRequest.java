package com.rz.manuscript.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("范文列表请求")
public class WriteSelectedTopicGetListRequest {
    @ApiModelProperty("是否完成")
    private Boolean isComplete;

    @ApiModelProperty("范文标题")
    private String title;

    @ApiModelProperty("范文类型")
    private Integer typeId;

    @ApiModelProperty("品牌")
    private String projectName;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("最小字数")
    private Integer charCountLimit;

    @ApiModelProperty("最大字数")
    private Integer charCountMax;

    //分页
    private Integer pageSize;
    private Integer pageIndex;
    private Integer startIndex;
}

