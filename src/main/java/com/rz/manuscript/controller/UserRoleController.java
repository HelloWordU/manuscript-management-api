package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rz.manuscript.common.AssertUtil;
import com.rz.manuscript.common.CacheManager;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.*;
import com.rz.manuscript.exception.RZException;
import com.rz.manuscript.pojo.entity.CacheEntity;
import com.rz.manuscript.pojo.request.*;
import com.rz.manuscript.pojo.vo.*;
import com.rz.manuscript.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/userRole")
@Api(tags = "用户角色")
@Slf4j
public class UserRoleController {
    @Resource
    private HttpServletRequest request;
    @Resource
    private IUserService iUserService;

    @Resource
    private ISupplierService iSupplierService;

    @Resource
    private ICustomerService iCustomerService;

    @Resource
    private ICompanyService iCompanyService;

    @Resource
    private IRoleService iRoleService;

    @Resource
    private IUserRoleMappingService iUserRoleMappingService;

    @Resource
    private IRoleMenuMappingService iRoleMenuMappingService;

    @PostMapping("/getList")
    public ResultEntityList<UserRoleVo> getList(@RequestBody QueryUserRoleVoRequest queryUserRoleVoRequest) {
//        String accessToken = request.getHeader("accessToken");
//        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
//        if (cacheInfo == null) {
//            return new ResultEntityList<>(0, "当前用户获取失败,请重新登录");
//        }
        ResultEntityList<UserRoleVo> res = new ResultEntityList<>();
        try {
            LambdaQueryWrapper<Role> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            Page<Role> rolePage = new Page<>(queryUserRoleVoRequest.getPageIndex(), queryUserRoleVoRequest.getPageSize());
            rolePage = iRoleService.page(rolePage, userLambdaQueryWrapper);
            res.setTotal(rolePage.getTotal());
            res.setPageSize(queryUserRoleVoRequest.getPageSize());
            res.setPageIndex(queryUserRoleVoRequest.getPageIndex());
            List<Role> roleList = rolePage.getRecords();
            List<UserRoleVo> result = new ArrayList<>();
            if (!roleList.isEmpty()) {
                List<Integer> roleIds = roleList.stream().map(i -> i.getId()).collect(Collectors.toList());
                QueryUserRoleMappingRequest queryUserRoleMappingRequest = new QueryUserRoleMappingRequest();
                queryUserRoleMappingRequest.setRoleIds(roleIds);
                List<UserRoleMappingVo> userRoleMappingList = iUserRoleMappingService.queryVo(queryUserRoleMappingRequest);
                QueryRoleMenuMappingVoRequest queryRoleMenuMappingVoRequest = new QueryRoleMenuMappingVoRequest();
                queryRoleMenuMappingVoRequest.setRoleIds(roleIds);
                List<RoleMenuMappingVo> roleMenuMappingVoList = iRoleMenuMappingService.queryVo(queryRoleMenuMappingVoRequest);
                for (Role r : roleList) {
                    UserRoleVo item = new UserRoleVo();
                    item.setId(r.getId());
                    item.setName(r.getName());
                    List<String> userNameList = userRoleMappingList.stream().filter(i -> i.getRoleId().equals(r.getId())).map(i -> i.getUserRealName()).distinct().collect(Collectors.toList());
                    String userName = String.join(",", userNameList);
                    item.setUserName(userName);
                    List<String> menuNameList = roleMenuMappingVoList.stream().filter(i -> i.getRoleId().equals(r.getId())).map(i -> i.getMenuName()).distinct().collect(Collectors.toList());
                    String menuName = String.join(",", menuNameList);
                    item.setMenuName(menuName);
                    result.add(item);
                }
            }
            res.setData(result);


        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("用户角色列表获取失败", e);
        }

        return res;
    }


