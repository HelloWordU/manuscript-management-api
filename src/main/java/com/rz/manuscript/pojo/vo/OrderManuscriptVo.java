package com.rz.manuscript.pojo.vo;

import lombok.Data;

@Data
public class OrderManuscriptVo {
    private Integer id;

    private Integer orderId;

    private Integer manuscriptId;

    private String title;

    private Integer projectId;

    private String auth;

    private Integer typeId;

    private Integer productId;

    private Integer charCount;

    private String fileName;

    private String url;

    private String shotScreenUrl;

    private String savePath;
}
