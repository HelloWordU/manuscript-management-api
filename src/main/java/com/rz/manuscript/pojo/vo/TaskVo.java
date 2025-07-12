package com.rz.manuscript.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class TaskVo {
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("关键词")
    private String keyWord;

    @ApiModelProperty("项目")
    private Integer projectId;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("最后更新时间")
    private Date lastModifyTime;

    @ApiModelProperty("创建人")
    private Integer createUserId;

    private Boolean result;

    private String message;

    private Integer status;
}
