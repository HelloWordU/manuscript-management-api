package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.client.NetHotRefreshClient;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.NetHot;
import com.rz.manuscript.service.INetHotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/netHot")
@Slf4j
@Api(tags = "网络热搜")
public class NetHotController {
    @Resource
    private INetHotService iNetHotService;

    @Autowired
    private NetHotRefreshClient netHotRefreshClient;


    @GetMapping("/getByType")
    @ApiOperation(value = "获取所有网络热搜关键词")
    public ResultEntityList<NetHot> getNetHotByType(@RequestParam @ApiParam(value = "类型", required = true) Integer type) {
        LambdaQueryWrapper<NetHot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NetHot::getType, type);
        wrapper.isNotNull(NetHot::getSortIndex);
        List<NetHot> data = iNetHotService.list(wrapper);
        return new ResultEntityList<>(200, data, "获取成功");
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存热搜关键词")
    public ResultEntity<Boolean> save(@RequestBody List<NetHot> netHotList) {
        if (netHotList == null || netHotList.size() == 0) {
            return new ResultEntity<>(0, "list不能为空");
        }
        Map<Integer, List<NetHot>> groupNetHotList = netHotList.stream().collect(Collectors.groupingBy(NetHot::getType));
        for (Map.Entry<Integer, List<NetHot>> entryList : groupNetHotList.entrySet()) {
            LambdaQueryWrapper<NetHot> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(NetHot::getType, entryList.getKey());
            iNetHotService.remove(wrapper);

            iNetHotService.saveOrUpdateBatch(entryList.getValue());
        }

        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/refresh")
    @ApiOperation(value = "刷新热搜关键词")
    public ResultEntity<Boolean> refresh() {

        netHotRefreshClient.weiboHot();
        netHotRefreshClient.baiduHot();
        netHotRefreshClient.toutiaoHot();
        return new ResultEntity<>(200, true, "操作成功");
    }
}
