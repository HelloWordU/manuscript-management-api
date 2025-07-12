package com.rz.manuscript.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient(url = "https://761v35k236.goho.co", name = "taskExecClient")
@FeignClient(url = "109.244.159.194:5001", name = "taskExecClient")
public interface TaskExecClient {
    @RequestMapping(value = "/scrapy", method = RequestMethod.POST)
    String exec(TaskExecRequest request);
}
