package com.rz.manuscript.controller;

import cn.hutool.core.annotation.AnnotationUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rz.manuscript.common.CacheManager;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.common.enums.UserTypeEnum;
import com.rz.manuscript.entity.Customer;
import com.rz.manuscript.entity.Supplier;
import com.rz.manuscript.entity.User;
import com.rz.manuscript.pojo.entity.CacheEntity;
import com.rz.manuscript.pojo.vo.ModifyPwdRequestVO;
import com.rz.manuscript.pojo.vo.UserVo;
import com.rz.manuscript.service.ICompanyService;
import com.rz.manuscript.service.ICustomerService;
import com.rz.manuscript.service.ISupplierService;
import com.rz.manuscript.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
@Api(tags = "用户")
@Slf4j
public class UserController {
    @Resource
    private HttpServletRequest request;
    @Resource
    private IUserService iUserService;

    @Resource
    private ISupplierService iSupplierService;

    @Resource
    private ICustomerService iCustomerService;

    @Resource
    private ICompanyService iCompanyService;

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
        User current = (User) cacheInfo.getValue();
        try {

            switch (current.getUserType()) {
                case 1:
                    LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    userLambdaQueryWrapper.eq(User::getName, ((User) cacheInfo.getValue()).getName());
                    userLambdaQueryWrapper.eq(User::getPassword, data.getPassWord());
                    User one = iUserService.getOne(userLambdaQueryWrapper);
                    if (one == null || one.getId() < 1)
                        return new ResultEntity<>(-1, "用户名密码错误");
                    one.setPassword(data.getNewPassWord());
                    iUserService.saveOrUpdate(one);
                    break;
                case 2:
                    LambdaQueryWrapper<Supplier> supplierLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    supplierLambdaQueryWrapper.eq(Supplier::getName, ((User) cacheInfo.getValue()).getName());
                    supplierLambdaQueryWrapper.eq(Supplier::getPassword, data.getPassWord());
                    Supplier oneSupplier = iSupplierService.getOne(supplierLambdaQueryWrapper);
                    if (oneSupplier == null || oneSupplier.getId() < 1)
                        return new ResultEntity<>(-1, "用户名密码错误");
                    oneSupplier.setPassword(data.getNewPassWord());
                    iSupplierService.saveOrUpdate(oneSupplier);
                    break;
                case 3:
                    LambdaQueryWrapper<Customer> customerLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    customerLambdaQueryWrapper.eq(Customer::getName, ((User) cacheInfo.getValue()).getName());
                    customerLambdaQueryWrapper.eq(Customer::getPassword, data.getPassWord());
                    Customer oneCustomer = iCustomerService.getOne(customerLambdaQueryWrapper);
                    if (oneCustomer == null || oneCustomer.getId() < 1)
                        return new ResultEntity<>(-1, "用户名密码错误");
                    oneCustomer.setPassword(data.getNewPassWord());
                    iCustomerService.saveOrUpdate(oneCustomer);
                    break;
            }
        } catch (Exception e) {
            log.error("修改密码异常", e);

            return new ResultEntity<>(0, false, "操作失败");
        }


        return new ResultEntity<>(200, true, "操作成功");
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

    @GetMapping("/get")
    public ResultEntityList<UserVo> getUser() {
//        String accessToken = request.getHeader("accessToken");
//        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
//        if (cacheInfo == null) {
//            return new ResultEntityList<>(0, "当前用户获取失败,请重新登录");
//        }
        List<UserVo> res = iUserService.getAllUser();
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
        return new ResultEntityList<UserVo>(200, res, "获取成功");
    }


    @GetMapping("/getPageList")
    public ResultEntityList<User> getPageList(@RequestParam(required = false) String q, @RequestParam Integer page) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (q != null && q.trim().length() != 0) {
            wrapper.like(User::getName, q);
        }
        Page<User> s = new Page<>(page, 10L);
        Page<User> page1 = iUserService.page(s, wrapper);
        ResultEntityList<User> res = new ResultEntityList<>(200, page1.getRecords(), "获取成功");
        res.setPageIndex(page);
        res.setPageSize(10);
        res.setTotal(page1.getTotal());
        return res;
    }

    @GetMapping("/getAllUserList")
    public ResultEntityList<User> getAllUserList(@RequestParam(required = false) String q, @RequestParam Integer page) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (q != null && q.trim().length() != 0) {
//            LambdaQueryWrapper<User>      wrappe2 = new LambdaQueryWrapper<>();
//            wrappe2.like(User::getRealName, q);
            wrapper.like(User::getName, q);
            wrapper.or().like(User::getRealName, q);
        }
        Page<User> s = new Page<>(page, 10L);
        Page<User> page1 = iUserService.page(s, wrapper);
        ResultEntityList<User> res = new ResultEntityList<>(200, page1.getRecords(), "获取成功");
        res.setPageIndex(page);
        res.setPageSize(10);
        res.setTotal(page1.getTotal());
        return res;
    }


    @PostMapping("/add")
    @ApiOperation(value = "新增用户")
    public ResultEntity<Boolean> save(@RequestBody User entity) {
        if (entity.getName() == null || entity.getName().equals("")) {
            return new ResultEntity<>(0, "用户名不能为空");
        }
        if (entity.getId() != null && entity.getId() > 0) {
            return edit(entity);
        } else {
            entity.setUpdateTime(new Date());
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getName, entity.getName());
            List<User> data = iUserService.list(wrapper);
            if (data != null && data.size() > 0) {
                return new ResultEntity<>(-1, true, "当前用户已经存在了");
            }
            entity.setPassword("123456");
            entity.setCreateTime(new Date());
            entity.setUpdateTime(new Date());
            iUserService.saveOrUpdate(entity);
            return new ResultEntity<>(200, true, "操作成功");
        }
    }

    private ResultEntity<Boolean> edit(User entity) {
        if (entity.getName() == null || entity.getName().equals("")) {
            return new ResultEntity<>(0, "用户名不能为空");
        }
        entity.setUpdateTime(new Date());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, entity.getId());
        User one = iUserService.getOne(wrapper);
        if (one == null) {
            return new ResultEntity<>(-1, true, "无效的用户");
        }
        wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName, entity.getName());
        wrapper.ne(User::getId, entity.getId());
        List<User> data = iUserService.list(wrapper);
        if (data != null && data.size() > 0) {
            return new ResultEntity<>(-1, true, "当前用户名已经存在了");
        }
        one.setName(entity.getName());
        one.setRealName(entity.getRealName());
        one.setUpdateTime(new Date());
        iUserService.saveOrUpdate(one);
        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/resetPws")
    @ApiOperation(value = "重置密码")
    public ResultEntity<Boolean> resetPws(@RequestParam Integer id) {
        User byId = iUserService.getById(id);
        byId.setPassword("123456");
        byId.setUpdateTime(new Date());
        iUserService.saveOrUpdate(byId);
        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除用户")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        iUserService.removeById(id);
        return new ResultEntity<>(200, true, "操作成功");
    }

}
