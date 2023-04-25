package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.CacheManager;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.common.enums.PublishStateEnum;
import com.rz.manuscript.entity.*;
import com.rz.manuscript.pojo.entity.CacheEntity;
import com.rz.manuscript.pojo.vo.*;
import com.rz.manuscript.service.*;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
@Api(tags  = "订单")
public class OrderController {

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

    @PostMapping("/getList")
    public ResultEntityList<OrderVo> getList(@RequestBody OrderGetListRequest request) {

        Long total = iOrderService.getListTotal(request);
        List<OrderVo> resData = new ArrayList<>();
        if (total > 0) {
            resData = iOrderService.getList(request);

        }
        ResultEntityList<OrderVo> res = new ResultEntityList<>(200, resData, "获取成功");
        res.setTotal(total);
        return res;
    }

    @PostMapping("/getSupplierList")
    public ResultEntityList<OrderVo> getSupplierList(@RequestBody OrderGetListRequest requestParam) {
        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return new ResultEntityList<>(0, "未获取请重新登录");
        }
        requestParam.setUserId(((User) cacheInfo.getValue()).getId());
        Long total = iOrderService.getSupplierListTotal(requestParam);
        List<OrderVo> resData = new ArrayList<>();
        if (total > 0) {
            resData = iOrderService.getSupplierList(requestParam);

        }
        ResultEntityList<OrderVo> res = new ResultEntityList<>(200, resData, "获取成功");
        res.setTotal(total);
        return res;
    }
    @PostMapping("/save")
    public ResultEntity<Boolean> save(@RequestBody OrderSaveRequest data) {
        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return new ResultEntity<>(0, "未获取请重新登录");
        }
        if (data.getSupplierId() == null) {
            return new ResultEntity<>(0, "无效的供应商");
        }
        if (data.getManuscriptIdList() == null || data.getManuscriptIdList().size() < 1) {
            return new ResultEntity<>(0, "请选择稿件");
        }
        try {
            Order orderEntity = new Order();
            orderEntity.setCreateTime(new Date());
            orderEntity.setCreateUser(((User) cacheInfo.getValue()).getId());
            orderEntity.setManuscriptCount(data.getManuscriptIdList().size());
            orderEntity.setSupplierId(data.getSupplierId());
            orderEntity.setProjectId(data.getProjectId());
            iOrderService.save(orderEntity);


            List<OrderManuscript> entityList = new ArrayList<>();
            for (Integer manuscriptId : data.getManuscriptIdList()) {
                OrderManuscript item = new OrderManuscript();
                item.setManuscriptId(manuscriptId);
                item.setOrderId(orderEntity.getId());
                entityList.add(item);
            }
            iOrderManuscriptService.saveBatch(entityList);
            List<Manuscript> manuscripts = iManuscriptService.listByIds(data.getManuscriptIdList());
            for (Manuscript m : manuscripts) {
                m.setPublishState(PublishStateEnum.提交订单.getCode());
            }
            iManuscriptService.updateBatchById(manuscripts);
            return new ResultEntity(200, true, "保存成功");
        } catch (Exception e) {
            log.error("Order 保存失败", e);
            return new ResultEntity(0, false, "保存失败");
        }

    }

    @PostMapping("/delete")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        try {
            iOrderService.removeById(id);
            return new ResultEntity(200, true, "删除成功");
        } catch (Exception e) {
            log.error("CategoryHotWord 删除失败", e);
            return new ResultEntity(0, false, "删除失败");
        }

    }

    @PostMapping("/getOrderManuscriptList")
    public ResultEntityList<OrderManuscriptVo> getOrderManuscriptList(@RequestParam Integer orderId) {
        try {
            List<OrderManuscriptVo> res = iOrderManuscriptService.getOrderManuscriptList(orderId);
            return new ResultEntityList(200, res, "操作成功");
        } catch (Exception e) {
            log.error("getOrderManuscriptList失败", e);
            return new ResultEntityList(0, "操作失败");
        }

    }

    @PostMapping("/sendOrderNotify")
    public ResultEntity<Boolean> sendOrderNotify(@RequestParam Integer id) {
        try {
            String accessToken = request.getHeader("accessToken");
            CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
            if (cacheInfo == null) {
                return new ResultEntity<>(0, "未获取请重新登录");
            }
            Order byId = iOrderService.getById(id);
            WebMessage message = new WebMessage();
            message.setContent("您有一个新订单，请及时接单");
            message.setTitle("您有一个新订单，请及时接单");
            message.setCreateTime(new Date());
            message.setCreateUserId(((User) cacheInfo.getValue()).getId());
            message.setNotifyUserId(byId.getSupplierId());
            message.setType(1);
            iWebMessageService.save(message);
            return new ResultEntity(200, true, "操作成功");
        } catch (Exception e) {
            log.error("CategoryHotWord 删除失败", e);
            return new ResultEntity(0, false, "操作成功");
        }

    }

    @PostMapping("/confirmComplete")
    public ResultEntity<Boolean> confirmComplete(@RequestParam Integer id) {
        try {
            String accessToken = request.getHeader("accessToken");
            CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
            if (cacheInfo == null) {
                return new ResultEntity<>(0, "未获取请重新登录");
            }
            Order byId = iOrderService.getById(id);
            byId.setState(1);
            byId.setUpdateTime(new Date());
            byId.setIsComplete(true);
            iOrderService.updateById(byId);
            return new ResultEntity(200, true, "操作成功");
        } catch (Exception e) {
            log.error("CategoryHotWord 删除失败", e);
            return new ResultEntity(0, false, "操作成功");
        }

    }

    @PostMapping("/confirmOrder")
    public ResultEntity<Boolean> confirmOrder(@RequestParam Integer id) {
        try {
            String accessToken = request.getHeader("accessToken");
            CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
            if (cacheInfo == null) {
                return new ResultEntity<>(0, "未获取请重新登录");
            }
            Order byId = iOrderService.getById(id);
            byId.setIsBeginOrder(true);
            byId.setUpdateTime(new Date());
            iOrderService.updateById(byId);
            return new ResultEntity(200, true, "操作成功");
        } catch (Exception e) {
            log.error("CategoryHotWord 删除失败", e);
            return new ResultEntity(0, false, "操作成功");
        }

    }

}
