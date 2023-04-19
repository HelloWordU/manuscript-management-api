package com.rz.manuscript.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.CacheManager;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.Company;
import com.rz.manuscript.pojo.entity.CacheEntity;
import com.rz.manuscript.service.ICompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/company")
@Api(tags = "公司")
public class CompanyController {
    @Resource
    private HttpServletRequest request;
    @Resource
    private ICompanyService iCompanyService;

    @GetMapping("/get")
    public ResultEntityList<Company> getUser() {
        String accessToken = request.getHeader("accessToken");
        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
        if (cacheInfo == null) {
            return new ResultEntityList<>(0, "当前用户获取失败,请重新登录");
        }
        LambdaQueryWrapper<Company> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Company> res = iCompanyService.list(userLambdaQueryWrapper);
        return new ResultEntityList<Company>(200, res, "获取成功");
    }


    @PostMapping("/save")
    @ApiOperation(value = "保存公司")
    public ResultEntity<Boolean> save(@RequestBody Company entity) {
        if (entity.getName() == null || entity.getName().equals("")) {
            return new ResultEntity<>(0, "公司名称不能为空");
        }

        entity.setUpdateTime(new Date());
        if(entity.getId()==null || entity.getId()<1)
        {
            LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Company::getName, entity.getName());
            List<Company> data = iCompanyService.list(wrapper);
            if (data != null && data.size() > 0) {
                return new ResultEntity<>(-1, true, "当前公司已经存在了");
            }
            entity.setCreateTime(new Date());
        }else
        {
            LambdaQueryWrapper<Company> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Company::getName, entity.getName());
            List<Company> data = iCompanyService.list(wrapper);
            if (data != null && data.size() > 0) {
               for(Company item : data )
               {
                   if(item.getId()!= entity.getId())
                   {
                       return new ResultEntity<>(-1, true, "当前公司已经存在了");
                   }
               }
            }
        }

        entity.setUpdateTime(new Date());
        iCompanyService.saveOrUpdate(entity);

        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除用户")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        iCompanyService.removeById(id);
        return new ResultEntity<>(200, true, "操作成功");
    }

}
