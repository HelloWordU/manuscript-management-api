package com.rz.manuscript.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVo {
    private Integer id;

    private String name;

    private String realName;

    private String password;

    private Date createTime;

    private Date updateTime;

    private String groupName;
}
