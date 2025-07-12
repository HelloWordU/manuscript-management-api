package com.rz.manuscript.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AiLogVo {
    @ApiModelProperty("数据主键")
    private Integer id;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("用户真实姓名")
    private String userRealName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("请求内容")
    private String content;

    @ApiModelProperty("返回结果")
    private String responseContent;
}
