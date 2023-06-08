package com.rz.manuscript.pojo.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("热文选题上传视图")
public class SelectTopicUploadVo {

    @ApiModelProperty("热文选题标题")
    @ExcelProperty(value = "标题", index = 0)
    private String title;

    @ApiModelProperty("热文选题来源")
    @ExcelProperty(value = "来源", index = 1)
    private String source;

    @ApiModelProperty("类型")
    @ExcelProperty(value = "类型", index = 2)
    private String industry;

    @ApiModelProperty("链接地址")
    @ExcelProperty(value = "链接", index = 3)
    private String url;
}
