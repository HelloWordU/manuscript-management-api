package com.rz.manuscript.pojo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class BatchAddPictureRequest {

    @ApiModelProperty("图片地址")
    private List<String> imageUrls;

    @ApiModelProperty("项目Id")
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
