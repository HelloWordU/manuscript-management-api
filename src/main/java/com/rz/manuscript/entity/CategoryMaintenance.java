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
 * 项目分类维护
 * </p>
 *
 * @author baomidou
 * @since 2022-06-06
 */
@Getter
@Setter
@TableName("category_maintenance")
@ApiModel(value = "CategoryMaintenance对象", description = "项目分类维护")
public class CategoryMaintenance implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer categoryId;

    private Integer writeTotal;

    private Integer writePlanTotal;

    private Integer publishTotal;

    private Integer publishPlanTotal;

    private Integer readTotal;

    private Integer readPlanTotal;


}
