package com.rz.manuscript.client;

import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.pojo.vo.CalcRateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "124.220.59.17:9100", name = "RepeatRateClient")
public interface RepeatRateClient {
    @RequestMapping(value = "/manuscriptRepeatRate/calcRate", method = RequestMethod.POST)
    ResultEntity<Boolean> calcRate(CalcRateRequest request);
}
