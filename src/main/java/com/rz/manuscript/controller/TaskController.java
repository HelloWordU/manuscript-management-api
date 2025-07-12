package com.rz.manuscript.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rz.manuscript.client.TaskExecClient;
import com.rz.manuscript.client.TaskExecRequest;
import com.rz.manuscript.common.AssertUtil;
import com.rz.manuscript.common.LoginUserUtils;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.*;
import com.rz.manuscript.exception.RZException;
import com.rz.manuscript.pojo.request.QueryPictureRequest;
import com.rz.manuscript.pojo.request.QuerySentenceByContentRequest;
import com.rz.manuscript.pojo.request.QueryTaskVoRequest;
import com.rz.manuscript.pojo.request.UploadTaskResultRequest;
import com.rz.manuscript.pojo.vo.SentenceVo;
import com.rz.manuscript.pojo.vo.TaskVo;
import com.rz.manuscript.service.IProjectService;
import com.rz.manuscript.service.ITaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
@Api(tags = "任务")
@Slf4j
public class TaskController {

    @Resource
    private HttpServletRequest request;
    @Resource
    private ITaskService iTaskService;
    @Resource
    private IProjectService iProjectService;

    @Resource
    private TaskExecClient taskExecClient;

    @PostMapping("/query")
    @ApiOperation(value = "获取任务列表")
    public ResultEntityList<TaskVo> query(@RequestBody QueryTaskVoRequest queryTaskVoRequest) {
        //获取当前用户项目的图片
        User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
        if (currentLoginUser == null)
            return new ResultEntityList<>(500, null, "无效的登录用户");
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        Long projectId = queryTaskVoRequest.getProjectId();
        if (projectId != null) {
            wrapper.eq(Task::getProjectId, projectId);
        }
        if (queryTaskVoRequest.getKeyword() != null) {
            wrapper.like(Task::getKeyWord, queryTaskVoRequest.getKeyword());
        }
        Page<Task> s = new Page<>(queryTaskVoRequest.getPageIndex(), queryTaskVoRequest.getPageSize());
        Page<Task> page1 = iTaskService.page(s, wrapper);
        List<Integer> projectIds = page1.getRecords().stream().map(i -> i.getProjectId()).collect(Collectors.toList());
        List<Project> projects = new ArrayList<>();
        if (!projectIds.isEmpty()) {
            projects = iProjectService.listByIds(projectIds);
        }
        List<TaskVo> resultList = new ArrayList<>();
        for (Task item : page1.getRecords()
        ) {
            TaskVo newItem = new TaskVo();
            BeanUtils.copyProperties(item, newItem);
            Project p = projects.stream().filter(i -> i.getId().equals(item.getProjectId())).findFirst().orElse(null);
            if (p != null) {
                newItem.setProjectName(p.getName());
            }
            resultList.add(newItem);
        }
        ResultEntityList<TaskVo> res = new ResultEntityList<>(200, resultList, "获取成功");
        res.setPageIndex(queryTaskVoRequest.getPageIndex());
        res.setPageSize(queryTaskVoRequest.getPageSize());
        res.setTotal(page1.getTotal());
        return res;
    }

    @PostMapping("/save")
    @ApiOperation("保存任务")
    public ResultEntity<Boolean> save(@RequestBody Task entity) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
            AssertUtil.notNull(currentLoginUser, "无效的登录用户");
            AssertUtil.notNull(entity, "无效的请求");
            AssertUtil.notNull(entity.getProjectId(), "无效的项目");
            AssertUtil.notNull(entity.getKeyWord(), "无效的关键词");
            if (entity.getId() == null) {
                entity.setCreateTime(new Date());
                entity.setCreateUserId(currentLoginUser.getId());
            } else {
                entity.setLastModifyTime(new Date());
            }
            iTaskService.saveOrUpdate(entity);
        } catch (RZException ex) {
            res.setCode(500);
            res.setMessage(ex.getMessage());
        } catch (Exception e) {
            log.error("保存任务", e);
        }
        return res;
    }


    @PostMapping("/startTask")
    @ApiOperation("开始采集")
    public ResultEntity<Boolean> startTask(@RequestParam Integer id) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            Task byId = iTaskService.getById(id);
            AssertUtil.notNull(byId, "无效的任务");
            byId.setStatus(1);
            TaskExecRequest request1 = new TaskExecRequest();
            request1.setProjectId(byId.getProjectId().toString());
            request1.setTaskId(byId.getId().toString());
            request1.setKeyWord(byId.getKeyWord());
            String exec = taskExecClient.exec(request1);
            log.info("开启采集任务返回结果:{}", exec);
            JSONObject jsonObject = JSON.parseObject(exec);
            String taskId = jsonObject.getString("taskid");
            AssertUtil.isTrue(!StringUtils.isEmpty(taskId), exec);
            iTaskService.updateById(byId);
        } catch (RZException ex) {
            res.setCode(500);
            res.setMessage(ex.getMessage());
        } catch (Exception e) {
            log.error("开始采集", e);
            res.setCode(500);
            res.setMessage("系统运行出错");
        }
        return res;
    }

    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            AssertUtil.notNull(id, "无效的任务");
            iTaskService.removeById(id);
        } catch (RZException ex) {
            res.setCode(500);
            res.setMessage(ex.getMessage());
        } catch (Exception e) {
            log.error("删除", e);
            res.setCode(500);
            res.setMessage("系统运行出错");
        }
        return res;
    }


    @PostMapping("/uploadResult")
    public ResultEntity<Boolean> uploadResult(@RequestBody UploadTaskResultRequest request) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            log.info("uploadResult结果:{}", JSON.toJSONString(request));
            AssertUtil.notNull(request, "无效的请求");
            AssertUtil.notNull(request.getTaskId(), "无效的任务");
            AssertUtil.notNull(request.getIsOk(), "无效的任务结果");
            Task byId = iTaskService.getById(request.getTaskId());
            AssertUtil.notNull(byId, "无效的任务");
            byId.setResult(request.getIsOk());
            if (request.getIsOk()) {
                byId.setStatus(2);
            } else {
                byId.setStatus(3);
            }
            byId.setLastModifyTime(new Date());
            byId.setMessage(request.getMsg());
            iTaskService.updateById(byId);
            res.setData(true);
            return res;

        } catch (RZException ex) {
            res.setCode(500);
            res.setMessage(ex.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统出错");
            log.error("queryDeformText 异常", e);
        }
        return res;
    }
}
