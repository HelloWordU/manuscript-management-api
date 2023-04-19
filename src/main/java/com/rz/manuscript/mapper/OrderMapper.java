package com.rz.manuscript.mapper;

import com.rz.manuscript.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rz.manuscript.pojo.vo.OrderGetListRequest;
import com.rz.manuscript.pojo.vo.OrderVo;
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
public interface OrderMapper extends BaseMapper<Order> {

    List<OrderVo> getList(OrderGetListRequest request);

    Long getListTotal(OrderGetListRequest request);

    Long getSupplierListTotal(OrderGetListRequest request);

    List<OrderVo> getSupplierList(OrderGetListRequest request);
}
