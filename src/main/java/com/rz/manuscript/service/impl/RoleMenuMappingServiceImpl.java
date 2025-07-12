package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.RoleMenuMapping;
import com.rz.manuscript.mapper.RoleMenuMappingMapper;
import com.rz.manuscript.pojo.request.QueryRoleMenuMappingVoRequest;
import com.rz.manuscript.pojo.vo.RoleMenuMappingVo;
import com.rz.manuscript.service.IRoleMenuMappingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-07-02
 */
@Service
public class RoleMenuMappingServiceImpl extends ServiceImpl<RoleMenuMappingMapper, RoleMenuMapping> implements IRoleMenuMappingService {

    @Override
    public List<RoleMenuMappingVo> queryVo(QueryRoleMenuMappingVoRequest queryRoleMenuMappingVoRequest) {
        return baseMapper.queryVo(queryRoleMenuMappingVoRequest);
    }
}
