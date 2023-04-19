package com.rz.manuscript.service;

import com.rz.manuscript.entity.ExamineSettle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.pojo.vo.ExamineSettleListRequest;
import com.rz.manuscript.pojo.vo.ExamineSettleVo;
import com.rz.manuscript.pojo.vo.OrderVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-09-02
 */
public interface IExamineSettleService extends IService<ExamineSettle> {

    Long getListTotal(ExamineSettleListRequest request);

    List<ExamineSettleVo> getList(ExamineSettleListRequest request);

    Long getSupplierListTotal(ExamineSettleListRequest requestParam);

    List<ExamineSettleVo> getSupplierList(ExamineSettleListRequest requestParam);
}
