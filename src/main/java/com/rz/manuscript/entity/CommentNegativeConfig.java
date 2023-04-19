package com.rz.manuscript.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2022-07-11
 */
@Getter
@Setter
@TableName("comment_negative_config")
@ApiModel(value = "CommentNegativeConfig对象", description = "")
public class CommentNegativeConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer normalCount;

    private Integer negetiveCount;

    private Integer noCommentCount;

    private Integer last2HourNormalCount;

    private Integer last2HourNegetiveCount;

    private Integer last2HourNoCommentCount;

    private Integer last5HourNormalCount;

    private Integer last5HourNegetiveCount;

    private Integer last5HourNoCommentCount;

    private Integer last12HourNormalCount;

    private Integer last12HourNegetiveCount;

    private Integer last12HourNoCommentCount;

    private Integer last24HourNormalCount;

    private Integer last24HourNegetiveCount;

    private Integer last24HourNoCommentCount;

    private Integer categoryId;


}
