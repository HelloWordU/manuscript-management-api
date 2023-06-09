package com.rz.manuscript.controller;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rz.manuscript.client.RepeatRateClient;
import com.rz.manuscript.common.CacheManager;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.common.enums.PublishStateEnum;
import com.rz.manuscript.entity.*;
import com.rz.manuscript.pojo.entity.CacheEntity;
import com.rz.manuscript.pojo.vo.CalcRateRequest;
import com.rz.manuscript.pojo.vo.GetManuscriptRequest;
import com.rz.manuscript.pojo.vo.ManuscriptVo;
import com.rz.manuscript.pojo.vo.SaveRepeatRateRequest;
import com.rz.manuscript.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/manuscript")
@Api(tags = "稿件")
@Slf4j
public class ManuscriptController {

    @Resource
    private IProjectService iProjectService;

    @Resource
    private IManuscriptService iManuscriptService;

    @Resource
    private IManuscriptHistoryService iManuscriptHistoryService;


    @Resource
    private HttpServletRequest request;
    @Resource
    private IProjectCategoryService iProjectCategoryService;

    @Resource
    private RepeatRateClient repeatRateClient;


    @Resource
    private IOrderService iOrderService;

    @Resource
    private IOrderManuscriptService iOrderManuscriptService;

    @GetMapping("/get")
    @ApiOperation(value = "获取稿件")
    public ResultEntity<ManuscriptVo> getManuscript(Integer id) {
        //  HttpSession session = request.getSession();
//        String accessToken = request.getHeader("accessToken");
//        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
//        if (cacheInfo == null) {
//            return new ResultEntity<>(0, "当前用户获取大屏配置失败,请重新登录");
//        }
//        Integer companyId = ((User) cacheInfo.getValue()).getCompanyId();
        LambdaQueryWrapper<Manuscript> wrapper = new LambdaQueryWrapper<>();
        Manuscript one = iManuscriptService.getById(id);
        ManuscriptVo res = new ManuscriptVo();
        BeanUtil.copyProperties(one, res);
        return new ResultEntity<>(200, res, "获取成功");
    }


