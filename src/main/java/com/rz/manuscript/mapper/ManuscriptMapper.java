package com.rz.manuscript.mapper;

import com.rz.manuscript.entity.Manuscript;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rz.manuscript.pojo.vo.GetManuscriptRequest;
import com.rz.manuscript.pojo.vo.ManuscriptVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-08-05
 */
@Mapper
public interface ManuscriptMapper extends BaseMapper<Manuscript> {

    List<ManuscriptVo> getList(GetManuscriptRequest request);

    List<ManuscriptVo> getCustomerList(GetManuscriptRequest request);

    Integer getTotal(GetManuscriptRequest request);

    Integer getCustomerListTotal(GetManuscriptRequest request);
}
