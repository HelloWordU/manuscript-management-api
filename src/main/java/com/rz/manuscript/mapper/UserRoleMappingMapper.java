package com.rz.manuscript.mapper;

import com.rz.manuscript.entity.UserRoleMapping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rz.manuscript.pojo.request.QueryUserRoleMappingRequest;
import com.rz.manuscript.pojo.vo.UserRoleMappingVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2023-07-02
 */
@Mapper
public interface UserRoleMappingMapper extends BaseMapper<UserRoleMapping> {

    List<UserRoleMappingVo> queryVo(QueryUserRoleMappingRequest queryUserRoleMappingRequest);
}
