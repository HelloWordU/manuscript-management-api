package com.rz.manuscript.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rz.manuscript.client.SentenceCenterClient;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.WriteSelectedTopic;
import com.rz.manuscript.mapper.WriteSelectedTopicMapper;
import com.rz.manuscript.pojo.request.AddSentenceRequest;
import com.rz.manuscript.pojo.request.GetContentKeysRequest;
import com.rz.manuscript.pojo.vo.WriteSelectedTopicGetListRequest;
import com.rz.manuscript.pojo.vo.WriteSelectedTopicVo;
import com.rz.manuscript.service.IWriteSelectedTopicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class WriteSelectedTopicServiceImpl extends ServiceImpl<WriteSelectedTopicMapper, WriteSelectedTopic> implements IWriteSelectedTopicService {
    @Resource
    private WriteSelectedTopicMapper writeSelectTopicMapper;

    @Resource
    private SentenceCenterClient sentenceCenterClient;


    @Override
    public List<WriteSelectedTopicVo> getList(WriteSelectedTopicGetListRequest request) {
        setRequestPage(request);
        return writeSelectTopicMapper.getList(request);
    }

    @Override
    public WriteSelectedTopicVo getById(int id) {
        return writeSelectTopicMapper.getVoById(id);
    }

    @Override
    public List<String> getContentKeys(GetContentKeysRequest request) {
        ResultEntityList<String> contentKeys = sentenceCenterClient.getContentKeys(request);
        if (contentKeys.getCode().equals(200)) {
            return contentKeys.getData();
        } else {
            log.error("getContentKeys res:" + JSON.toJSONString(contentKeys));
            return new ArrayList<>();
        }
    }

    private void setRequestPage(WriteSelectedTopicGetListRequest request) {
        if (request.getPageIndex() == null || request.getPageIndex() < 1)
            request.setPageIndex(1);
        if (request.getPageSize() == null || request.getPageSize() < 1)
            request.setPageSize(20);
        request.setStartIndex((request.getPageIndex() - 1) * request.getPageSize());
    }

}
