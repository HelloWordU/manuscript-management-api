package com.rz.manuscript.service;

import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rz.manuscript.pojo.request.BatchAddPictureRequest;

/**
 * <p>
 * 图片 服务类
 * </p>
 *
 * @author baomidou
 * @since 2023-05-25
 */
public interface IPictureService extends IService<Picture> {

    ResultEntity<Boolean> batchAdd(BatchAddPictureRequest batchAddPictureRequest);
}
