package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.ProjectCategory;
import com.rz.manuscript.entity.ProjectProduct;
import com.rz.manuscript.service.IProjectProductService;
import com.rz.manuscript.service.IProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/projectProduct")
@Api(value = "项目关键词")
public class ProjectProductController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private IProjectProductService iProjectProductService;

    @Resource
    private IProjectService iProjectService;

    @GetMapping("/get")
    @ApiOperation(value = "获取项目下面所有的关键词")
    public ResultEntityList<ProjectProduct> getProjectCategory(@RequestParam int projectId) {
        LambdaQueryWrapper<ProjectProduct> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProjectProduct::getProjectId, projectId);
        List<ProjectProduct> data = iProjectProductService.list(wrapper);
        return new ResultEntityList<>(200, data, "获取成功");
    }


    @GetMapping("/getById")
    @ApiOperation(value = "获取单个新项目关键词")
    public ResultEntity<ProjectProduct> getCategory(@RequestParam int categoryId) {
        ProjectProduct data = iProjectProductService.getById(categoryId);
        return new ResultEntity<>(200, data, "获取成功");
    }


//    @GetMapping("/admin/get")
//    @ApiOperation(value = "获取当前用户的下的所有关键词")
//    public ResultEntityList<ProjectCategory> getProjectCategoryAdmin() {
//        String accessToken = request.getHeader("accessToken");
//        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
//        if (cacheInfo == null) {
//            return new ResultEntityList<>(0, "当前用户获取大屏配置失败,请重新登录");
//        }
//        Integer companyId = ((User) cacheInfo.getValue()).getCompanyId();
//        LambdaQueryWrapper<Project> wrapper1 = new LambdaQueryWrapper<>();
//        wrapper1.eq(Project::getCompanyId, companyId);
//        Project one = iProjectService.getOne(wrapper1);
//        if (one == null) {
//            return new ResultEntityList<>(0, "当前用户没有项目，请新建项目");
//        }
//        LambdaQueryWrapper<ProjectCategory> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(ProjectCategory::getProjectId, one.getId());
//        List<ProjectCategory> data = iProjectCategoryService.list(wrapper);
//        return new ResultEntityList<>(200, data, "获取成功");
//    }


    @PostMapping("/save")
    @ApiOperation(value = "保存项目关键词")
    public ResultEntity<Boolean> save(@RequestBody ProjectProduct entity) {
        if (entity.getName() == null || entity.getName().equals("")) {
            return new ResultEntity<>(0, "项目关键词不能为空");
        }
        if (entity.getProjectId() == 0) {
            return new ResultEntity<>(0, "没有可用的项目");
        }
      //  entity.setUpdateTime(new Date());
//        LambdaQueryWrapper<ProjectCategory> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(ProjectCategory::getProjectId, entity.getProjectId());
//        List<ProjectCategory> data = iProjectCategoryService.list(wrapper);
//        if (data != null && (entity.getId() == null || entity.getId() < 0) && data.size() > 9) {
//            return new ResultEntity<>(-1, true, "当前项目配置的关键词已经达到最大");
//        }
        LambdaQueryWrapper<ProjectProduct> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(ProjectProduct::getProjectId, entity.getProjectId());
        wrapper1.eq(ProjectProduct::getName, entity.getName());
        ProjectProduct one = iProjectProductService.getOne(wrapper1);
        if (one != null)
            entity.setId(one.getId());
        iProjectProductService.saveOrUpdate(entity);

        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除项目关键词")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        iProjectProductService.removeById(id);
        return new ResultEntity<>(200, true, "操作成功");
    }


}
