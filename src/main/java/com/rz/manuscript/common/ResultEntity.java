package com.rz.manuscript.common;

import lombok.Data;

@Data
public class ResultEntity<T> {

    private T data;
    private Integer code;
    private String message;

    public ResultEntity() {
        this.code = 200;
    }

    public ResultEntity(T data) {
        this.code = 200;
        this.data = data;
    }
    public ResultEntity(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public ResultEntity(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
