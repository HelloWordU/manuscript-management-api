package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.LoginUserUtils;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.SelectTopic;
import com.rz.manuscript.entity.User;
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
    private HttpServletRequest request;
    @Resource
    private ISelectTopicService iSelectTopicService;

    @PostMapping("/getList")
    @ApiOperation(value = "获取所有热文选题-分页")
    public ResultEntityList<SelectTopicVo> getSelectTopic(@RequestBody SelectTopicGetListRequest selectTopicGetListRequest) {
        User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
        if (currentLoginUser == null)
            return new ResultEntityList<>(500, null, "无效的登录用户");
        if (!currentLoginUser.getName().equals("admin")) {
            selectTopicGetListRequest.setUserId(currentLoginUser.getId());
        }
        List<SelectTopicVo> resData = iSelectTopicService.getList(selectTopicGetListRequest);
        ResultEntityList<SelectTopicVo> res = new ResultEntityList<>(200, resData, "获取成功");
        return res;
    }

    @PostMapping("/addToWrite")
    public ResultEntity<Boolean> addToWrite(@RequestParam Integer id,@RequestParam Integer projectId) {
        User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
        if (currentLoginUser == null)
            return new ResultEntity<>(500, null, "无效的登录用户");
        ResultEntity<Boolean> res = iSelectTopicService.addToWrite(id,projectId,currentLoginUser.getId());
        return res;
    }

    @PostMapping("/upload")
    public ResultEntity<Boolean> uploadFile(HttpServletRequest request, @RequestParam MultipartFile uploadFile) {
        ResultEntity res = new ResultEntity<>(true);
        User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
        if (currentLoginUser == null)
            return new ResultEntity<>(500, null, "无效的登录用户");
        try {
            if (uploadFile == null)
                throw new RZException("无效的上传文件");
            iSelectTopicService.uploadFile(uploadFile,currentLoginUser.getId());
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
