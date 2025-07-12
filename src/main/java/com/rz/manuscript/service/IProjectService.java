package com.rz.manuscript.service;

import com.rz.manuscript.entity.Project;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-07
 */
public interface IProjectService extends IService<Project> {

    List<Project> getUserProject(Integer userId);
}
