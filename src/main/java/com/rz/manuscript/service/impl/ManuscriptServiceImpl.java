package com.rz.manuscript.service.impl;

import com.rz.manuscript.client.RepeatRateClient;
import com.rz.manuscript.entity.Manuscript;
import com.rz.manuscript.entity.Project;
import com.rz.manuscript.mapper.ManuscriptMapper;
import com.rz.manuscript.mapper.MonitoringPlantformArticleMapper;
import com.rz.manuscript.pojo.vo.CalcRateRequest;
import com.rz.manuscript.pojo.vo.GetManuscriptRequest;
import com.rz.manuscript.pojo.vo.ManuscriptVo;
import com.rz.manuscript.service.IManuscriptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rz.manuscript.service.IProjectService;
import lombok.Synchronized;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-05
 */
@Service
public class ManuscriptServiceImpl extends ServiceImpl<ManuscriptMapper, Manuscript> implements IManuscriptService {

    @Resource
    private ManuscriptMapper manuscriptMapper;

    @Resource
    private RepeatRateClient repeatRateClient;

    @Resource
    private IProjectService iProjectService;

    @Override
    public List<ManuscriptVo> getList(GetManuscriptRequest request) {
        setRequestPage(request);
        return manuscriptMapper.getList(request);
    }

    @Override
    public List<ManuscriptVo> getCustomerList(GetManuscriptRequest request) {
        setRequestPage(request);
        return manuscriptMapper.getCustomerList(request);
    }

    @Override
    public Integer getTotal(GetManuscriptRequest request) {
        setRequestPage(request);
        return manuscriptMapper.getTotal(request);
    }

    private void setRequestPage(GetManuscriptRequest request) {
        if (request.getPageIndex() == null || request.getPageIndex() < 1)
            request.setPageIndex(1);
        if (request.getPageSize() == null || request.getPageSize() < 1)
            request.setPageSize(20);
        request.setStartIndex((request.getPageIndex()-1) * request.getPageSize());
    }

    @Override
    public Integer getCustomerListTotal(GetManuscriptRequest request) {
        setRequestPage(request);
        return manuscriptMapper.getCustomerListTotal(request);
    }
    @Override
    public void calcRate(Manuscript entity) {
        CalcRateRequest request = new CalcRateRequest();
        request.setContext(entity.getContent());
        request.setManuscriptId(entity.getId());
        repeatRateClient.calcRate(request);
    }
    @Override
    @Synchronized
    public void updateManuscriptCount(Project byId, Boolean isAdd) {
        if (byId != null) {
            if (isAdd) {
                byId.setManuscriptUploadCount(byId.getManuscriptUploadCount() + 1);
            } else {
                byId.setManuscriptUploadCount(byId.getManuscriptUploadCount() - 1);
            }
            iProjectService.updateById(byId);
        }

    }
}
