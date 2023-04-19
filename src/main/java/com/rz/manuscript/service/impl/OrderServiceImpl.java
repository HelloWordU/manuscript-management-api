package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.Order;
import com.rz.manuscript.mapper.OrderMapper;
import com.rz.manuscript.pojo.vo.GetManuscriptRequest;
import com.rz.manuscript.pojo.vo.OrderGetListRequest;
import com.rz.manuscript.pojo.vo.OrderVo;
import com.rz.manuscript.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-31
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public List<OrderVo> getList(OrderGetListRequest request) {
        setRequestPage(request);
        return orderMapper.getList(request);
    }
    @Override
    public Long getListTotal(OrderGetListRequest request) {
        setRequestPage(request);
        return orderMapper.getListTotal(request);
    }

    @Override
    public Long getSupplierListTotal(OrderGetListRequest request) {
        setRequestPage(request);
        return orderMapper.getSupplierListTotal(request);
    }

    @Override
    public List<OrderVo> getSupplierList(OrderGetListRequest request) {
        setRequestPage(request);
        return orderMapper.getSupplierList(request);
    }

    private void setRequestPage(OrderGetListRequest request) {
        if (request.getPageIndex() == null || request.getPageIndex() < 1)
            request.setPageIndex(1);
        if (request.getPageSize() == null || request.getPageSize() < 1)
            request.setPageSize(20);
        request.setStartIndex((request.getPageIndex()-1) * request.getPageSize());
    }
}
