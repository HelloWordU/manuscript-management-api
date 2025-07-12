package com.rz.manuscript.pojo.request;

import lombok.Data;

@Data
public class SelectTopicSaveRequest {

    private Integer id;
    private  String title;
    private String content;
    private Integer charCount;
}
