package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.Project;
import com.rz.manuscript.mapper.ProjectMapper;
import com.rz.manuscript.service.IProjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-07
 */
@Service
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Override
    public List<Project> getUserProject(Integer userId) {
        return baseMapper.getUserProject(userId);
    }
}
