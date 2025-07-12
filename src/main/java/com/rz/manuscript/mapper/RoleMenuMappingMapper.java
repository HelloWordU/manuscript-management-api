package com.rz.manuscript.mapper;

import com.rz.manuscript.entity.RoleMenuMapping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rz.manuscript.pojo.request.QueryRoleMenuMappingVoRequest;
import com.rz.manuscript.pojo.vo.RoleMenuMappingVo;
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
public interface RoleMenuMappingMapper extends BaseMapper<RoleMenuMapping> {

    List<RoleMenuMappingVo> queryVo(QueryRoleMenuMappingVoRequest queryRoleMenuMappingVoRequest);
}
