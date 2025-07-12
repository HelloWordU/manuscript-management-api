package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.CacheManager;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.common.enums.UserTypeEnum;
import com.rz.manuscript.entity.ExamineSettle;
import com.rz.manuscript.entity.Menu;
import com.rz.manuscript.entity.Order;
import com.rz.manuscript.entity.User;
import com.rz.manuscript.pojo.entity.CacheEntity;
import com.rz.manuscript.pojo.vo.ExamineSettleListRequest;
import com.rz.manuscript.pojo.vo.ExamineSettleVo;
import com.rz.manuscript.pojo.vo.MenuVo;
import com.rz.manuscript.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
@Slf4j
@Api(tags = "菜单")
public class MenuController {

    @Resource
    private HttpServletRequest request;
    @Resource
    private IManuscriptService iManuscriptService;

    @Resource
    private IOrderService iOrderService;

    @Resource
    private IOrderManuscriptService iOrderManuscriptService;

    @Resource
    private IWebMessageService iWebMessageService;

    @Resource
    private IExamineSettleService iExamineSettleService;


    @Resource
    private IUserMenuMappingService iUserMenuMappingService;

    @Resource
    private IUserMenuDefaultService iUserMenuDefaultService;

    @Resource
    private IMenuService iMenuService;

    @Resource
    private IUserRoleMappingService iUserRoleMappingService;

    @Resource
    private IRoleMenuMappingService iRoleMenuMappingService;

    @PostMapping("/getUserList")
    @ApiOperation(value = "获取当前用户的菜单")
    public ResultEntityList<MenuVo> getUserList() {

        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return new ResultEntityList<>(0, "未获取请重新登录");
        }
        List<MenuVo> resData = new ArrayList<>();
        List<MenuVo> mappingRes = iUserMenuMappingService.getUserMenuList(((User) cacheInfo.getValue()).getId());
        if (mappingRes == null || mappingRes.size() < 1) {
            mappingRes = iUserMenuDefaultService.getUserMenuListWithRole(((User) cacheInfo.getValue()).getId());
            //获取当前用户绑定角色的菜单
          //  mappingRes = iUserMenuDefaultService.getUserMenuList(UserTypeEnum.员工.getCode());
        }
        if (mappingRes == null || mappingRes.size() < 1) {
            mappingRes = iUserMenuDefaultService.getUserMenuList(UserTypeEnum.员工.getCode());
        }
        Map<Integer, MenuVo> idCollect = mappingRes.stream().collect(Collectors.toMap(MenuVo::getId, item -> item));
        Map<Integer, List<MenuVo>> collect = mappingRes.stream().sorted(Comparator.comparing(i -> i.getSort())).collect(Collectors.groupingBy(MenuVo::getParentId));

        for (Integer key : collect.keySet()) {
            if (idCollect.containsKey(key)) {
                idCollect.get(key).setChild(collect.get(key));
            }
        }
        for (Integer key : mappingRes.stream().map(i -> i.getId()).collect(Collectors.toList())) {
            if (idCollect.get(key).getParentId() == 0) {
                resData.add(idCollect.get(key));
            }
        }

        ResultEntityList<MenuVo> res = new ResultEntityList<>(200, resData, "获取成功");
        //  res.setTotal(total);
        return res;
    }

    @PostMapping("/getAllList")
    @ApiOperation(value = "获取所有菜单")
    public ResultEntityList<MenuVo> getAllList() {

        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return new ResultEntityList<>(0, "未获取请重新登录");
        }
        List<MenuVo> resData = new ArrayList<>();
        List<Menu> list = iMenuService.list();
        List<MenuVo> mappingRes = new ArrayList<>();
        for (Menu m : list) {
            MenuVo item = new MenuVo();
            BeanUtils.copyProperties(m, item);
            mappingRes.add(item);

        }
        Map<Integer, MenuVo> idCollect = mappingRes.stream().collect(Collectors.toMap(MenuVo::getId, item -> item));
        Map<Integer, List<MenuVo>> collect = mappingRes.stream().sorted(Comparator.comparing(i -> i.getSort())).collect(Collectors.groupingBy(MenuVo::getParentId));

        for (Integer key : collect.keySet()) {
            if (idCollect.containsKey(key)) {
                idCollect.get(key).setChild(collect.get(key));
            }
        }
        for (Integer key : mappingRes.stream().map(i -> i.getId()).collect(Collectors.toList())) {
            if (idCollect.get(key).getParentId() == 0) {
                resData.add(idCollect.get(key));
            }
        }
         resData.sort(Comparator.comparing(MenuVo::getSort));
        ResultEntityList<MenuVo> res = new ResultEntityList<>(200, resData, "获取成功");
        //  res.setTotal(total);
        return res;
    }


}
