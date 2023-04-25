package com.rz.manuscript.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rz.manuscript.entity.WriteSelectedTopic;
import com.rz.manuscript.pojo.vo.WriteSelectedTopicGetListRequest;
import com.rz.manuscript.pojo.vo.WriteSelectedTopicVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WriteSelectedTopicMapper extends BaseMapper<WriteSelectedTopic> {
    List<WriteSelectedTopicVo> getList(WriteSelectedTopicGetListRequest request);

    WriteSelectedTopicVo getById(int id);
}
