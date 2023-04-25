package com.rz.manuscript.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.entity.SelectTopic;
import com.rz.manuscript.pojo.vo.SelectTopicGetListRequest;
import com.rz.manuscript.pojo.vo.SelectTopicVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISelectTopicService extends IService<SelectTopic> {
    List<SelectTopicVo> getList(SelectTopicGetListRequest request);
}
