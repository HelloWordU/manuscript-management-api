package com.rz.manuscript.pojo.request;

import lombok.Data;

import java.util.List;

@Data
public class QueryRoleMenuMappingVoRequest {
    private List<Integer> roleIds;
    private Integer roleId;
}
