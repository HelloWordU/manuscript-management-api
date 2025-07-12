package com.rz.manuscript.mapper;

import com.rz.manuscript.entity.UserMenuDefault;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rz.manuscript.pojo.vo.MenuVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-09-05
 */
@Mapper
public interface UserMenuDefaultMapper extends BaseMapper<UserMenuDefault> {

    List<MenuVo> getUserMenuList(int userType);

    List<MenuVo> getUserMenuListWithRole(Integer userId);
}
