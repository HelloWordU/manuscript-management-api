package com.rz.manuscript.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class SaveGroupUserRequest {
    private Integer groupId;
    private List<Integer> userIds;
}
