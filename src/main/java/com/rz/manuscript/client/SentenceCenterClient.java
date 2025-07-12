package com.rz.manuscript.client;

import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.pojo.request.AddSentenceRequest;
import com.rz.manuscript.pojo.request.GetContentKeysRequest;
import com.rz.manuscript.pojo.request.QuerySentenceByContentRequest;
import com.rz.manuscript.pojo.request.QuerySentenceRequest;
import com.rz.manuscript.pojo.vo.SentenceVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "175.27.171.171:8100", name = "SentenceCenterClient")
public interface SentenceCenterClient {
    @RequestMapping(value = "/api/v1/sentence/getContentKeys", method = RequestMethod.POST)
    ResultEntityList<String> getContentKeys(@RequestBody GetContentKeysRequest request);

    @RequestMapping(value = "/api/v1/sentence/getContentCategory", method = RequestMethod.POST)
    ResultEntityList<String> getContentCategory(@RequestBody GetContentKeysRequest request);
    @RequestMapping(value = "/api/v1/sentence/add", method = RequestMethod.POST)
    ResultEntityList<String> add(AddSentenceRequest request);
    @RequestMapping(value = "/api/v1/sentence/query", method = RequestMethod.POST)
    ResultEntityList<SentenceVo> query(@RequestBody QuerySentenceRequest request);

    @RequestMapping(value = "/api/v1/sentence/getESSentenceKeys", method = RequestMethod.POST)
    ResultEntityList<String> getESSentenceKeys( @RequestBody GetContentKeysRequest request);

    @RequestMapping(value = "/api/v1/sentence/queryByContent", method = RequestMethod.POST)
    ResultEntityList<SentenceVo> queryByContent(@RequestBody QuerySentenceByContentRequest request);

    @RequestMapping(value = "/api/v1/sentence/queryByContentCategory", method = RequestMethod.POST)
    ResultEntityList<SentenceVo> queryByContentCategory(@RequestBody QuerySentenceByContentRequest request);
    @RequestMapping(value = "/api/v1/sentence/update", method = RequestMethod.POST)
    ResultEntity<Boolean> update(@RequestBody SentenceVo sentenceVo);
    @RequestMapping(value = "/api/v1/sentence/delete", method = RequestMethod.POST)
    ResultEntity<Boolean> delete(@RequestParam("id") String id);
}
