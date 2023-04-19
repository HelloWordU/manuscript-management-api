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
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
@Slf4j
@Api(value = "结算")
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

    @PostMapping("/getUserList")
    public ResultEntityList<MenuVo> getUserList() {

        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return new ResultEntityList<>(0, "未获取请重新登录");
        }
        List<MenuVo> resData = new ArrayList<>();
        List<MenuVo> mappingRes = iUserMenuMappingService.getUserMenuList(((User) cacheInfo.getValue()).getId());
        if (mappingRes == null || mappingRes.size() < 1) {
            mappingRes = iUserMenuDefaultService.getUserMenuList(UserTypeEnum.员工.getCode());
        }
        Map<Integer, MenuVo> idCollect = mappingRes.stream().collect(Collectors.toMap(MenuVo::getId, item -> item));
        Map<Integer, List<MenuVo>> collect = mappingRes.stream().collect(Collectors.groupingBy(MenuVo::getParentId));

        for (Integer key : collect.keySet()) {
            if (idCollect.containsKey(key)) {
                idCollect.get(key).setChild(collect.get(key));
            }
        }
        for (Integer key : idCollect.keySet()) {
            if (idCollect.get(key).getParentId() == 0) {
                resData.add(idCollect.get(key));
            }
        }

        ResultEntityList<MenuVo> res = new ResultEntityList<>(200, resData, "获取成功");
      //  res.setTotal(total);
        return res;
    }

    @PostMapping("/save")
    public ResultEntity<Boolean> save(@RequestBody ExamineSettle data) {
        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return new ResultEntity<>(0, "未获取请重新登录");
        }
//        if (data.getProjectId() == null || data.getProjectId()<1) {
//            return new ResultEntity<>(0, "无效的项目");
//        }
        if (data.getOrderId() == null || data.getOrderId() < 1) {
            return new ResultEntity<>(0, "无效的订单");
        }
        Order byId = iOrderService.getById(data.getOrderId());
        if (byId.getSupplierId() != ((User) cacheInfo.getValue()).getId()) {
            return new ResultEntity<>(0, "只能对自己的订单发起结算");
        }
        LambdaQueryWrapper<ExamineSettle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamineSettle::getOrderId, data.getOrderId());
        ExamineSettle entity = iExamineSettleService.getOne(wrapper);
        if (entity != null && entity.getId() > 0) {
            return new ResultEntity<>(0, "一个订单只能发起一个结算");
        }
        try {
            data.setProjectId(byId.getProjectId());
            data.setCreateUserId(((User) cacheInfo.getValue()).getId());
            data.setIsChecked(false);
            data.setCreateTime(new Date());
            iExamineSettleService.save(data);
            return new ResultEntity(200, true, "保存成功");
        } catch (Exception e) {
            log.error("Order 保存失败", e);
            return new ResultEntity(0, false, "保存失败");
        }

    }

    @PostMapping("/delete")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        try {
            iExamineSettleService.removeById(id);
            return new ResultEntity(200, true, "删除成功");
        } catch (Exception e) {
            log.error("CategoryHotWord 删除失败", e);
            return new ResultEntity(0, false, "删除失败");
        }

    }

    @PostMapping("/submitCheck")
    public ResultEntity<Boolean> submitCheck(@RequestParam Integer id) {
        try {
            String accessToken = request.getHeader("accessToken");
            CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
            if (cacheInfo == null) {
                return new ResultEntity<>(0, "未获取请重新登录");
            }
            ExamineSettle byId = iExamineSettleService.getById(id);
            byId.setCheckTime(new Date());
            byId.setCheckUserId(((User) cacheInfo.getValue()).getId());
            iExamineSettleService.updateById(byId);
            return new ResultEntity(200, true, "操作成功");
        } catch (Exception e) {
            log.error("CategoryHotWord 删除失败", e);
            return new ResultEntity(0, false, "操作成功");
        }

    }


}
