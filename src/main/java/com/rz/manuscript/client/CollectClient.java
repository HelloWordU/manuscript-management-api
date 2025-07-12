package com.rz.manuscript.client;

import com.rz.manuscript.pojo.request.CollectAnalysisRequest;
import com.rz.manuscript.pojo.vo.CollectResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "109.244.159.194:5000", name = "CollectClient")
public interface CollectClient {

    /**
     * 文章解析
     * @param request
     * @return
     */
    @RequestMapping(value = "/analysis", method = RequestMethod.POST)
    String analysis(CollectAnalysisRequest request);
}
