package com.rz.manuscript.client;

import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.pojo.vo.CalcRateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(url = "109.244.159.194:9900", name = "NetHotRefreshClient")
public interface NetHotRefreshClient {
    @RequestMapping(value = "/weibo/getHot/", method = RequestMethod.GET)
    String weiboHot();

    @RequestMapping(value = "/baidu/getHot/", method = RequestMethod.GET)
    String baiduHot();

    @RequestMapping(value = "/toutiao/getHot/", method = RequestMethod.GET)
    String toutiaoHot();
}
