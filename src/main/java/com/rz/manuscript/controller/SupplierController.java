package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rz.manuscript.common.CacheManager;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.Supplier;
import com.rz.manuscript.entity.User;
import com.rz.manuscript.pojo.entity.CacheEntity;
import com.rz.manuscript.pojo.vo.ModifyPwdRequestVO;
import com.rz.manuscript.service.ICompanyService;
import com.rz.manuscript.service.ISupplierService;
import com.rz.manuscript.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/supplier")
@Api(tags = "用户")
public class SupplierController {
    @Resource
    private HttpServletRequest request;
    @Resource
    private IUserService iUserService;

    @Resource
    private ICompanyService iCompanyService;

    @Resource
    private ISupplierService iSupplierService;

    @PostMapping("/modifyPwd")
    public ResultEntity<Boolean> modifyPwd(@RequestBody ModifyPwdRequestVO data) {

        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return new ResultEntity<>(0, "当前用户获取失败,请重新登录");
        }
        if (!data.getNewPassWord().equals(data.getNewPassWordConfirm())) {
            return new ResultEntity<>(0, "两次输入的密码不一致");
        }
        LambdaQueryWrapper<Supplier> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(Supplier::getName, ((User) cacheInfo.getValue()).getName());
        userLambdaQueryWrapper.eq(Supplier::getPassword, data.getPassWord());
        Supplier one = iSupplierService.getOne(userLambdaQueryWrapper);
        if (one == null || one.getId() < 1)
            return new ResultEntity<>(-1, "用户名密码错误");
        one.setPassword(data.getNewPassWord());
        iSupplierService.saveOrUpdate(one);
        return new ResultEntity<>();
    }

    @GetMapping("/getCurrentUser")
    public ResultEntity<String> getCurrentUser() {
        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return new ResultEntity<>(0, "当前用户获取失败,请重新登录");
        }
        return new ResultEntity<String>(200, ((User) cacheInfo.getValue()).getRealName(), "获取成功");
    }

    @GetMapping("/getCurrentUserInfo")
    public ResultEntity<User> getCurrentUserInfo() {
        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return new ResultEntity<>(0, "当前用户获取失败,请重新登录");
        }
        return new ResultEntity<User>(200, (User) cacheInfo.getValue(), "获取成功");
    }

    @GetMapping("/getList")
    public ResultEntityList<Supplier> getUser() {
//        String accessToken = request.getHeader("accessToken");
//        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
//        if (cacheInfo == null) {
//            return new ResultEntityList<>(0, "当前用户获取失败,请重新登录");
//        }
        List<Supplier> res = iSupplierService.list();
//        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        userLambdaQueryWrapper.ne(User::getName, "admin");
//        userLambdaQueryWrapper.ne(User::getCompanyId, 0);
//        userLambdaQueryWrapper.orderBy(true, true, User::getId);
//        List<User> userRes = iUserService.list(userLambdaQueryWrapper);
//        List<UserVo> res = new ArrayList<>();
//        for (User item:userRes
//             ) {
//            UserVo vo = new UserVo();
//
//
//        }
        return new ResultEntityList<Supplier>(200, res, "获取成功");
    }

    @GetMapping("/getSearchList")
    public ResultEntityList<Supplier> getSearchList(@RequestParam(required = false)  String q, @RequestParam Integer page) {
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        if(q!=null && q.trim().length()!=0)
        {
            wrapper.like(Supplier::getRealName, q);
        }
        Page<Supplier> s = new Page<>(page, 10);
        Page<Supplier> page1 = iSupplierService.page(s, wrapper);
        ResultEntityList<Supplier> res = new ResultEntityList<>(200, page1.getRecords(), "获取成功");
        res.setPageIndex(page);
        res.setPageSize(10);
        res.setTotal(page1.getTotal());
        return res;
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增用户")
    public ResultEntity<Boolean> save(@RequestBody Supplier entity) {
        if (entity.getName() == null || entity.getName().equals("")) {
            return new ResultEntity<>(0, "用户名不能为空");
        }
        entity.setUpdateTime(new Date());
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Supplier::getName, entity.getName());
        List<Supplier> data = iSupplierService.list(wrapper);
        if (data != null && data.size() > 0) {
            return new ResultEntity<>(-1, true, "当前用户已经存在了");
        }
        entity.setPassword("123456");
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
//        LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
//        wrapper1.eq(ProjectCategory::getProjectId, entity.getProjectId());
//        wrapper1.eq(ProjectCategory::getName, entity.getName());
//        ProjectCategory one = iProjectCategoryService.getOne(wrapper1);
//        if (one != null)
//            entity.setId(one.getId());
        iSupplierService.saveOrUpdate(entity);

        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/resetPws")
    @ApiOperation(value = "重置密码")
    public ResultEntity<Boolean> resetPws(@RequestParam Integer id) {
        Supplier byId = iSupplierService.getById(id);
        byId.setPassword("123456");
        byId.setUpdateTime(new Date());
        iSupplierService.saveOrUpdate(byId);
        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除用户")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        iSupplierService.removeById(id);
        return new ResultEntity<>(200, true, "操作成功");
    }

}
