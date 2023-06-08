package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.LoginUserUtils;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.NetHot;
import com.rz.manuscript.entity.Picture;
import com.rz.manuscript.entity.User;
import com.rz.manuscript.pojo.vo.PictureVo;
import com.rz.manuscript.service.IPictureService;
import com.rz.manuscript.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 图片 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2023-05-25
 */
@Controller
@RequestMapping("/picture")
@Slf4j
@Api(tags = "图片")
public class PictureController {

    @Resource
    private IPictureService iPictureService;
    @Resource
    private HttpServletRequest request;

    @Resource
    private IUserService iUserService;

    @GetMapping("/getProjectPicture")
    @ApiOperation(value = "获取项目图片")
    public ResultEntityList<Picture> getProjectPicture(@RequestParam @ApiParam(value = "类型", required = true) Long projectId) {
      //获取当前用户项目的图片
        User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
        if (currentLoginUser == null)
            return new ResultEntityList<>(500, null, "无效的登录用户");
        List<Long> userProjectList = iUserService.getUserProjectList(currentLoginUser.getId());
        LambdaQueryWrapper<Picture> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Picture::getProjectId, userProjectList);
        List<Picture> data = iPictureService.list(wrapper);
        return new ResultEntityList<>(200, data, "获取成功");
    }
}
