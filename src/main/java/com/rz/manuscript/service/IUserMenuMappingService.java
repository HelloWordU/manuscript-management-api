package com.rz.manuscript.service;

import com.rz.manuscript.entity.User;
import com.rz.manuscript.entity.UserMenuMapping;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.pojo.vo.MenuVo;

import javax.net.ssl.SSLSession;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-09-05
 */
public interface IUserMenuMappingService extends IService<UserMenuMapping> {

    List<MenuVo> getUserMenuList(Integer userId);
}
