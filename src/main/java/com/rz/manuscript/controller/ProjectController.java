package com.rz.manuscript.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.word.WordUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rz.manuscript.common.CacheManager;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.common.enums.PublishStateEnum;
import com.rz.manuscript.entity.*;
import com.rz.manuscript.pojo.entity.CacheEntity;
import com.rz.manuscript.pojo.vo.ProjectRequest;
import com.rz.manuscript.pojo.vo.ProjectVo;
import com.rz.manuscript.service.IManuscriptService;
import com.rz.manuscript.service.IProjectCategoryService;
import com.rz.manuscript.service.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/project")
@Api(tags = "项目")
@Slf4j
public class ProjectController {

    @Resource
    private IProjectService iProjectService;

    @Resource
    private IManuscriptService iManuscriptService;

    @Resource
    private HttpServletRequest request;
    @Resource
    private IProjectCategoryService iProjectCategoryService;


    @PostMapping("/getList")
    public ResultEntityList<ProjectVo> getList(@RequestBody ProjectRequest request) {
        List<ProjectVo> res = new ArrayList<>();
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        if (request.getName() != null && request.getName().trim() != "") {
            wrapper.like(Project::getName, request.getName().trim());
        }
        if (request.getBeginDate() != null) {
            wrapper.ge(Project::getBeginDate, request.getBeginDate());
        }
        if (request.getEndDate() != null) {
            wrapper.le(Project::getEndDate, request.getEndDate());
        }
        wrapper.orderBy(true, true, Project::getCreateTime);
        List<Project> list = iProjectService.list(wrapper);
        if (list != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (Project item : list) {
                ProjectVo newItem = new ProjectVo();
                newItem.setId(item.getId());
                newItem.setName(item.getName());
                newItem.setBeginDt(format.format(item.getBeginDate()));
                newItem.setEndDt(format.format(item.getEndDate()));
                newItem.setPlanNumber(item.getManuscriptUploadCount() + "/" + item.getManuscriptCount());
                newItem.setPublishNumber(item.getManuscriptPublishCount() + "/" + item.getManuscriptUploadCount());
                newItem.setOrderNumber(item.getOrderCompleteCount() + "/" + item.getOrderCount());
                res.add(newItem);
            }
        }
        return new ResultEntityList<>(200, res, "获取成功");
    }

    @PostMapping("/getPageList")
    public ResultEntityList<ProjectVo> getPageList(@RequestBody ProjectRequest request) {
        List<ProjectVo> res = new ArrayList<>();
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        if (request.getProjectId() != null && request.getProjectId() > 0) {
            wrapper.eq(Project::getId, request.getProjectId());
        }
        if (request.getBeginDate() != null) {
            wrapper.ge(Project::getBeginDate, request.getBeginDate());
        }
        if (request.getEndDate() != null) {
            wrapper.le(Project::getEndDate, request.getEndDate());
        }
        wrapper.orderBy(true, true, Project::getCreateTime);
        Page<Project> s = new Page<>(request.getPageIndex(), request.getPageSize());
        Page<Project> page1 = iProjectService.page(s, wrapper);
        List<Project> list = page1.getRecords();
        if (list != null) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            for (Project item : list) {
                ProjectVo newItem = new ProjectVo();
                newItem.setId(item.getId());
                newItem.setName(item.getName());
                newItem.setBeginDt(format.format(item.getBeginDate()));
                newItem.setEndDt(format.format(item.getEndDate()));
                newItem.setPlanNumber(item.getManuscriptUploadCount() + "/" + item.getManuscriptCount());
                newItem.setPublishNumber(item.getManuscriptPublishCount() + "/" + item.getManuscriptUploadCount());
                newItem.setOrderNumber(item.getOrderCompleteCount() + "/" + item.getOrderCount());
                res.add(newItem);
            }
        }

        ResultEntityList<ProjectVo> result = new ResultEntityList<>(200, res, "获取成功");
        result.setTotal(page1.getTotal());
        return result;
    }


    @GetMapping("/geUserProjectList")
    public ResultEntityList<Project> geUserProjectList(@RequestParam(required = false) String q, @RequestParam Integer page) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        if (q != null && q.trim().length() != 0) {
            wrapper.like(Project::getName, q);
        }
        Page<Project> s = new Page<>(page, 10L);
        Page<Project> page1 = iProjectService.page(s, wrapper);
        ResultEntityList<Project> res = new ResultEntityList<>(200, page1.getRecords(), "获取成功");
        res.setPageIndex(page);
        res.setPageSize(10);
        res.setTotal(page1.getTotal());
        return res;
    }

    @GetMapping("/geAllProjectList")
    public ResultEntityList<Project> geAllProjectList(@RequestParam(required = false) String q, @RequestParam Integer page) {
        LambdaQueryWrapper<Project> wrapper = new LambdaQueryWrapper<>();
        if (q != null && q.trim().length() != 0) {
            wrapper.like(Project::getName, q);
        }
        Page<Project> s = new Page<>(page, 10L);
        Page<Project> page1 = iProjectService.page(s, wrapper);
        ResultEntityList<Project> res = new ResultEntityList<>(200, page1.getRecords(), "获取成功");
        res.setPageIndex(page);
        res.setPageSize(10);
        res.setTotal(page1.getTotal());
        return res;
    }


    @PostMapping("/save")
    public ResultEntity<Boolean> save(@RequestBody Project entity) {
        entity.setModifyTime(new Date());
        if (entity.getId() == null || entity.getId() < 1) {
            entity.setCreateTime(new Date());
        }
        iProjectService.saveOrUpdate(entity);
        return new ResultEntity<>(200, true, "保存成功");
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除项目")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        iProjectService.removeById(id);
        return new ResultEntity<>(200, true, "操作成功");
    }


}
