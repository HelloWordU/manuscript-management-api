package com.rz.manuscript.pojo.vo;

import lombok.Data;

@Data
public class RoleMenuMappingVo {

    private Integer id;

    private Integer roleId;

    private Integer menuId;

    private String roleName;

    private String menuName;
}
