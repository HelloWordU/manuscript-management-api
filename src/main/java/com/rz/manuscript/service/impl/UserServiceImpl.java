package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.User;
import com.rz.manuscript.mapper.UserMapper;
import com.rz.manuscript.pojo.vo.UserVo;
import com.rz.manuscript.service.IUserService;
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
 * @since 2022-06-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Resource
    private UserMapper userMapper;
    @Override
    public List<UserVo> getAllUser() {
        return userMapper.getAllUser();
    }
}
