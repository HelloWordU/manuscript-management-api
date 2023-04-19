package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.Customer;
import com.rz.manuscript.mapper.CustomerMapper;
import com.rz.manuscript.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-12
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
