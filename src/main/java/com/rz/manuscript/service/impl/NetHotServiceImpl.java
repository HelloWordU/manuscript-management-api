package com.rz.manuscript.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rz.manuscript.client.NetHotRefreshClient;
import com.rz.manuscript.client.NetHotRefreshResEntity;
import com.rz.manuscript.entity.NetHot;
import com.rz.manuscript.mapper.NetHotMapper;
import com.rz.manuscript.service.INetHotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Ario
 * @create 2023/4/24
 */
@Service
@Slf4j
public class NetHotServiceImpl extends ServiceImpl<NetHotMapper, NetHot> implements INetHotService {
    @Autowired
    private NetHotRefreshClient netHotRefreshClient;

    @Scheduled(fixedDelay=10*60*1000)
    public void refreshNetHot() {
        log.error("开始执行网热热词刷新");
        NetHotRefreshResEntity netHotRefreshResEntity;
        try {
            netHotRefreshResEntity = JSON.parseObject(netHotRefreshClient.baiduHot(), NetHotRefreshResEntity.class);
            if (netHotRefreshResEntity != null && !netHotRefreshResEntity.getCode().equals(200)) {
                log.error("网热热词刷新异常:" + JSON.toJSONString(netHotRefreshResEntity));
            }
        } catch (Exception e) {
            log.error("网热热词刷新异常", e);
        }
        try {

            netHotRefreshResEntity = JSON.parseObject(netHotRefreshClient.toutiaoHot(), NetHotRefreshResEntity.class);
            if (netHotRefreshResEntity != null && !netHotRefreshResEntity.getCode().equals(200)) {
                log.error("网热热词刷新异常:" + JSON.toJSONString(netHotRefreshResEntity));
            }
        } catch (Exception e) {
            log.error("网热热词刷新异常", e);
        }
        try {
            netHotRefreshResEntity = JSON.parseObject(netHotRefreshClient.weiboHot(), NetHotRefreshResEntity.class);
            if (netHotRefreshResEntity != null && !netHotRefreshResEntity.getCode().equals(200)) {
                log.error("网热热词刷新异常:" + JSON.toJSONString(netHotRefreshResEntity));
            }
        } catch (Exception e) {
            log.error("网热热词刷新异常", e);
        }
    }


}
