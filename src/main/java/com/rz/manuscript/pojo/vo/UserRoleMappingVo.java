package com.rz.manuscript.pojo.vo;

import lombok.Data;

@Data
public class UserRoleMappingVo {
    private Integer id;

    private Integer roleId;

    private String roleName;

    private Integer userId;

    private String userRealName;
}
