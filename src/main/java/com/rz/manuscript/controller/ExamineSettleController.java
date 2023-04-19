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
@RequestMapping("/examineSettle")
@Slf4j
@Api(value = "结算")
public class ExamineSettleController {

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
    @PostMapping("/getList")
    public ResultEntityList<ExamineSettleVo> getList(@RequestBody ExamineSettleListRequest request) {

        Long total = iExamineSettleService.getListTotal(request);
        List<ExamineSettleVo> resData = new ArrayList<>();
        if (total > 0) {
            resData = iExamineSettleService.getList(request);

        }
        ResultEntityList<ExamineSettleVo> res = new ResultEntityList<>(200, resData, "获取成功");
        res.setTotal(total);
        return res;
    }

    @PostMapping("/getSupplierList")
    public ResultEntityList<ExamineSettleVo> getSupplierList(@RequestBody ExamineSettleListRequest requestParam) {
        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return new ResultEntityList<>(0, "未获取请重新登录");
        }
        requestParam.setSupplierId(((User) cacheInfo.getValue()).getId());
        Long total = iExamineSettleService.getSupplierListTotal(requestParam);
        List<ExamineSettleVo> resData = new ArrayList<>();
        if (total > 0) {
            resData = iExamineSettleService.getSupplierList(requestParam);

        }
        ResultEntityList<ExamineSettleVo> res = new ResultEntityList<>(200, resData, "获取成功");
        res.setTotal(total);
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
        if (data.getOrderId() == null || data.getOrderId()< 1) {
            return new ResultEntity<>(0, "无效的订单");
        }
        Order byId = iOrderService.getById(data.getOrderId());
        if(byId.getSupplierId()!=((User) cacheInfo.getValue()).getId())
        {
            return new ResultEntity<>(0, "只能对自己的订单发起结算");
        }
        LambdaQueryWrapper<ExamineSettle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExamineSettle::getOrderId,data.getOrderId());
        ExamineSettle entity = iExamineSettleService.getOne(wrapper);
        if(entity!=null && entity.getId()>0)
        {
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
            byId.setIsChecked(true);
            byId.setCheckUserId(((User) cacheInfo.getValue()).getId());
            iExamineSettleService.updateById(byId);
            return new ResultEntity(200, true, "操作成功");
        } catch (Exception e) {
            log.error("CategoryHotWord 删除失败", e);
            return new ResultEntity(0, false, "操作成功");
        }

    }


}
