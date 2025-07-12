package com.rz.manuscript.service;

import com.rz.manuscript.entity.Manuscript;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.entity.Project;
import com.rz.manuscript.pojo.vo.GetManuscriptRequest;
import com.rz.manuscript.pojo.vo.ManuscriptVo;
import com.rz.manuscript.pojo.vo.MonitoringPlantformStatisticVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-08-05
 */
public interface IManuscriptService extends IService<Manuscript> {
    List<ManuscriptVo> getList(GetManuscriptRequest request);

    List<ManuscriptVo> getCustomerList(GetManuscriptRequest request);

    Integer getTotal(GetManuscriptRequest request);

    Integer getCustomerListTotal(GetManuscriptRequest request);

    void calcRate(Manuscript entity);

    void updateManuscriptCount(Project byId, Boolean isAdd);
}
