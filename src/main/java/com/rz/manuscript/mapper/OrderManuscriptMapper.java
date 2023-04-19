package com.rz.manuscript.mapper;

import com.rz.manuscript.entity.OrderManuscript;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rz.manuscript.pojo.vo.OrderManuscriptVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-08-31
 */
@Mapper
public interface OrderManuscriptMapper extends BaseMapper<OrderManuscript> {

    List<OrderManuscriptVo> getOrderManuscriptList(Integer orderId);
}
