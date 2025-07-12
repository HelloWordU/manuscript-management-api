package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rz.manuscript.common.AssertUtil;
import com.rz.manuscript.common.LoginUserUtils;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.NetHot;
import com.rz.manuscript.entity.Picture;
import com.rz.manuscript.entity.PictureProject;
import com.rz.manuscript.entity.User;
import com.rz.manuscript.exception.RZException;
import com.rz.manuscript.pojo.request.BatchAddPictureRequest;
import com.rz.manuscript.pojo.request.QueryPictureRequest;
import com.rz.manuscript.pojo.vo.PictureVo;
import com.rz.manuscript.service.IPictureService;
import com.rz.manuscript.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

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
@RestController
@RequestMapping("/picture")
@Slf4j
@Api(tags = "网络图片")
public class PictureController {

    @Resource
    private IPictureService iPictureService;
    @Resource
    private HttpServletRequest request;

    @Resource
    private IUserService iUserService;

    @GetMapping("/getNetPicture")
    @ApiOperation(value = "获取网络图片")
    public ResultEntityList<Picture> getNetPicture(@RequestParam @ApiParam(value = "项目id", required = true) Integer projectId, @RequestParam Integer page) {
        //获取当前用户项目的图片
        User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
        if (currentLoginUser == null)
            return new ResultEntityList<>(500, null, "无效的登录用户");
//        List<Long> userProjectList = iUserService.getUserProjectList(currentLoginUser.getId());
        LambdaQueryWrapper<Picture> wrapper = new LambdaQueryWrapper<>();
        if (projectId != null) {
            wrapper.eq(Picture::getProjectId, projectId);
        }
        Page<Picture> s = new Page<>(page, 10L);
        Page<Picture> page1 = iPictureService.page(s, wrapper);
        ResultEntityList<Picture> res = new ResultEntityList<>(200, page1.getRecords(), "获取成功");
        res.setPageIndex(page);
        res.setPageSize(10);
        res.setTotal(page1.getTotal());
        return res;
    }

    @PostMapping("/query")
    @ApiOperation(value = "获取网络图片")
    public ResultEntityList<Picture> query(@RequestBody QueryPictureRequest queryPictureRequest) {
        //获取当前用户项目的图片
        User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
        if (currentLoginUser == null)
            return new ResultEntityList<>(500, null, "无效的登录用户");
//        List<Long> userProjectList = iUserService.getUserProjectList(currentLoginUser.getId());
        Long projectId = queryPictureRequest.getProjectId();
        LambdaQueryWrapper<Picture> wrapper = new LambdaQueryWrapper<>();
        if (projectId != null) {
            wrapper.eq(Picture::getProjectId, projectId);
        }
        Page<Picture> s = new Page<>(queryPictureRequest.getPageIndex(), queryPictureRequest.getPageSize());
        Page<Picture> page1 = iPictureService.page(s, wrapper);
        ResultEntityList<Picture> res = new ResultEntityList<>(200, page1.getRecords(), "获取成功");
        res.setPageIndex(queryPictureRequest.getPageIndex());
        res.setPageSize(queryPictureRequest.getPageSize());
        res.setTotal(page1.getTotal());
        return res;
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "保存网络图片")
    public ResultEntity<Boolean> batchAdd(@RequestBody BatchAddPictureRequest batchAddPictureRequest) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            AssertUtil.notNull(batchAddPictureRequest, "无效的请求");
            res = iPictureService.batchAdd(batchAddPictureRequest);
        } catch (RZException e) {
            res.setCode(500);
            res.setMessage(e.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("保存网络图片异常", e);
        }
        return res;
    }

    @PostMapping("/delete")
    @ApiOperation(value = "保存网络图片")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        try {
            AssertUtil.notNull(id, "无效的数据");
            iPictureService.removeById(id);
        } catch (RZException e) {
            res.setCode(500);
            res.setMessage(e.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("保存网络图片异常", e);
        }
        return res;
    }
}
