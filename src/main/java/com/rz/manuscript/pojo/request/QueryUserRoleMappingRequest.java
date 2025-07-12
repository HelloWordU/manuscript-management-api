package com.rz.manuscript.pojo.request;

import lombok.Data;

import java.util.List;

@Data
public class QueryUserRoleMappingRequest {
    private List<Integer> roleIds;
    private Integer roleId;
}
