package com.rz.manuscript.controller;

import cn.hutool.socket.aio.AioServer;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rz.manuscript.common.*;
import com.rz.manuscript.entity.*;
import com.rz.manuscript.exception.RZException;
import com.rz.manuscript.pojo.entity.CacheEntity;
import com.rz.manuscript.pojo.request.*;
import com.rz.manuscript.pojo.vo.*;
import com.rz.manuscript.service.IAiDeformManuscriptService;
import com.rz.manuscript.service.IAiLogService;
import com.rz.manuscript.service.IAiWriteManuscriptService;
import com.rz.manuscript.service.IUserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ai")
@Api(tags = "智能数据")
@Slf4j
public class AIController {

    @Resource
    private HttpServletRequest request;
    @Resource
    private RestTemplate restTemplate;

    @Resource
    private IAiLogService iAiLogService;

    @Resource
    private IUserService iUserService;

    @Resource
    private IAiDeformManuscriptService iAiDeformManuscriptService;

    @Resource
    private IAiWriteManuscriptService iAiWriteManuscriptService;

    @PostMapping("/queryDeformText")
    public ResultEntity<String> queryDeformText(@RequestBody QueryDeformTextRequest queryDeformTextRequest) {
        ResultEntity<String> res = new ResultEntity<>();
        try {
            User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);

            if (currentLoginUser == null)
                return new ResultEntity<>(500, null, "无效的登录用户");
            AiLog aiLog = new AiLog();
            aiLog.setCreateTime(new Date());
            aiLog.setUserId(currentLoginUser.getId());
            aiLog.setContent(queryDeformTextRequest.getContent());

            String url = "http://156.232.9.3:8080/chat/queryChat";
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            queryDeformTextRequest.setContent("对文字变形，尽量不要超过20个汉字:" + queryDeformTextRequest.getContent());
            HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(queryDeformTextRequest), headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String body = response.getBody();
            aiLog.setResponseContent(body);
            res = JSON.parseObject(body, res.getClass());
            if (!res.getCode().equals(200)) {
                res.setMessage("系统出错");
                log.error("queryDeformText 异常" + body);
            }
            iAiLogService.save(aiLog);
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统出错");
            log.error("queryDeformText 异常", e);
        }

