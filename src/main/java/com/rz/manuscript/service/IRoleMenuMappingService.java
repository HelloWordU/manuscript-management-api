package com.rz.manuscript.service;

import com.rz.manuscript.entity.RoleMenuMapping;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.pojo.request.QueryRoleMenuMappingVoRequest;
import com.rz.manuscript.pojo.vo.RoleMenuMappingVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-07-02
 */
public interface IRoleMenuMappingService extends IService<RoleMenuMapping> {

    List<RoleMenuMappingVo> queryVo(QueryRoleMenuMappingVoRequest queryRoleMenuMappingVoRequest);
}
