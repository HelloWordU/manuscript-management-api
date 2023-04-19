package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.UserMenuMapping;
import com.rz.manuscript.mapper.UserMenuMappingMapper;
import com.rz.manuscript.pojo.vo.MenuVo;
import com.rz.manuscript.service.IUserMenuMappingService;
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
public class UserMenuMappingServiceImpl extends ServiceImpl<UserMenuMappingMapper, UserMenuMapping> implements IUserMenuMappingService {

    @Resource
    private UserMenuMappingMapper userMenuMappingMapper;

    @Override
    public List<MenuVo> getUserMenuList(Integer userId) {
       return  userMenuMappingMapper.getUserMenuList(userId);
    }
}
