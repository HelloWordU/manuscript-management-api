package com.rz.manuscript.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.file.FileNameUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.Manuscript;
import com.rz.manuscript.entity.ManuscriptHistory;
import com.rz.manuscript.entity.Project;
import com.rz.manuscript.pojo.vo.GetManuscriptRequest;
import com.rz.manuscript.pojo.vo.ManuscriptVo;
import com.rz.manuscript.service.IManuscriptHistoryService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/manuscriptHistory")
@Api(tags = "项目")
@Slf4j
public class ManuscriptHistoryController {


    @Resource
    private IManuscriptHistoryService iManuscriptHistoryService;


    @Resource
    private HttpServletRequest request;
    @Resource
    private IProjectCategoryService iProjectCategoryService;

//    @GetMapping("/get")
//    @ApiOperation(value = "获取稿件")
//    public ResultEntity<ManuscriptVo> getManuscript(Integer id) {
//        //  HttpSession session = request.getSession();
////        String accessToken = request.getHeader("accessToken");
////        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
////        if (cacheInfo == null) {
////            return new ResultEntity<>(0, "当前用户获取大屏配置失败,请重新登录");
////        }
////        Integer companyId = ((User) cacheInfo.getValue()).getCompanyId();
//        LambdaQueryWrapper<Manuscript> wrapper = new LambdaQueryWrapper<>();
//        Manuscript one = iManuscriptService.getVoById(id);
//        ManuscriptVo res = new ManuscriptVo();
//        BeanUtil.copyProperties(one, res);
//        return new ResultEntity<>(200, res, "获取成功");
//    }

    @GetMapping("/getList")
    public ResultEntityList<ManuscriptHistory> getList(@RequestParam Integer mId) {

        LambdaQueryWrapper<ManuscriptHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ManuscriptHistory::getManuscriptId,mId);
        wrapper.orderBy(true,false,ManuscriptHistory::getCreateTime);
        List<ManuscriptHistory> list = iManuscriptHistoryService.list(wrapper);

        return new ResultEntityList<>(200, list, "获取成功");

    }



}
