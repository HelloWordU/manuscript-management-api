package com.rz.manuscript.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SelectTopicVo {
    private String title;
    private Integer charCount;
    private String productName;
    private String source;
    private Date publishTime;
}
