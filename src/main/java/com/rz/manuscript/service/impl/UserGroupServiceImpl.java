package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.UserGroup;
import com.rz.manuscript.mapper.UserGroupMapper;
import com.rz.manuscript.pojo.vo.GroupProjectVo;
import com.rz.manuscript.pojo.vo.GroupUserVo;
import com.rz.manuscript.pojo.vo.UserGroupVo;
import com.rz.manuscript.service.IUserGroupService;
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
 * @since 2022-08-11
 */
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup> implements IUserGroupService {

    @Resource
    private UserGroupMapper userGroupMapper;

    @Override
    public List<UserGroupVo> getAllUserGroup() {
        return userGroupMapper.getAllUserGroup();
    }

    @Override
    public List<GroupProjectVo> getGroupProject(Integer groupId) {
        return userGroupMapper.getGroupProject(groupId);
    }

    @Override
    public List<GroupUserVo> getGroupUser(Integer groupId) {
        return userGroupMapper.getGroupUser(groupId);
    }
}
