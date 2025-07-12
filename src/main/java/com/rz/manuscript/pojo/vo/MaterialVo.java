package com.rz.manuscript.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialVo {
    private Integer id;

    @ApiModelProperty("项目")
    private Integer projectId;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("产品")
    private Integer productId;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("文件路径")
    private String fileUrl;

    @ApiModelProperty("文件原始名称")
    private String originName;

    @ApiModelProperty("创建时间")
    private Date createTime;


}
