package com.rz.manuscript.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class SaveGroupProjectRequest {
    private Integer groupId;
    private List<Integer> projectIds;
}
