package com.rz.manuscript.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 监控达标率
 * </p>
 *
 * @author baomidou
 * @since 2022-06-18
 */
@Getter
@Setter
@TableName("monitoring_reaching_standard_count")
@ApiModel(value = "MonitoringReachingStandardCount对象", description = "监控达标率")
public class MonitoringReachingStandardCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("1 自身 2 竞品 3 行业 4 自身竞品对比")
    private Integer type;

    private LocalDate monitoringDate;

    private Integer reachingStandardCount;

    private  Integer categoryId;


}
