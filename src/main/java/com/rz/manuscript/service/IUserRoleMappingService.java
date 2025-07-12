package com.rz.manuscript.service;

import com.rz.manuscript.entity.UserRoleMapping;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.pojo.request.QueryUserRoleMappingRequest;
import com.rz.manuscript.pojo.vo.UserRoleMappingVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-07-02
 */
public interface IUserRoleMappingService extends IService<UserRoleMapping> {

    List<UserRoleMappingVo> queryVo(QueryUserRoleMappingRequest queryUserRoleMappingRequest);
}
