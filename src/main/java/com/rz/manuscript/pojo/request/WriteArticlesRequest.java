package com.rz.manuscript.pojo.request;

import lombok.Data;

@Data
public class WriteArticlesRequest {
    private String topic;
    private Integer charCount;
    private String keys;

}
