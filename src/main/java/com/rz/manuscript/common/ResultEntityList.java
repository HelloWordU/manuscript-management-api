package com.rz.manuscript.common;

import lombok.Data;

import java.util.List;

@Data
public class ResultEntityList<T> {

    private List<T> data;
    private Integer code;
    private String message;
    private Long total;
    private Integer pageIndex;
    private Integer pageSize;


    public ResultEntityList() {
        this.code = 200;
    }

    public ResultEntityList(List<T> data) {
        this.code = 200;
        this.data = data;
    }

    public ResultEntityList(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResultEntityList(Integer code, List<T> data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public ResultEntityList(Integer code, List<T> data, String message,Long total) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.total = total;
    }
}
