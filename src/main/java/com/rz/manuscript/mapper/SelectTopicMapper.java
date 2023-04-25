package com.rz.manuscript.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rz.manuscript.entity.SelectTopic;
import com.rz.manuscript.pojo.vo.SelectTopicGetListRequest;
import com.rz.manuscript.pojo.vo.SelectTopicVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SelectTopicMapper extends BaseMapper<SelectTopic> {
    List<SelectTopicVo> getList(SelectTopicGetListRequest request);
}
