package com.rz.manuscript.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class SentenceVo {
    private String id;
    private String content;
    private List<String> tags;
    private List<String> categories;
}
