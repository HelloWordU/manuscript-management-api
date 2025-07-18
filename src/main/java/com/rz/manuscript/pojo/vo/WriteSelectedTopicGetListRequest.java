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


    //分页
    private Integer pageSize;
    private Integer pageIndex;
    private Integer startIndex;
    private Integer userId;
}

