package com.rz.manuscript.service;

import com.rz.manuscript.entity.UserGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.pojo.vo.GroupProjectVo;
import com.rz.manuscript.pojo.vo.GroupUserVo;
import com.rz.manuscript.pojo.vo.UserGroupVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-11
 */
public interface IUserGroupService extends IService<UserGroup> {

    List<UserGroupVo> getAllUserGroup();

    List<GroupProjectVo> getGroupProject(Integer groupId);

    List<GroupUserVo> getGroupUser(Integer groupId);
}
