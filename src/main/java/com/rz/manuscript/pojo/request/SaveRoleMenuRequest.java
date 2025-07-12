package com.rz.manuscript.pojo.request;

import lombok.Data;

import java.util.List;

@Data
public class SaveRoleMenuRequest {
    private  Integer roleId;
    private List<Integer> menuIds;
}