        return res;
    }

    @PostMapping("/queryDeformText2")
    public ResultEntity<String> queryDeformText2(@RequestBody QueryDeformTextRequest queryDeformTextRequest) {
        ResultEntity<String> res = new ResultEntity<>();
        User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);

        if (currentLoginUser == null)
            return new ResultEntity<>(500, null, "无效的登录用户");
        try {
            if (queryDeformTextRequest.getContent().equals(""))
                return new ResultEntity<>("");
            String content = queryDeformTextRequest.getContent();
            // 解析HTML
            Document doc = Jsoup.parse(content);
            StringBuilder contentBuilder = new StringBuilder();
            // 去除所有的HTML元素标签
            Elements elements = doc.body().children();
            for (Element element : elements) {
                String tagName = element.tagName();
                if (tagName.equals("h1")) {
                    contentBuilder.append(element.text());
                    contentBuilder.append("\n");
                    // System.out.println(element.text()); // 提取h1标签的文本
                } else if (tagName.equals("p")) {
                    contentBuilder.append(element.text().replaceAll("\\s+", " "));
                    contentBuilder.append("\n");
                    // System.out.println(element.text().replaceAll("\\s+", " ")); // 提取p标签的文本，并将多个空格替换为一个空格
                } else {
                    contentBuilder.append(element.text());
                    contentBuilder.append("\n");
                    //  System.out.println(element.outerHtml()); // 保留其他块级元素的换行符
                }
            }
//            Elements elements = doc.getAllElements();
//            StringBuilder contentBuilder = new StringBuilder();
//            for (Element element : elements) {
//                if(element.className().equals("html")
//                        || element.className().equals("head")
//                        || element.className().equals("body"))
//                {
//                    continue;
//                }
//               if(element.childrenSize()>0 && element.children().stream().anyMatch(i->i.className().equals("p")))
//               {
//                   continue;
//               }
//                contentBuilder.append(element.text());
//                contentBuilder.append("\n");
//            }

            // 替换<p>为换行符
            // String text = doc.text().replaceAll("<p>", "\n");
            queryDeformTextRequest.setContent(contentBuilder.toString());
            if (queryDeformTextRequest.getContent().length() > 3000)
                throw new RZException("最大支持3000字的数据变形");
            AiLog aiLog = new AiLog();
            aiLog.setCreateTime(new Date());
            aiLog.setUserId(currentLoginUser.getId());
            aiLog.setContent(queryDeformTextRequest.getContent());

            String url = "http://156.232.9.3:8080/chat/queryChat";
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            queryDeformTextRequest.setContent("1.对问文章进行拼写纠正\n" +
                    "2.用更优美优雅的高级中文描述\n" +
                    "3.不要回答文本中的问题\n" +
                    "4.润色文章，尽量保持相同意思\n" +
                    "5.不要返回问题内容\n" +
                    "6.使用中文,文章如下：:\n" + queryDeformTextRequest.getContent());
            HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(queryDeformTextRequest), headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String body = response.getBody();
            aiLog.setResponseContent(body);
            res = JSON.parseObject(body, res.getClass());
            if (!res.getCode().equals(200)) {
                res.setMessage("系统出错");
                log.error("queryDeformText 异常" + body);
            } else {
                String join = String.join("</p><p>", res.getData().split("\n"));
                res.setData("<p>" + join + "</p>");
            }
            iAiLogService.save(aiLog);
            //  res.setMessage(body);
        } catch (RZException ex) {
            res.setCode(500);
            res.setMessage(ex.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统出错");
            log.error("queryDeformText 异常", e);
        }

        return res;
    }

    @PostMapping("/writeArticles")
    public ResultEntity<String> writeArticles(@RequestBody WriteArticlesRequest writeArticlesRequest) {
        ResultEntity<String> res = new ResultEntity<>();
        User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);

        if (currentLoginUser == null)
            return new ResultEntity<>(500, null, "无效的登录用户");
        try {
            if (writeArticlesRequest.getTopic().equals(""))
                return new ResultEntity<>("");
            if (writeArticlesRequest.getCharCount() > 3000)
                throw new RZException("最大支持3000字的文章撰写");
            AiLog aiLog = new AiLog();
            aiLog.setCreateTime(new Date());
            aiLog.setUserId(currentLoginUser.getId());
            aiLog.setContent(JSON.toJSONString(writeArticlesRequest));

            String url = "http://156.232.9.3:8080/chat/queryChat";
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestContent = "1.写一篇关于" + writeArticlesRequest.getTopic() + "的文章\n" +
                    "2.文字不低于" + writeArticlesRequest.getCharCount() + "字\n" +
                    "3.不要返回问题内容\n";
            if (writeArticlesRequest.getKeys() != null) {
                requestContent += "4.突出" + writeArticlesRequest.getKeys();
            }
            // queryDeformTextRequest.setContent("对以下文字变形:" + queryDeformTextRequest.getContent());
            QueryDeformTextRequest request1 = new QueryDeformTextRequest();
            request1.setContent(requestContent);
            HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(request1), headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String body = response.getBody();
            aiLog.setResponseContent(body);
            res = JSON.parseObject(body, res.getClass());
            if (!res.getCode().equals(200)) {
                res.setMessage("系统出错");
                log.error("queryDeformText 异常" + body);
            } else {
                String join = String.join("</p><p>", res.getData().split("\n"));
                res.setData("<p>" + join + "</p>");
            }
            iAiLogService.save(aiLog);
            //  res.setMessage(body);
        } catch (RZException ex) {
            res.setCode(500);
            res.setMessage(ex.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统出错");
            log.error("queryDeformText 异常", e);
        }

        return res;
    }

    @PostMapping("/saveAiDeformManuscript")
    public ResultEntity<Integer> saveAiDeformManuscript(@RequestBody AiDeformManuscript aiDeformManuscript) {
        ResultEntity<Integer> res = new ResultEntity<>();
        try {
            User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
            AssertUtil.notNull(currentLoginUser, "无效的登录用户");
            AssertUtil.notNull(aiDeformManuscript, "无效的稿件");
            AssertUtil.notNull(aiDeformManuscript.getContent(), "无效的稿件");
            AssertUtil.isTrue(!StringUtils.isEmpty(aiDeformManuscript.getContent()), "无效的稿件");
            String title = getTitle(aiDeformManuscript.getContent());
            aiDeformManuscript.setTitle(title);
            aiDeformManuscript.setCreateUserId(currentLoginUser.getId());
            aiDeformManuscript.setCreateTime(new Date());
            String saveName = Html2WordUtil.doConvert(aiDeformManuscript.getContent(), -10086);
            aiDeformManuscript.setSavePath(saveName);
            aiDeformManuscript.setCharCount(aiDeformManuscript.getContent().toLowerCase().replace("<p>", "").replace("</p>", "").length());
            if (aiDeformManuscript.getId() != null) {
                iAiDeformManuscriptService.updateById(aiDeformManuscript);
            } else {
                iAiDeformManuscriptService.save(aiDeformManuscript);
            }

            res.setData(aiDeformManuscript.getId());
        } catch (RZException ex) {
            res.setCode(500);
            res.setMessage(ex.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("用户角色列表获取失败", e);
        }

        return res;
    }

    private String getTitle(String content) {
        String res = "";
        try {
            Document doc = Jsoup.parse(content, "UTF-8");
            Elements elements = doc.getAllElements();
            for (Element element : elements) {
                if (element.tagName().equals("p")) {
                    res = element.text();
                    break;
                }
            }
        } catch (Exception e) {
            log.error("未能正确获取稿件标题，{}", content, e);
        }
        if (res.length() < 1) {
            res = content;
        }
        return res;
    }

    @PostMapping("/saveAiWriteManuscript")
    public ResultEntity<Integer> saveAiWriteManuscript(@RequestBody AiWriteManuscript aiWriteManuscript) {
        ResultEntity<Integer> res = new ResultEntity<>();
        try {
            User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
            AssertUtil.notNull(currentLoginUser, "无效的登录用户");
            AssertUtil.notNull(aiWriteManuscript, "无效的稿件");
            AssertUtil.notNull(aiWriteManuscript.getContent(), "无效的稿件");
            AssertUtil.isTrue(!StringUtils.isEmpty(aiWriteManuscript.getContent()), "无效的稿件");
            String title = getTitle(aiWriteManuscript.getContent());
            aiWriteManuscript.setTitle(title);
            aiWriteManuscript.setCreateUserId(currentLoginUser.getId());
            aiWriteManuscript.setCreateTime(new Date());
            String saveName = Html2WordUtil.doConvert(aiWriteManuscript.getContent(), -10010);
            aiWriteManuscript.setSavePath(saveName);
            aiWriteManuscript.setCharCount(aiWriteManuscript.getContent().toLowerCase().replace("<p>", "").replace("</p>", "").length());
            if (aiWriteManuscript.getId() != null) {
                iAiWriteManuscriptService.updateById(aiWriteManuscript);
            } else {
                iAiWriteManuscriptService.save(aiWriteManuscript);
            }


            res.setData(aiWriteManuscript.getId());
        } catch (RZException ex) {
            res.setCode(500);
            res.setMessage(ex.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("用户角色列表获取失败", e);
        }

        return res;
    }


    @PostMapping("/getAiLogList")
    public ResultEntityList<AiLogVo> getAiLogList(@RequestBody QueryAiLogVoRequest queryAiLogVoRequest) {
//        String accessToken = request.getHeader("accessToken");
//        CacheEntity cacheInfo = CacheManager.getCacheInfo(accessToken);
//        if (cacheInfo == null) {
//            return new ResultEntityList<>(0, "当前用户获取失败,请重新登录");
//        }
        ResultEntityList<AiLogVo> res = new ResultEntityList<>();
        try {
            LambdaQueryWrapper<AiLog> aiLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (queryAiLogVoRequest.getUserId() != null) {
                aiLogLambdaQueryWrapper.eq(AiLog::getUserId, queryAiLogVoRequest.getUserId());
            }
            if (!StringUtils.isEmpty(queryAiLogVoRequest.getContent())) {
                aiLogLambdaQueryWrapper.like(AiLog::getContent, queryAiLogVoRequest.getContent());
            }
            Page<AiLog> aiLogPage = new Page<>(queryAiLogVoRequest.getPageIndex(), queryAiLogVoRequest.getPageSize());
            aiLogPage = iAiLogService.page(aiLogPage, aiLogLambdaQueryWrapper);
            res.setTotal(aiLogPage.getTotal());
            res.setPageSize(queryAiLogVoRequest.getPageSize());
            res.setPageIndex(queryAiLogVoRequest.getPageIndex());
            List<AiLog> aiLogs = aiLogPage.getRecords();
            List<AiLogVo> result = new ArrayList<>();
            if (!aiLogs.isEmpty()) {
                List<Integer> userIds = aiLogs.stream().map(i -> i.getUserId()).collect(Collectors.toList());
                LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userLambdaQueryWrapper.in(User::getId, userIds);
                List<User> users = iUserService.list(userLambdaQueryWrapper);
                for (AiLog item : aiLogs) {
                    AiLogVo newItem = new AiLogVo();

                    BeanUtils.copyProperties(item, newItem);
                    User cUser = users.stream().filter(i -> i.getId().equals(item.getUserId())).findFirst().orElse(null);
                    if (cUser != null) {
                        newItem.setUserRealName(cUser.getRealName());
                    }
                    result.add(newItem);
                }
            }
            res.setData(result);


        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("用户角色列表获取失败", e);
        }

        return res;
    }

    @GetMapping("getLogById")
    public ResultEntity<AiLogVo> getLogById(@RequestParam Integer id) {
        ResultEntity<AiLogVo> res = new ResultEntity<>();
        try {
            AssertUtil.notNull(id, "无效的数据");
            AiLog byId = iAiLogService.getById(id);
            AiLogVo newItem = new AiLogVo();
            BeanUtils.copyProperties(byId, newItem);
            res.setData(newItem);
        } catch (RZException ex) {
            res.setCode(500);
            res.setMessage(ex.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("getLogById获取AIlog异常", e);
        }
        return res;
    }

    @PostMapping("/getAiDeformList")
    public ResultEntityList<AiDeformManuscriptVo> getAiDeformList(@RequestBody QueryAiDeformManuscriptRequest queryAiDeformManuscriptRequest) {
        ResultEntityList<AiDeformManuscriptVo> res = new ResultEntityList<>();
        try {
            User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
            AssertUtil.notNull(currentLoginUser, "无效的登录用户");
            LambdaQueryWrapper<AiDeformManuscript> aiLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (!currentLoginUser.getName().equals("admin")) {
                aiLogLambdaQueryWrapper.eq(AiDeformManuscript::getCreateUserId, currentLoginUser.getId());
            }
            if (!StringUtils.isEmpty(queryAiDeformManuscriptRequest.getTitle())) {
                aiLogLambdaQueryWrapper.like(AiDeformManuscript::getTitle, queryAiDeformManuscriptRequest.getTitle());
            }
            Page<AiDeformManuscript> aiDeformManuscriptPage = new Page<>(queryAiDeformManuscriptRequest.getPageIndex(), queryAiDeformManuscriptRequest.getPageSize());
            aiDeformManuscriptPage = iAiDeformManuscriptService.page(aiDeformManuscriptPage, aiLogLambdaQueryWrapper);
            res.setTotal(aiDeformManuscriptPage.getTotal());
            res.setPageSize(queryAiDeformManuscriptRequest.getPageSize());
            res.setPageIndex(queryAiDeformManuscriptRequest.getPageIndex());
            List<AiDeformManuscript> aiLogs = aiDeformManuscriptPage.getRecords();
            List<AiDeformManuscriptVo> result = new ArrayList<>();
            if (!aiLogs.isEmpty()) {
                List<Integer> userIds = aiLogs.stream().map(i -> i.getCreateUserId()).collect(Collectors.toList());
                LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userLambdaQueryWrapper.in(User::getId, userIds);
                List<User> users = iUserService.list(userLambdaQueryWrapper);
                for (AiDeformManuscript item : aiLogs) {
                    AiDeformManuscriptVo newItem = new AiDeformManuscriptVo();

                    BeanUtils.copyProperties(item, newItem);
                    User cUser = users.stream().filter(i -> i.getId().equals(item.getCreateUserId())).findFirst().orElse(null);
                    if (cUser != null) {
                        newItem.setCreateUserName(cUser.getRealName());
                    }
                    result.add(newItem);
                }
            }
            res.setData(result);


        } catch (RZException ex) {
            res.setCode(500);
            res.setMessage(ex.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("getAiDeformList获取失败", e);
        }

        return res;
    }

    @PostMapping("/getAiWriteList")
    public ResultEntityList<AiWriteManuscriptVo> getAiWriteList(@RequestBody QueryAiWriteManuscriptRequest queryAiDeformManuscriptRequest) {
        ResultEntityList<AiWriteManuscriptVo> res = new ResultEntityList<>();
        try {
            User currentLoginUser = LoginUserUtils.getCurrentLoginUser(request);
            AssertUtil.notNull(currentLoginUser, "无效的登录用户");
            LambdaQueryWrapper<AiWriteManuscript> aiLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (!currentLoginUser.getName().equals("admin")) {
                aiLogLambdaQueryWrapper.eq(AiWriteManuscript::getCreateUserId, currentLoginUser.getId());
            }
            if (!StringUtils.isEmpty(queryAiDeformManuscriptRequest.getTitle())) {
                aiLogLambdaQueryWrapper.like(AiWriteManuscript::getTitle, queryAiDeformManuscriptRequest.getTitle());
            }
            Page<AiWriteManuscript> aiDeformManuscriptPage = new Page<>(queryAiDeformManuscriptRequest.getPageIndex(), queryAiDeformManuscriptRequest.getPageSize());
            aiDeformManuscriptPage = iAiWriteManuscriptService.page(aiDeformManuscriptPage, aiLogLambdaQueryWrapper);
            res.setTotal(aiDeformManuscriptPage.getTotal());
            res.setPageSize(queryAiDeformManuscriptRequest.getPageSize());
            res.setPageIndex(queryAiDeformManuscriptRequest.getPageIndex());
            List<AiWriteManuscript> aiLogs = aiDeformManuscriptPage.getRecords();
            List<AiWriteManuscriptVo> result = new ArrayList<>();
            if (!aiLogs.isEmpty()) {
                List<Integer> userIds = aiLogs.stream().map(i -> i.getCreateUserId()).collect(Collectors.toList());
                LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
                userLambdaQueryWrapper.in(User::getId, userIds);
                List<User> users = iUserService.list(userLambdaQueryWrapper);
                for (AiWriteManuscript item : aiLogs) {
                    AiWriteManuscriptVo newItem = new AiWriteManuscriptVo();

                    BeanUtils.copyProperties(item, newItem);
                    User cUser = users.stream().filter(i -> i.getId().equals(item.getCreateUserId())).findFirst().orElse(null);
                    if (cUser != null) {
                        newItem.setCreateUserName(cUser.getRealName());
                    }
                    result.add(newItem);
                }
            }
            res.setData(result);


        } catch (RZException ex) {
            res.setCode(500);
            res.setMessage(ex.getMessage());
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("getAiWriteList获取失败", e);
        }

        return res;
    }

    @GetMapping("/getAiDeform")
    public ResultEntity<AiDeformManuscript> getAiDeform(@RequestParam Integer id) {
        ResultEntity<AiDeformManuscript> res = new ResultEntity<>();
        try {
            AiDeformManuscript data = iAiDeformManuscriptService.getById(id);
            res.setData(data);
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("getAiDeform获取失败", e);
        }

        return res;
    }

    @GetMapping("/getAiWrite")
    public ResultEntity<AiWriteManuscript> getAiWrite(@RequestParam Integer id) {
        ResultEntity<AiWriteManuscript> res = new ResultEntity<>();
        try {
            AiWriteManuscript data = iAiWriteManuscriptService.getById(id);
            res.setData(data);
        } catch (Exception e) {
            res.setCode(500);
            res.setMessage("系统运行出错");
            log.error("getAiDeform获取失败", e);
        }

        return res;
    }

}
