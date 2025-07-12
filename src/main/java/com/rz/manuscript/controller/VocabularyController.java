package com.rz.manuscript.controller;

import com.rz.manuscript.client.SentenceCenterClient;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.neo4j.Vocabulary;
import com.rz.manuscript.exception.RZException;
import com.rz.manuscript.mapper.neo4j.VocabularyMapper;
import com.rz.manuscript.pojo.request.GetContentKeysRequest;
import com.rz.manuscript.pojo.request.QueryVocabularyDicRequest;
import com.rz.manuscript.pojo.vo.VocabularyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vocabulary")
@Api(tags = "近义词")
@Slf4j
public class VocabularyController {
    @Autowired
    private VocabularyMapper vocabularyMapper;

    @Autowired
    private SentenceCenterClient sentenceCenterClient;

    @ApiOperation(value = "获取传递词的近义词")
    @PostMapping("/queryVocabulary")
    public ResultEntityList<VocabularyVo> queryVocabulary(@RequestParam String key) {
        ResultEntityList<VocabularyVo> res = new ResultEntityList<>();
        try {
            if (StringUtils.isEmpty(key))
                throw new RZException("无效的请求参数");
            List<Vocabulary> vocabularies = vocabularyMapper.selectByName(key);
            List<VocabularyVo> result = new ArrayList<>();
            for (Vocabulary item : vocabularies) {
                VocabularyVo newItem = new VocabularyVo();
                BeanUtils.copyProperties(item, newItem);
                result.add(newItem);
            }
            res.setData(result);
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


    @ApiOperation(value = "获取近义词词典")
    @PostMapping("/queryVocabularyDic")
    public ResultEntityList<String> queryVocabularyDic(@RequestBody QueryVocabularyDicRequest queryVocabularyDicRequest) {
        ResultEntityList<String> res = new ResultEntityList<>();
        try {
            if (queryVocabularyDicRequest == null)
                throw new RZException("无效的请求参数");
            GetContentKeysRequest getContentKeysRequest = new GetContentKeysRequest();
            getContentKeysRequest.setContent(queryVocabularyDicRequest.getContent());
            ResultEntityList<String> contentKeys = sentenceCenterClient.getContentKeys(getContentKeysRequest);
            if (!contentKeys.getCode().equals(200))
                return contentKeys;

//            if (queryVocabularyDicRequest.getKeys() == null)
//                throw new RZException("无效的请求参数");
            List<Vocabulary> vocabularies = vocabularyMapper.findByNamesAndNodeType(contentKeys.getData(), 1);
            res.setData(vocabularies.stream().map(i -> i.getName()).collect(Collectors.toList()));
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

