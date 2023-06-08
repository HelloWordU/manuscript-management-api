package com.rz.manuscript.controller;

import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.pojo.request.GetContentKeysRequest;
import com.rz.manuscript.pojo.vo.WriteSelectedTopicGetListRequest;
import com.rz.manuscript.pojo.vo.WriteSelectedTopicVo;
import com.rz.manuscript.service.IWriteSelectedTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/writeSelectedTopic")
@Slf4j
@Api(tags = "撰写选题")
public class WriteSelectedTopicController {
    @Resource
    private IWriteSelectedTopicService iWriteSelectTopicService;

    @PostMapping("/getList")
    @ApiOperation(value = "获取范文列表-分页")
    public ResultEntityList<WriteSelectedTopicVo> getSelectTopic(@RequestBody WriteSelectedTopicGetListRequest request) {
        List<WriteSelectedTopicVo> resData = iWriteSelectTopicService.getList(request);
        ResultEntityList<WriteSelectedTopicVo> res = new ResultEntityList<>(200, resData, "获取成功");
        return res;
    }

    @PostMapping("/getById")
    @ApiOperation(value = "获取范文")
    public ResultEntity<WriteSelectedTopicVo> getSelectTopic(@RequestParam @ApiParam(value = "范文id",required = true) int id) {
        WriteSelectedTopicVo resData = iWriteSelectTopicService.getById(id);
        ResultEntity<WriteSelectedTopicVo> res = new ResultEntity<>(200, resData, "获取成功");
        return res;
    }

    @PostMapping("/getContentKeys")
    @ApiOperation(value = "获取文章内容分词")
    public ResultEntityList<String> getContentKeys(@RequestBody GetContentKeysRequest request) {
        List<String> resData = iWriteSelectTopicService.getContentKeys(request);
        ResultEntityList<String> res = new ResultEntityList<>(200, resData, "获取成功");
        return res;
    }

}
