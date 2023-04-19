package com.rz.manuscript.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum CheckStateEnum {
    //  0 未提交 1 提交待审核  2审核通过 3未通过审核
    未提交(0, "未提交"),
    提交待审核(1, "提交待审核"),
    审核通过(2, "审核通过"),
    未通过审核(3, "未通过审核");
    private int code;
    private String value;

    CheckStateEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // 方式一、每次取枚举用for循环遍历
    public static CheckStateEnum getByCode(int code) {
        for (CheckStateEnum it : CheckStateEnum.values()) {
            if (it.getCode() == code) {
                return it;
            }
        }
        return null;
    }

    // 方式二、放入map中，通过键取值
    private static Map<Integer, CheckStateEnum> zyMap = new HashMap<>();

    static {
        for (CheckStateEnum value : CheckStateEnum.values()) {
            zyMap.put(value.getCode(), value);
        }
    }

    public static CheckStateEnum getByCode(Integer code) {
        return zyMap.get(code);
    }
}
