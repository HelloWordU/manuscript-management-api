package com.rz.manuscript.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectVo {
    private Integer id;

    private String name;

    private String beginDt;

    private String endDt;

    private String planNumber;

    private String publishNumber;

    private String orderNumber;

}
