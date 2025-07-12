package com.rz.manuscript.pojo.request;

import lombok.Data;

import java.util.List;

@Data
public class SaveRoleUserRequest {
    private  Integer roleId;
    private List<Integer> userIds;
}
