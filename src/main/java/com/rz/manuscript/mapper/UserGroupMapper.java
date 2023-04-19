package com.rz.manuscript.mapper;

import com.rz.manuscript.entity.UserGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rz.manuscript.pojo.vo.GroupProjectVo;
import com.rz.manuscript.pojo.vo.GroupUserVo;
import com.rz.manuscript.pojo.vo.UserGroupVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-08-11
 */
@Mapper
public interface UserGroupMapper extends BaseMapper<UserGroup> {

    List<UserGroupVo> getAllUserGroup();

    List<GroupProjectVo> getGroupProject(Integer groupId);

    List<GroupUserVo> getGroupUser(Integer groupId);
}
