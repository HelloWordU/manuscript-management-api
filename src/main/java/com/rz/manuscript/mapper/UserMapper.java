package com.rz.manuscript.mapper;

import com.rz.manuscript.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rz.manuscript.pojo.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-06-06
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    List<UserVo> getAllUser();

    List<Long> getUserProjectList(Integer userId);
}
