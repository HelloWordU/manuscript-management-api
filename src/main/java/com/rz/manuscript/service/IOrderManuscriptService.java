package com.rz.manuscript.service;

import com.rz.manuscript.entity.OrderManuscript;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.pojo.vo.OrderManuscriptVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-31
 */
public interface IOrderManuscriptService extends IService<OrderManuscript> {

    List<OrderManuscriptVo> getOrderManuscriptList(Integer orderId);
}
