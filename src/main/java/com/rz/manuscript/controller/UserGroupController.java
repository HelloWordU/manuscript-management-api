package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.GroupProjectRelation;
import com.rz.manuscript.entity.UserGroup;
import com.rz.manuscript.entity.UserGroupRelation;
import com.rz.manuscript.pojo.vo.*;
import com.rz.manuscript.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/userGroup")
@Api(tags = "用户分组")
public class UserGroupController {
    @Resource
    private HttpServletRequest request;
    @Resource
    private IUserService iUserService;

    @Resource
    private IUserGroupService iUserGroupService;

    @Resource
    private ICompanyService iCompanyService;

    @Resource
    private IGroupProjectRelationService iGroupProjectRelationService;

    @Resource
    private IUserGroupRelationService iUserGroupRelationService;

    @PostMapping("/getList")
    public ResultEntityList<UserGroupVo> getList() {
        List<UserGroupVo> res = iUserGroupService.getAllUserGroup();
        return new ResultEntityList<UserGroupVo>(200, res, "获取成功");
    }


    @PostMapping("/add")
    @ApiOperation(value = "新增用户")
    public ResultEntity<Boolean> save(@RequestBody UserGroup entity) {
        if (entity.getName() == null || entity.getName().equals("")) {
            return new ResultEntity<>(0, "用户名不能为空");
        }
        LambdaQueryWrapper<UserGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserGroup::getName, entity.getName());
        List<UserGroup> data = iUserGroupService.list(wrapper);
        if (data != null && data.size() > 0) {
            return new ResultEntity<>(-1, true, "当前用户分组已经存在了");
        }
        iUserGroupService.saveOrUpdate(entity);

        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除用户分组")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        iUserGroupService.removeById(id);
        return new ResultEntity<>(200, true, "操作成功");
    }


    @PostMapping("/getGroupProject")
    @ApiOperation(value = "获取分组项目")
    public ResultEntityList<GroupProjectVo> getGroupProject(@RequestParam Integer groupId) {
        List<GroupProjectVo> res = iUserGroupService.getGroupProject(groupId);
        return new ResultEntityList<>(200, res, "操作成功");
    }

    @PostMapping("/saveGroupProject")
    @ApiOperation(value = "保存分组项目")
    public ResultEntity<Boolean> saveGroupProject(@RequestBody SaveGroupProjectRequest request) {
        if (request.getProjectIds().size() < 1) {
            return new ResultEntity<>(0, "项目不能为空");
        }
        LambdaQueryWrapper<GroupProjectRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GroupProjectRelation::getGroupId, request.getGroupId());
        iGroupProjectRelationService.remove(wrapper);
        List<GroupProjectRelation> newList = new ArrayList<>();
        for (Integer projectId : request.getProjectIds())
        {
            GroupProjectRelation item = new GroupProjectRelation();
            item.setGroupId(request.getGroupId());
            item.setProjectId(projectId);
            newList.add(item);
        }
        iGroupProjectRelationService.saveBatch(newList);
        //List<GroupProjectVo> res = iUserGroupService.getGroupProject(groupId);
        return new ResultEntity<>(200,true, "操作成功");
    }

    @PostMapping("/getGroupUser")
    @ApiOperation(value = "获取分组用户")
    public ResultEntityList<GroupUserVo> getGroupUser(@RequestParam Integer groupId) {
        List<GroupUserVo> res = iUserGroupService.getGroupUser(groupId);
        return new ResultEntityList<>(200, res, "操作成功");
    }

    @PostMapping("/saveGroupUser")
    @ApiOperation(value = "保存分组项目")
    public ResultEntity<Boolean> saveGroupUser(@RequestBody SaveGroupUserRequest request) {
        if (request.getUserIds().size() < 1) {
            return new ResultEntity<>(0, "用户不能为空");
        }
        LambdaQueryWrapper<UserGroupRelation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserGroupRelation::getGroupId, request.getGroupId());
        iUserGroupRelationService.remove(wrapper);
        List<UserGroupRelation> newList = new ArrayList<>();
        for (Integer userId : request.getUserIds())
        {
            UserGroupRelation item = new UserGroupRelation();
            item.setGroupId(request.getGroupId());
            item.setUserId(userId);
            newList.add(item);
        }
        iUserGroupRelationService.saveBatch(newList);
        //List<GroupProjectVo> res = iUserGroupService.getGroupProject(groupId);
        return new ResultEntity<>(200,true, "操作成功");
    }

}
