package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.Material;
import com.rz.manuscript.mapper.MaterialMapper;
import com.rz.manuscript.service.IMaterialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资料库 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-05-25
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements IMaterialService {

}
