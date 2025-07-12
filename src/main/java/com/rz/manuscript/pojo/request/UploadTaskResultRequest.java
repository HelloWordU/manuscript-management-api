package com.rz.manuscript.pojo.request;

import lombok.Data;

@Data
public class UploadTaskResultRequest {
    private Integer taskId;
    private Boolean isOk;
    private String msg;
}
