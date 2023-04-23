package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.SelectTopic;
import com.rz.manuscript.pojo.vo.OrderVo;
import com.rz.manuscript.pojo.vo.SelectTopicGetListRequest;
import com.rz.manuscript.pojo.vo.SelectTopicVo;
import com.rz.manuscript.service.ISelectTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/selectTopic")
@Slf4j
@Api(tags = "热文选题")
public class SelectTopicController {

    @Resource
    private ISelectTopicService iSelectTopicService;

    @PostMapping("/get")
    @ApiOperation(value = "获取所有热文选题-分页")
    public ResultEntityList<SelectTopicVo> getSelectTopic(@RequestBody SelectTopicGetListRequest request) {
        List<SelectTopicVo> resData = iSelectTopicService.getList(request);
        ResultEntityList<SelectTopicVo> res = new ResultEntityList<>(200, resData, "获取成功");
        return res;
    }

}
