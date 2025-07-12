package com.rz.manuscript.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rz.manuscript.common.CacheManager;
import com.rz.manuscript.common.Html2WordUtil;
import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.entity.Picture;
import com.rz.manuscript.mapper.PictureMapper;
import com.rz.manuscript.pojo.entity.CacheEntity;
import com.rz.manuscript.pojo.request.BatchAddPictureRequest;
import com.rz.manuscript.service.IPictureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 图片 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-05-25
 */
@Slf4j
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements IPictureService {

    @Override
    public ResultEntity<Boolean> batchAdd(BatchAddPictureRequest batchAddPictureRequest) {
        ResultEntity<Boolean> res = new ResultEntity<>();
        List<Picture> addList = new ArrayList<>();
        for (String imageUrl : batchAddPictureRequest.getImageUrls()
        ) {
            Picture item = new Picture();
            item.setTitle(batchAddPictureRequest.getTitle());
            item.setOrigin(batchAddPictureRequest.getOrigin());
            item.setImageUrl(imageUrl);
            item.setCreateTime(new Date());
            item.setIsDownload(false);
            item.setSearchKey(batchAddPictureRequest.getSearchKey());
            item.setProjectId(batchAddPictureRequest.getProjectId());
            item.setSourceUrl(batchAddPictureRequest.getSourceUrl());
            addList.add(item);
        }
        this.saveBatch(addList);
        return res;
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void getPictureData() {
        String key = "getPictureData";
        CacheEntity cacheInfo = CacheManager.getCacheInfo(key);
        if (cacheInfo != null && cacheInfo.getValue() != null) {
            log.info("有正在执行的图片获取任务，当前定时任务不执行");
            return;
        }
        cacheInfo = new CacheEntity();
        cacheInfo.setKey(key);
        cacheInfo.setTimeOut(24 * 60 * 60 * 1000);
        cacheInfo.setValue(true);
        CacheManager.putCache(key, cacheInfo);
        log.info("开始获取网络图片");
        Integer total = 0;
        Integer maxTotal = 1000;
        LambdaQueryWrapper<Picture> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Picture::getIsDownload, false);
        Page<Picture> page = new Page<>(1, 10);
        page = this.page(page, wrapper);
        while (!page.getRecords().isEmpty()) {
            List<Picture> records = page.getRecords();
            for (Picture p : records) {
                total++;
                log.debug("下载" + p.getImageUrl());
                String imageData = Html2WordUtil.getImageDataFromUrl(p.getImageUrl());
                if(imageData!=null && imageData.length()>0)
                {
                    p.setImageData(imageData);
                }
                p.setIsDownload(true);
                log.debug("下载完成" + p.getImageUrl());
            }
            this.updateBatchById(records);
            if (total > maxTotal)
                break;
            if (page.hasNext()) {
                page = new Page<>(page.getCurrent() + 1, 10);
                this.page(page, wrapper);
            }
        }
        CacheManager.clearOnly(key);
    }
}
