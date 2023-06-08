package com.rz.manuscript.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PictureVo {


    @ApiModelProperty("数据主键")
    private Integer id;

    @ApiModelProperty("行业")
    private String industry;

    @ApiModelProperty("数据")
    private String imageData;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("图片地址")
    private String imageUrl;
}
