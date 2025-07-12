package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.UserMenuDefault;
import com.rz.manuscript.mapper.UserMenuDefaultMapper;
import com.rz.manuscript.pojo.vo.MenuVo;
import com.rz.manuscript.service.IUserMenuDefaultService;
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
 * @since 2022-09-05
 */
@Service
public class UserMenuDefaultServiceImpl extends ServiceImpl<UserMenuDefaultMapper, UserMenuDefault> implements IUserMenuDefaultService {

    @Resource
    private UserMenuDefaultMapper userMenuDefaultMapper;

    @Override
    public List<MenuVo> getUserMenuList(int userType) {
        return  userMenuDefaultMapper.getUserMenuList(userType);
    }

    @Override
    public List<MenuVo> getUserMenuListWithRole(Integer userId) {
        return  userMenuDefaultMapper.getUserMenuListWithRole(userId);
    }
}
