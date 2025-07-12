package com.rz.manuscript.client;

import lombok.Data;

@Data
public class TaskExecRequest {
    private String keyWord;
    private String taskId;
    private String projectId;
}
