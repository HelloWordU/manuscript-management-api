package com.rz.manuscript.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2022-06-06
 */
@Getter
@Setter
@TableName("category_article")
@ApiModel(value = "CategoryArticle对象", description = "")
public class CategoryArticle implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer categoryId;

    private String title;

    private String url;

    @ApiModelProperty("媒体")
    private String media;

    @ApiModelProperty("版面、作者")
    private String sheet;

    private Integer readCount;

    private Integer commentCount;


}
