package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.UserRoleMapping;
import com.rz.manuscript.mapper.UserRoleMappingMapper;
import com.rz.manuscript.pojo.request.QueryUserRoleMappingRequest;
import com.rz.manuscript.pojo.vo.UserRoleMappingVo;
import com.rz.manuscript.service.IUserRoleMappingService;
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
public class UserRoleMappingServiceImpl extends ServiceImpl<UserRoleMappingMapper, UserRoleMapping> implements IUserRoleMappingService {

    @Override
    public List<UserRoleMappingVo> queryVo(QueryUserRoleMappingRequest queryUserRoleMappingRequest) {
        return baseMapper.queryVo(queryUserRoleMappingRequest);
    }
}
