package com.rz.manuscript.service;

import com.rz.manuscript.entity.UserMenuDefault;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.pojo.vo.MenuVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-09-05
 */
public interface IUserMenuDefaultService extends IService<UserMenuDefault> {

    List<MenuVo> getUserMenuList(int userType);
}
