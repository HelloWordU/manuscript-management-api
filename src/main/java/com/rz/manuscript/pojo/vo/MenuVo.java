package com.rz.manuscript.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class MenuVo {
    private Integer id;

    private String name;

    private String url;

    private Integer parentId;

    private String ico;

    private Integer sort;

    private List<MenuVo> child;
}
