package com.rz.manuscript.controller;

import com.alibaba.fastjson.JSON;
import com.rz.manuscript.common.LoginUserUtils;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.entity.User;
import com.rz.manuscript.exception.RZException;
import com.rz.manuscript.pojo.vo.QueryDeformTextRequest;
import com.rz.manuscript.pojo.request.WriteArticlesRequest;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@RestController
@RequestMapping("/ai")
@Api(tags = "智能数据")
@Slf4j
public class AIController {

    @Resource
    private HttpServletRequest request;
    @Resource
    private RestTemplate restTemplate;

    @PostMapping("/queryDeformText")
    public ResultEntity<String> queryDeformText(@RequestBody QueryDeformTextRequest queryDeformTextRequest) {
        ResultEntity<String> res = new ResultEntity<>();
        try {
            String url = "http://156.232.13.241:8080/chat/queryChat";
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            queryDeformTextRequest.setContent("对文字变形，尽量不要超过20个汉字:" + queryDeformTextRequest.getContent());
            HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(queryDeformTextRequest), headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String body = response.getBody();
            res = JSON.parseObject(body, res.getClass());
            if (!res.getCode().equals(200)) {
                res.setMessage("系统出错");
                log.error("queryDeformText 异常" + body);
            }
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
            if (queryDeformTextRequest.getContent().length() > 3000)
                throw new RZException("最大支持3000字的数据变形");
            String url = "http://156.232.13.241:8080/chat/queryChat";
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            queryDeformTextRequest.setContent("使用中文对以下文章进行改写:\n" + queryDeformTextRequest.getContent());
            HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(queryDeformTextRequest), headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String body = response.getBody();
            res = JSON.parseObject(body, res.getClass());
            if (!res.getCode().equals(200)) {
                res.setMessage("系统出错");
                log.error("queryDeformText 异常" + body);
            } else {
                String join = String.join("</p><p>", res.getData().split("\n"));
                res.setData("<p>" + join + "</p>");
            }
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
            if (writeArticlesRequest.getCharCount() > 1500)
                throw new RZException("最大支持1500字的文章撰写");
            String url = "http://156.232.13.241:8080/chat/queryChat";
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestContent = "写一篇关于" + writeArticlesRequest.getTopic() + "的文章，字数不低于" + writeArticlesRequest.getCharCount() + "字";
            if (writeArticlesRequest.getKeys() != null) {
                requestContent += "，突出" + writeArticlesRequest.getKeys();
            }
            // queryDeformTextRequest.setContent("对以下文字变形:" + queryDeformTextRequest.getContent());
            QueryDeformTextRequest request1 = new QueryDeformTextRequest();
            request1.setContent(requestContent);
            HttpEntity<String> entity = new HttpEntity<String>(JSON.toJSONString(request1), headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String body = response.getBody();
            res = JSON.parseObject(body, res.getClass());
            if (!res.getCode().equals(200)) {
                res.setMessage("系统出错");
                log.error("queryDeformText 异常" + body);
            }else {
                String join = String.join("</p><p>", res.getData().split("\n"));
                res.setData("<p>" + join + "</p>");
            }
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


}
