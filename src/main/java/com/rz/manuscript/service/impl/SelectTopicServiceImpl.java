package com.rz.manuscript.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rz.manuscript.entity.SelectTopic;
import com.rz.manuscript.mapper.SelectTopicMapper;
import com.rz.manuscript.pojo.vo.SelectTopicGetListRequest;
import com.rz.manuscript.pojo.vo.SelectTopicVo;
import com.rz.manuscript.service.ISelectTopicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SelectTopicServiceImpl extends ServiceImpl<SelectTopicMapper, SelectTopic> implements ISelectTopicService {
    @Resource
    private SelectTopicMapper selectTopicMapper;


    @Override
    public List<SelectTopicVo> getList(SelectTopicGetListRequest request) {
        setRequestPage(request);
        return selectTopicMapper.getList(request);
    }

    private void setRequestPage(SelectTopicGetListRequest request) {
        if (request.getPageIndex() == null || request.getPageIndex() < 1)
            request.setPageIndex(1);
        if (request.getPageSize() == null || request.getPageSize() < 1)
            request.setPageSize(20);
        request.setStartIndex((request.getPageIndex() - 1) * request.getPageSize());
    }
}