    @PostMapping("/add")
    @ApiOperation(value = "新增角色")
    public ResultEntity<Boolean> save(@RequestBody Role entity) {
        if (entity.getName() == null || entity.getName().equals("")) {
            return new ResultEntity<>(0, "用户名不能为空");
        }
        if (entity.getId() != null && entity.getId() > 0) {
            return edit(entity);
        } else {
            entity.setUpdateTime(new Date());
            LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Role::getName, entity.getName());
            List<Role> data = iRoleService.list(wrapper);
            if (data != null && data.size() > 0) {
                return new ResultEntity<>(-1, true, "当前用户已经存在了");
            }
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            iRoleService.saveOrUpdate(entity);
            return new ResultEntity<>(200, true, "操作成功");
        }
    }


    private ResultEntity<Boolean> edit(Role entity) {
        if (entity.getName() == null || entity.getName().equals("")) {
            return new ResultEntity<>(0, "角色名不能为空");
        }
        entity.setUpdateTime(new Date());
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getId, entity.getId());
        Role one = iRoleService.getOne(wrapper);
        if (one == null) {
            return new ResultEntity<>(-1, true, "无效的角色");
        }
        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getName, entity.getName());
        wrapper.ne(Role::getId, entity.getId());
        List<Role> data = iRoleService.list(wrapper);
        if (data != null && data.size() > 0) {
            return new ResultEntity<>(-1, true, "当前角色已经存在了");
        }
        one.setName(entity.getName());
        one.setUpdateTime(new Date());
        iRoleService.saveOrUpdate(one);
        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除角色")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        try {
            iRoleService.removeById(id);
            LambdaQueryWrapper<UserRoleMapping> userRoleMappingLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userRoleMappingLambdaQueryWrapper.eq(UserRoleMapping::getRoleId, id);
            iUserRoleMappingService.remove(userRoleMappingLambdaQueryWrapper);
            LambdaQueryWrapper<RoleMenuMapping> roleMenuMappingLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roleMenuMappingLambdaQueryWrapper.eq(RoleMenuMapping::getRoleId, id);
            iRoleMenuMappingService.remove(roleMenuMappingLambdaQueryWrapper);
        } catch (Exception e) {
            log.error("删除角色失败", e);
        }

        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/getRoleMenu")
    @ApiOperation(value = "获取角色菜单")
    public ResultEntityList<RoleMenuMappingVo> getRoleMenu(@RequestParam Integer roleId) {
        ResultEntityList<RoleMenuMappingVo> res = new ResultEntityList<>();
        try {
            QueryRoleMenuMappingVoRequest queryRoleMenuMappingVoRequest = new QueryRoleMenuMappingVoRequest();
            queryRoleMenuMappingVoRequest.setRoleId(roleId);
            List<RoleMenuMappingVo> roleMenuMappingVoList = iRoleMenuMappingService.queryVo(queryRoleMenuMappingVoRequest);
            res.setData(roleMenuMappingVoList);
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("获取角色菜单失败", e);
        }
        return res;
    }

    @PostMapping("/saveRoleMenu")
    @ApiOperation(value = "保存角色菜单")
    public ResultEntity<Boolean> saveRoleMenu(@RequestBody SaveRoleMenuRequest saveRoleMenuRequest) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            Role byId = iRoleService.getById(saveRoleMenuRequest.getRoleId());
            AssertUtil.notNull(byId, "无效的角色");
            LambdaQueryWrapper<RoleMenuMapping> roleMenuMappingLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roleMenuMappingLambdaQueryWrapper.eq(RoleMenuMapping::getRoleId, saveRoleMenuRequest.getRoleId());
            iRoleMenuMappingService.remove(roleMenuMappingLambdaQueryWrapper);
            List<RoleMenuMapping> saveList = new ArrayList<>();
            for (Integer menuId : saveRoleMenuRequest.getMenuIds()) {
                RoleMenuMapping item = new RoleMenuMapping();
                item.setMenuId(menuId);
                item.setRoleId(saveRoleMenuRequest.getRoleId());
                saveList.add(item);
            }
            if (!saveList.isEmpty()) {
                iRoleMenuMappingService.saveBatch(saveList);
            }
        } catch (RZException e) {
            res.setCode(500);
            res.setMessage(e.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("保存角色菜单", e);
        }
        return res;
    }


    @PostMapping("/getRoleUser")
    @ApiOperation(value = "获取角色用户")
    public ResultEntityList<UserRoleMappingVo> getRoleUser(@RequestParam Integer roleId) {
        ResultEntityList<UserRoleMappingVo> res = new ResultEntityList<>();
        try {
            QueryUserRoleMappingRequest queryRoleMenuMappingVoRequest = new QueryUserRoleMappingRequest();
            queryRoleMenuMappingVoRequest.setRoleId(roleId);
            List<UserRoleMappingVo> roleMenuMappingVoList = iUserRoleMappingService.queryVo(queryRoleMenuMappingVoRequest);
            res.setData(roleMenuMappingVoList);
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("获取角色菜单失败", e);
        }
        return res;
    }

    @PostMapping("/saveRoleUser")
    @ApiOperation(value = "保存角色用户")
    public ResultEntity<Boolean> saveRoleUser(@RequestBody SaveRoleUserRequest saveRoleMenuRequest) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            Role byId = iRoleService.getById(saveRoleMenuRequest.getRoleId());
            AssertUtil.notNull(byId, "无效的角色");
            LambdaQueryWrapper<UserRoleMapping> roleMenuMappingLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roleMenuMappingLambdaQueryWrapper.eq(UserRoleMapping::getRoleId, saveRoleMenuRequest.getRoleId());
            iUserRoleMappingService.remove(roleMenuMappingLambdaQueryWrapper);
            List<UserRoleMapping> saveList = new ArrayList<>();
            for (Integer userId : saveRoleMenuRequest.getUserIds()) {
                UserRoleMapping item = new UserRoleMapping();
                item.setUserId(userId);
                item.setRoleId(saveRoleMenuRequest.getRoleId());
                saveList.add(item);
            }
            if (!saveList.isEmpty()) {
                iUserRoleMappingService.saveBatch(saveList);
            }
        } catch (RZException e) {
            res.setCode(500);
            res.setMessage(e.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("保存角色菜单", e);
        }
        return res;
    }
}
