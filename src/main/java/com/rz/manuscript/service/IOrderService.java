package com.rz.manuscript.service;

import com.rz.manuscript.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.pojo.vo.OrderGetListRequest;
import com.rz.manuscript.pojo.vo.OrderVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-31
 */
public interface IOrderService extends IService<Order> {

    List<OrderVo> getList(OrderGetListRequest request);
    Long getListTotal(OrderGetListRequest request);

    Long getSupplierListTotal(OrderGetListRequest request);

    List<OrderVo> getSupplierList(OrderGetListRequest request);
}
