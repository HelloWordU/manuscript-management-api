package com.rz.manuscript.entity;

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
 * @since 2022-08-08
 */
@Getter
@Setter
@TableName("manuscript_type")
@ApiModel(value = "ManuscriptType对象", description = "")
public class ManuscriptType implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;


}
