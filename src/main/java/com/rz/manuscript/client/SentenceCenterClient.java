package com.rz.manuscript.client;

import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.pojo.request.GetContentKeysRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "175.27.171.171:8100", name = "SentenceCenterClient")
public interface SentenceCenterClient {
    @RequestMapping(value = "/manuscriptRepeatRate/calcRate", method = RequestMethod.POST)
    ResultEntityList<String> getContentKeys(GetContentKeysRequest request);
}
