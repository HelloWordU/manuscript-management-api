package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.Task;
import com.rz.manuscript.mapper.TaskMapper;
import com.rz.manuscript.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-07-21
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

}
