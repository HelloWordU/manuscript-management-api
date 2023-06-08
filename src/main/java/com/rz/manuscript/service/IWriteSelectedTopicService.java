package com.rz.manuscript.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.entity.WriteSelectedTopic;
import com.rz.manuscript.pojo.request.GetContentKeysRequest;
import com.rz.manuscript.pojo.vo.WriteSelectedTopicGetListRequest;
import com.rz.manuscript.pojo.vo.WriteSelectedTopicVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWriteSelectedTopicService extends IService<WriteSelectedTopic> {
    List<WriteSelectedTopicVo> getList(WriteSelectedTopicGetListRequest request);
    WriteSelectedTopicVo getById(int id);

    List<String> getContentKeys(GetContentKeysRequest request);
}
