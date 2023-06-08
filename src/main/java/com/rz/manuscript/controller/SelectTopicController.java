package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.SelectTopic;
import com.rz.manuscript.exception.RZException;
import com.rz.manuscript.pojo.vo.OrderVo;
import com.rz.manuscript.pojo.vo.SelectTopicGetListRequest;
import com.rz.manuscript.pojo.vo.SelectTopicVo;
import com.rz.manuscript.service.ISelectTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/selectTopic")
@Slf4j
@Api(tags = "热文选题")
public class SelectTopicController {

    @Resource
    private ISelectTopicService iSelectTopicService;

    @PostMapping("/getList")
    @ApiOperation(value = "获取所有热文选题-分页")
    public ResultEntityList<SelectTopicVo> getSelectTopic(@RequestBody SelectTopicGetListRequest request) {
        List<SelectTopicVo> resData = iSelectTopicService.getList(request);
        ResultEntityList<SelectTopicVo> res = new ResultEntityList<>(200, resData, "获取成功");
        return res;
    }

    @PostMapping("/addToWrite")
    public ResultEntity<Boolean> addToWrite(@RequestParam Integer id) {
        ResultEntity<Boolean> res = iSelectTopicService.addToWrite(id);
        return res;
    }

    @PostMapping("/upload")
    public ResultEntity<Boolean> uploadFile(HttpServletRequest request, @RequestParam MultipartFile uploadFile) {
        ResultEntity res = new ResultEntity<>(true);
        try {
            if (uploadFile == null)
                throw new RZException("无效的上传文件");
            iSelectTopicService.uploadFile(uploadFile);
        } catch (RZException ex) {
            res.setCode(500);
            res.setMessage(ex.getMessage());

        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("uploadFile error", e);
        }
        return res;

    }

}
