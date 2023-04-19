package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.OrderManuscript;
import com.rz.manuscript.mapper.OrderManuscriptMapper;
import com.rz.manuscript.pojo.vo.OrderManuscriptVo;
import com.rz.manuscript.service.IOrderManuscriptService;
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
public class OrderManuscriptServiceImpl extends ServiceImpl<OrderManuscriptMapper, OrderManuscript> implements IOrderManuscriptService {

    @Resource
    private OrderManuscriptMapper orderManuscriptMapper;

    @Override
    public List<OrderManuscriptVo> getOrderManuscriptList(Integer orderId) {
        return orderManuscriptMapper.getOrderManuscriptList(orderId);
    }
}
