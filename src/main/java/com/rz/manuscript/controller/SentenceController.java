package com.rz.manuscript.controller;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.rz.manuscript.client.SentenceCenterClient;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.Project;
import com.rz.manuscript.pojo.request.AddSentenceRequest;
import com.rz.manuscript.pojo.request.QuerySentenceByContentRequest;
import com.rz.manuscript.pojo.request.QuerySentenceRequest;
import com.rz.manuscript.pojo.vo.SentenceVo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sentence")
@Api(tags = "句库")
@Slf4j
public class SentenceController {

    @Resource
    private SentenceCenterClient sentenceCenterClient;

    @PostMapping("/queryByContent")
    public ResultEntityList<SentenceVo> queryByContent(@RequestBody QuerySentenceByContentRequest request) {
        return sentenceCenterClient.queryByContent(request);
    }
    @PostMapping("/queryByContentCategory")
    public ResultEntityList<SentenceVo> queryByContentCategory(@RequestBody QuerySentenceByContentRequest request) {
        return sentenceCenterClient.queryByContentCategory(request);
    }


    @PostMapping("/query")
    public ResultEntityList<SentenceVo> query(@RequestBody QuerySentenceRequest request) {
        return sentenceCenterClient.query(request);
    }

    @PostMapping("/update")
    public ResultEntity<Boolean> update(@RequestBody SentenceVo sentenceVo) {
        return sentenceCenterClient.update(sentenceVo);
    }

    @PostMapping("/delete")
    public ResultEntity<Boolean> delete(@RequestParam String id) {
        return sentenceCenterClient.delete(id);
    }

//    @GetMapping("/init")
//    public ResultEntity<Boolean> init() {
//        ResultEntity<Boolean> res = new ResultEntity<>();
//        List<File> files = FileUtil.loopFiles("H:\\华为手机");
//        List<AddSentenceRequest> addSentenceRequests = new ArrayList<>();
//        for (File txtFile : files) {
//            String title = FileUtil.getPrefix(txtFile);
//            String content = FileUtil.readString(txtFile, "utf-8");
//            Document doc = Jsoup.parse(content);
//            // 去除所有的HTML元素标签
//            Elements elements = doc.body().children();
//            if (elements.isEmpty())
//                continue;
//            ;
//            if (elements.size() == 1) {
//                elements = elements.eq(0);
//            }
//            for (Element element : elements) {
//                String info = element.text();
//                if (StringUtils.isEmpty(info))
//                    continue;
//                if (info.length() < 10)
//                    continue;
//                ;
//                AddSentenceRequest request = new AddSentenceRequest();
//                request.setTitle(title);
//                request.setContent(info);
//                request.setSearchKey("华为手机");
//                request.setProjectId(1L);
//                log.info("开始保存数据----" + JSON.toJSONString(request));
//                try {
//                    ResultEntityList<String> add = sentenceCenterClient.add(request);
//                    log.info("保存数据结束----" + JSON.toJSONString(add) + "-------" + JSON.toJSONString(request));
//                } catch (Exception e) {
//                    log.error("保存数据失败 ！" + JSON.toJSONString(request), e);
//                }
//
//                //addSentenceRequests.add(request);
//            }
//        }
//        return res;
//    }
}
