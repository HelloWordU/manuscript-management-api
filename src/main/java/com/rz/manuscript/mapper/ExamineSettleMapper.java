package com.rz.manuscript.mapper;

import com.rz.manuscript.entity.ExamineSettle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rz.manuscript.pojo.vo.ExamineSettleListRequest;
import com.rz.manuscript.pojo.vo.ExamineSettleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-09-02
 */
@Mapper
public interface ExamineSettleMapper extends BaseMapper<ExamineSettle> {

    Long getListTotal(ExamineSettleListRequest request);

    List<ExamineSettleVo> getList(ExamineSettleListRequest request);

    Long getSupplierListTotal(ExamineSettleListRequest request);

    List<ExamineSettleVo> getSupplierList(ExamineSettleListRequest request);
}
