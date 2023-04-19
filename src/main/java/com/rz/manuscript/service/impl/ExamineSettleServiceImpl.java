package com.rz.manuscript.service.impl;

import com.rz.manuscript.entity.ExamineSettle;
import com.rz.manuscript.mapper.ExamineSettleMapper;
import com.rz.manuscript.pojo.vo.ExamineSettleListRequest;
import com.rz.manuscript.pojo.vo.ExamineSettleVo;
import com.rz.manuscript.pojo.vo.OrderGetListRequest;
import com.rz.manuscript.service.IExamineSettleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-09-02
 */
@Service
public class ExamineSettleServiceImpl extends ServiceImpl<ExamineSettleMapper, ExamineSettle> implements IExamineSettleService {

    @Resource
    private ExamineSettleMapper examineSettleMapper;

    @Override
    public Long getListTotal(ExamineSettleListRequest request) {
        setRequestPage(request);
        return examineSettleMapper.getListTotal(request);
    }

    @Override
    public List<ExamineSettleVo> getList(ExamineSettleListRequest request) {
        setRequestPage(request);
        return examineSettleMapper.getList(request);
    }

    @Override
    public Long getSupplierListTotal(ExamineSettleListRequest request) {
        setRequestPage(request);
        return examineSettleMapper.getSupplierListTotal(request);
    }

    @Override
    public List<ExamineSettleVo> getSupplierList(ExamineSettleListRequest request) {
        setRequestPage(request);
        return examineSettleMapper.getSupplierList(request);
    }

    private void setRequestPage(ExamineSettleListRequest request) {
        if (request.getPageIndex() == null || request.getPageIndex() < 1)
            request.setPageIndex(1);
        if (request.getPageSize() == null || request.getPageSize() < 1)
            request.setPageSize(20);
        request.setStartIndex((request.getPageIndex()-1) * request.getPageSize());
    }
}
