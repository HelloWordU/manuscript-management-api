package com.rz.manuscript.controller;

import com.rz.manuscript.common.Html2WordUtil;
import com.rz.manuscript.common.LoginUserUtils;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.Manuscript;
import com.rz.manuscript.entity.Project;
import com.rz.manuscript.entity.User;
import com.rz.manuscript.entity.WriteSelectedTopic;
import com.rz.manuscript.pojo.request.ConfirmCompleteRequest;
import com.rz.manuscript.pojo.request.GetContentKeysRequest;
import com.rz.manuscript.pojo.request.SelectTopicSaveRequest;
import com.rz.manuscript.pojo.vo.WriteSelectedTopicGetListRequest;
import com.rz.manuscript.pojo.vo.WriteSelectedTopicVo;
import com.rz.manuscript.service.IManuscriptService;
import com.rz.manuscript.service.IProjectService;
import com.rz.manuscript.service.IWriteSelectedTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/writeSelectedTopic")
@Slf4j
@Api(tags = "撰写选题")
public class WriteSelectedTopicController {
    @Resource
    private IWriteSelectedTopicService iWriteSelectTopicService;

    @Resource
    private IManuscriptService iManuscriptService;

    @Resource
    private IProjectService iProjectService;

    @Resource
    private HttpServletRequest request;

    @PostMapping("/getList")
    @ApiOperation(value = "获取范文列表-分页")
    public ResultEntityList<WriteSelectedTopicVo> getSelectTopic(@RequestBody WriteSelectedTopicGetListRequest writeSelectedTopicGetListRequest) {
        User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
        if (currentLoginUser == null)
            return new ResultEntityList<>(500, null, "无效的登录用户");
        if(!currentLoginUser.getName().equals("admin"))
        {
            writeSelectedTopicGetListRequest.setUserId(currentLoginUser.getId());
        }
        List<WriteSelectedTopicVo> resData = iWriteSelectTopicService.getList(writeSelectedTopicGetListRequest);
        ResultEntityList<WriteSelectedTopicVo> res = new ResultEntityList<>(200, resData, "获取成功");
        return res;
    }

    @PostMapping("/getById")
    @ApiOperation(value = "获取范文")
    public ResultEntity<WriteSelectedTopicVo> getSelectTopic(@RequestParam @ApiParam(value = "范文id", required = true) int id) {
        ResultEntity<WriteSelectedTopicVo> res = new ResultEntity<>();
        try {
            WriteSelectedTopicVo resData = iWriteSelectTopicService.getVoById(id);
            res = new ResultEntity<>(200, resData, "获取成功");
        } catch (Exception e) {
            log.error("保存范文异常", e);
            res.setCode(500);
            res.setMessage("系统出错");
        }
        return res;
    }

    @PostMapping("/getContentKeys")
    @ApiOperation(value = "获取文章内容分词")
    public ResultEntityList<String> getContentKeys(@RequestBody GetContentKeysRequest request) {
        ResultEntityList<String> res = new ResultEntityList<>();
        try {
            List<String> resData = iWriteSelectTopicService.getContentKeys(request);
            res = new ResultEntityList<>(200, resData, "获取成功");
        } catch (Exception e) {
            log.error("获取文章内容分词异常", e);
            res.setCode(500);
            res.setMessage("系统出错");
        }
        return res;
    }
    @PostMapping("/getContentCategory")
    @ApiOperation(value = "获取文章内容分类")
    public ResultEntityList<String> getContentCategory(@RequestBody GetContentKeysRequest request) {
        ResultEntityList<String> res = new ResultEntityList<>();
        try {
            List<String> resData = iWriteSelectTopicService.getContentCategory(request);
            res = new ResultEntityList<>(200, resData, "获取成功");
        } catch (Exception e) {
            log.error("获取文章内容分词异常", e);
            res.setCode(500);
            res.setMessage("系统出错");
        }
        return res;
    }

    @PostMapping("/saveSelectTopic")
    @ApiOperation(value = "保存范文")
    public ResultEntity<Boolean> saveSelectTopic(@RequestBody SelectTopicSaveRequest selectTopicSaveRequest) {
        ResultEntity<Boolean> res = new ResultEntity<>(200, true, "操作成功");
        try {
            WriteSelectedTopic resData = iWriteSelectTopicService.getById(selectTopicSaveRequest.getId());
            if (resData != null) {
                resData.setTitle(selectTopicSaveRequest.getTitle());
                resData.setContent(selectTopicSaveRequest.getContent());
                resData.setCharCount(selectTopicSaveRequest.getCharCount());
                resData.setLastModifyDate(new Date());
                iWriteSelectTopicService.updateById(resData);
            }
        } catch (Exception e) {
            log.error("保存范文异常", e);
            res.setCode(500);
            res.setMessage("系统出错");
        }
        return res;
    }

    @PostMapping("/confirmComplete")
    @ApiOperation(value = "确认完成")
    public ResultEntity<Boolean> confirmComplete(@RequestBody ConfirmCompleteRequest confirmCompleteRequest) {
        ResultEntity<Boolean> res = new ResultEntity<>(200, true, "操作成功");
        try {
            User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);

            if (currentLoginUser == null)
                return new ResultEntity<>(500, null, "无效的登录用户");
            WriteSelectedTopic resData = iWriteSelectTopicService.getById(confirmCompleteRequest.getId());
            if (resData != null) {
                String saveName = Html2WordUtil.doConvert(resData.getContent(), resData.getProjectId());
                Manuscript manuscript = new Manuscript();
                manuscript.setContent(resData.getContent());
                manuscript.setCharCount(resData.getCharCount());
                manuscript.setCreateTime(new Date());
                manuscript.setTitle(resData.getTitle());
                manuscript.setProjectId(resData.getProjectId());
                manuscript.setProductId(confirmCompleteRequest.getProductId());
                manuscript.setTypeId(confirmCompleteRequest.getTypeId());
                manuscript.setSavePath(saveName);
                manuscript.setTitle(resData.getTitle() + ".docx");
                manuscript.setAuth(currentLoginUser.getRealName());
                iManuscriptService.save(manuscript);
                iManuscriptService.calcRate(manuscript);
                Project byId = iProjectService.getById(confirmCompleteRequest.getProductId());
                iManuscriptService.updateManuscriptCount(byId, true);
                resData.setIsComplete(true);
                iWriteSelectTopicService.updateById(resData);
            }
        } catch (Exception e) {
            log.error("确认完成异常", e);
            res.setCode(500);
            res.setMessage("系统出错");
        }
        return res;

    }


}
