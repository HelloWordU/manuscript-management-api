package com.rz.manuscript.pojo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddSentenceRequest {
    private  String content;

    private Long projectId;

    @ApiModelProperty("搜索关键词")
    private String searchKey;
    //来源
    @ApiModelProperty("来源")
    private String origin;
    //，网址，
    @ApiModelProperty("网址")
    private String sourceUrl;
    // 标题
    @ApiModelProperty("标题")
    private String title;
}
