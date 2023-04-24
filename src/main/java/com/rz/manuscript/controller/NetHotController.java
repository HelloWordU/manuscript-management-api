package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.NetHot;
import com.rz.manuscript.service.INetHotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/netHot")
@Slf4j
@Api(tags = "网络热搜")
public class NetHotController {
    @Resource
    private INetHotService iNetHotService;

    @GetMapping("/getAll")
    @ApiOperation(value = "获取所有网络热搜关键词")
    public ResultEntityList<NetHot> getAllNetHot() {
        LambdaQueryWrapper<NetHot> wrapper = new LambdaQueryWrapper<>();
        List<NetHot> data = iNetHotService.list(wrapper);
        return new ResultEntityList<>(200, data, "获取成功");
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存热搜关键词")
    public ResultEntity<Boolean> save(@RequestBody NetHot entity) {
        if (entity.getHotWord() == null || entity.getHotWord().equals("")) {
            return new ResultEntity<>(0, "热搜关键词不能为空");
        }

        LambdaQueryWrapper<NetHot> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NetHot::getHotWord, entity.getHotWord());
        NetHot one = iNetHotService.getOne(wrapper);
        if (one != null)
            entity.setId(one.getId());
        iNetHotService.saveOrUpdate(entity);

        return new ResultEntity<>(200, true, "操作成功");
    }
}