    @GetMapping("/getWaitOrderList")
    public ResultEntityList<Manuscript> getWaitOrderList(@RequestParam(required = false) String q, @RequestParam Integer page, @RequestParam Integer projectId) {
        LambdaQueryWrapper<Manuscript> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Manuscript::getProjectId, projectId);
        wrapper.eq(Manuscript::getPublishState, PublishStateEnum.未发布.getCode());
        wrapper.eq(Manuscript::getIsChecked, true);
        if (q != null && q.trim().length() != 0) {
            wrapper.like(Manuscript::getTitle, q);
        }
        Page<Manuscript> s = new Page<>(page, 10L);
        Page<Manuscript> page1 = iManuscriptService.page(s, wrapper);
        ResultEntityList<Manuscript> res = new ResultEntityList<>(200, page1.getRecords(), "获取成功");
        res.setPageIndex(page);
        res.setPageSize(10);
        res.setTotal(page1.getTotal());
        return res;
    }

    @PostMapping("/getList")
    public ResultEntityList<ManuscriptVo> getList(@RequestBody GetManuscriptRequest request) {
        Integer total = iManuscriptService.getTotal(request);

        List<ManuscriptVo> list = new ArrayList<>();
        if (total > 0) {
            list = iManuscriptService.getList(request);
        }


        return new ResultEntityList<>(200, list, "获取成功", total.longValue());

    }

    @PostMapping("/getCustomerList")
    public ResultEntityList<ManuscriptVo> getCustomerList(@RequestBody GetManuscriptRequest request) {

        List<ManuscriptVo> list = iManuscriptService.getCustomerList(request);
        return new ResultEntityList<>(200, list, "获取成功");

    }

    @PostMapping("/save")
    public ResultEntity<Boolean> save(@RequestBody Project entity) {
        entity.setModifyTime(new Date());
        if (entity.getId() < 1) {
            entity.setCreateTime(new Date());
        }
        iProjectService.saveOrUpdate(entity);
        return new ResultEntity<>(200, true, "保存成功");
    }


    @PostMapping("/upload")
    public ResultEntity<Boolean> upload(@RequestParam Integer projectId, MultipartHttpServletRequest request) {
        projectId = Integer.parseInt(request.getParameter("projectId"));
        Integer type = Integer.parseInt(request.getParameter("type") == null ? "0" : request.getParameter("type"));
        Integer product = Integer.parseInt(request.getParameter("product") == null ? "0" : request.getParameter("product"));
        if (projectId == null || projectId < 1) {
            return new ResultEntity<>(0, true, "无效的项目");
        }
        Map<String, MultipartFile> fileMap = request.getFileMap();
        if (fileMap == null || fileMap.isEmpty()) {
            return new ResultEntity<>(0, true, "无效的文件");
        }
        String uploadInfo = "";
        for (String key : fileMap.keySet()) {
            MultipartFile uploadFile = fileMap.get(key);
            if (uploadFile.isEmpty())
                uploadInfo += key + "无效的文件";
            //return new ResultEntity<>(0, true, "无效的文件");
            String fileName = uploadFile.getOriginalFilename();
            String fileSuffix = FileNameUtil.extName(fileName);
            if (!fileSuffix.equals("doc") && !fileSuffix.equals("docx")) {
                uploadInfo += key + uploadFile.getOriginalFilename() + "无效的文件格式，doc、docx";
            }
            try {
                XWPFDocument doc = new XWPFDocument(uploadFile.getInputStream());
                XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
                String text = extractor.getText();
                //保存到数据库和文件
                String filePath = "D:\\test" + "\\" + projectId;
                if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                    filePath = "/home/file/" + projectId;
                }
                File file = new File(filePath);
                if (!file.exists()) {

                    file.mkdir();

                }//重新设置文件名为 UUID，以确保唯一
                String saveName = UUID.randomUUID() + "." + fileSuffix;

                file = new File(filePath, saveName);
                uploadFile.transferTo(file);
                String title = fileName.replace("." + fileSuffix, "");
                String author = "";
                String[] split = title.split("&");
                if (split.length > 1) {
                    author = split[1];
                }
                Manuscript entity = new Manuscript();
                entity.setAuth(author);
                entity.setCharCount(text.length());
                entity.setContent(text);
                entity.setFileName(fileName);
                entity.setOriginalPresent(90);
                entity.setTitle(title);
                entity.setSavePath(saveName);
                entity.setTypeId(type);
                entity.setProductId(product);
                entity.setProjectId(projectId);
                iManuscriptService.save(entity);
                calcRate(entity);
                Project byId = iProjectService.getById(projectId);
                updateManuscriptCount(byId, true);
            } catch (Exception e) {
                uploadInfo += key + uploadFile.getOriginalFilename() + "读取文件失败" + e.getMessage();
                log.error("读取文件失败", e);
            }
        }
        return new ResultEntity<>(200, true, "上传成功!" + uploadInfo);
    }

    private void calcRate(Manuscript entity) {
        CalcRateRequest request = new CalcRateRequest();
        request.setContext(entity.getContent());
        request.setManuscriptId(entity.getId());
        repeatRateClient.calcRate(request);
    }

    /**
     * 更新稿件上传数量
     *
     * @param byId
     */
    @Synchronized
    private void updateManuscriptCount(Project byId, Boolean isAdd) {
        if (byId != null) {
            if (isAdd) {
                byId.setManuscriptUploadCount(byId.getManuscriptUploadCount() + 1);
            } else {
                byId.setManuscriptUploadCount(byId.getManuscriptUploadCount() - 1);
            }
            iProjectService.updateById(byId);
        }

    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除用户")
    public ResultEntity<Boolean> delete(@RequestParam Integer id) {
        Manuscript m = iManuscriptService.getById(id);
        if (m != null) {
            Project byId = iProjectService.getById(m.getProjectId());
            updateManuscriptCount(byId, false);
            iManuscriptService.removeById(id);
        }

        return new ResultEntity<>(200, true, "操作成功");
    }


    @PostMapping("/single-upload")
    public ResultEntity<Boolean> singleUpload(@RequestParam Integer id, @RequestParam MultipartFile uploadFile) {
        if (id == null || id < 1) {
            return new ResultEntity<>(0, true, "无效的稿件");
        }
        Manuscript entity = iManuscriptService.getById(id);

        if (entity == null || entity.getId() < 1) {
            return new ResultEntity<>(0, true, "无效的稿件");
        }
        Integer projectId = entity.getProjectId();
        String uploadInfo = "";
        if (uploadFile.isEmpty())
            return new ResultEntity<>(0, true, "无效的文件");
        if (!FileNameUtil.extName(uploadFile.getOriginalFilename()).equals("doc") && !FileNameUtil.extName(uploadFile.getOriginalFilename()).equals("docx")) {
            return new ResultEntity<>(0, true, "无效的文件格式，只支持doc、docx");
        }
        String fileName = uploadFile.getOriginalFilename();
        String fileSuffix = FileNameUtil.extName(fileName);
        try {
            XWPFDocument doc = new XWPFDocument(uploadFile.getInputStream());
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            String text = extractor.getText();
            //保存到数据库和文件
            String filePath = "D:\\test" + "\\" + projectId;
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                filePath = "/home/file/" + projectId;
            }
            File file = new File(filePath);
            if (!file.exists()) {

                file.mkdir();

            }//重新设置文件名为 UUID，以确保唯一
            String saveName = UUID.randomUUID() + "." + fileSuffix;

            file = new File(filePath, saveName);
            uploadFile.transferTo(file);
            String title = fileName.replace("." + fileSuffix, "");
            String author = "";
            String[] split = title.split("&");
            if (split.length > 1) {
                author = split[1];
            }
            ManuscriptHistory mh = new ManuscriptHistory();
            BeanUtil.copyProperties(entity, mh);
            mh.setManuscriptId(entity.getId());
            mh.setId(null);
            iManuscriptHistoryService.save(mh);
            if (author != "") {
                entity.setAuth(author);
            }
            entity.setCharCount(text.length());
            entity.setContent(text);
            entity.setFileName(fileName);
            entity.setTitle(title);
            entity.setSavePath(saveName);
            entity.setUpdateTime(new Date());
            iManuscriptService.updateById(entity);
            calcRate(entity);
        } catch (Exception e) {
            log.error("读取文件失败", e);
            return new ResultEntity<>(0, true, "上传失败!" + e.getMessage());
        }

        return new ResultEntity<>(200, true, "上传成功!");
    }

    @PostMapping("/checkPass")
    public ResultEntity<Boolean> checkPass(@RequestParam Integer id) {
        Manuscript entity = iManuscriptService.getById(id);

        if (entity == null || entity.getId() < 1) {
            return new ResultEntity<>(0, true, "无效的稿件");
        }
        entity.setIsChecked(true);
        entity.setCheckState(2);
        iManuscriptService.updateById(entity);
        return new ResultEntity<>(200, true, "审核成功");
    }

    @PostMapping("/setReUpload")
    public ResultEntity<Boolean> setReUpload(@RequestParam Integer id) {
        Manuscript entity = iManuscriptService.getById(id);

        if (entity == null || entity.getId() < 1) {
            return new ResultEntity<>(0, true, "无效的稿件");
        }
        entity.setCheckState(3);
        iManuscriptService.updateById(entity);
        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/setReadyCheck")
    public ResultEntity<Boolean> setReadyCheck(@RequestParam Integer id) {
        Manuscript entity = iManuscriptService.getById(id);

        if (entity == null || entity.getId() < 1) {
            return new ResultEntity<>(0, true, "无效的稿件");
        }

        if (entity.getCheckState() != 0 && entity.getCheckState() != 3) {
            return new ResultEntity<>(0, true, "稿件无需审核或正在审核中");
        }
        entity.setCheckState(1);
        iManuscriptService.updateById(entity);
        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/saveRepeatRate")
    public ResultEntity<Boolean> saveRepeatRate(@RequestBody SaveRepeatRateRequest request) {
        Manuscript entity = iManuscriptService.getById(request.getId());

        if (entity == null || entity.getId() < 1) {
            return new ResultEntity<>(0, true, "无效的稿件");
        }
        entity.setRepeatRate(request.getRepeatRate());
        entity.setMaxRepeatRateManuscriptId(request.getMaxRepeatRateManuscriptId());
        iManuscriptService.updateById(entity);
        return new ResultEntity<>(200, true, "操作成功");
    }

    @PostMapping("/uploadUrl")
    @ApiOperation(value = "上传文章URL")
    public ResultEntity<Boolean> upload(@RequestParam Integer manuscriptId, @RequestParam String url, @RequestParam MultipartFile uploadFile) {
        if (manuscriptId == null || manuscriptId < 1) {
            return new ResultEntity<>(0, true, "无效的稿件");
        }
        if (uploadFile.isEmpty())
            return new ResultEntity<>(0, true, "无效的文件");
        if (!FileNameUtil.extName(uploadFile.getOriginalFilename()).equals("png") && !FileNameUtil.extName(uploadFile.getOriginalFilename()).equals("jpg")) {
            return new ResultEntity<>(0, true, "无效的文件格式，只支持png、jpg");
        }
        Manuscript entity = iManuscriptService.getById(manuscriptId);
        Integer projectId = entity.getProjectId();
        String fileName = uploadFile.getOriginalFilename();
        String fileSuffix = FileNameUtil.extName(fileName);
        try {
            //保存到数据库和文件
            String filePath = "D:\\test" + "\\" + projectId + "";
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                filePath = "/home/file/" + projectId + "";
            }
            File file = new File(filePath);
            if (!file.exists()) {

                file.mkdir();

            }//重新设置文件名为 UUID，以确保唯一

            filePath = "D:\\test" + "\\" + projectId + "\\screen";
            if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                filePath = "/home/file/" + projectId + "/screen";
            }
            file = new File(filePath);
            if (!file.exists()) {

                file.mkdir();

            }//重新设置文件名为 UUID，以确保唯一
            String saveName = UUID.randomUUID() + "." + fileSuffix;

            file = new File(filePath, saveName);
            uploadFile.transferTo(file);
            entity.setShotScreenUrl(saveName);
            entity.setUrl(url);
            boolean isPublished = entity.getPublishState() == PublishStateEnum.已发布.getCode();

            entity.setPublishState(PublishStateEnum.已发布.getCode());
            iManuscriptService.updateById(entity);

            //获取订单下所有已经发布的稿件 更新发布数量
//            LambdaQueryWrapper<Manuscript> manuscriptWrapper = new LambdaQueryWrapper<>();
////            if(!isPublished)
////            {
            LambdaQueryWrapper<OrderManuscript> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderManuscript::getManuscriptId, manuscriptId);
            List<OrderManuscript> orderManuscripts = iOrderManuscriptService.list(wrapper);
            for (OrderManuscript one : orderManuscripts) {
                calcManuscriptPublishCount(one.getOrderId());
            }

//            Order order = iOrderService.getById(one.getOrderId());
//            order.setManuscriptPublishCount(order.getManuscriptPublishCount() + 1);
//            iOrderService.updateById(order);
            //   }
        } catch (IOException e) {
            log.error("读取文件失败", e);
        }
        return new ResultEntity<>(200, true, "上传成功");
    }

    @Async("CalcRateControllerThreadExecutor")
    public void calcManuscriptPublishCount(Integer orderId) {

        LambdaQueryWrapper<OrderManuscript> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderManuscript::getOrderId, orderId);
        List<OrderManuscript> one = iOrderManuscriptService.list(wrapper);
        List<Integer> manuscriptIdList = one.stream().map(OrderManuscript::getManuscriptId).collect(Collectors.toList());

        LambdaQueryWrapper<Manuscript> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Manuscript::getPublishState, PublishStateEnum.已发布.getCode());
        wrapper1.in(Manuscript::getId, manuscriptIdList);

        Page<Manuscript> s = new Page<>(1, 1);
        Page<Manuscript> page1 = iManuscriptService.page(s, wrapper1);
        Order order = iOrderService.getById(orderId);
        order.setManuscriptPublishCount(new Long(page1.getTotal()).intValue());
        iOrderService.updateById(order);

    }

}
