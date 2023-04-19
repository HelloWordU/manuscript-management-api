package com.rz.manuscript.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum OrderStateEnum {
    //  0 未提交 1 提交待审核  2审核通过 3未通过审核
    未完成(0, "未完成"),
    已完成(1, "已完成");
    private int code;
    private String value;

    OrderStateEnum(int code, String value) {
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
    public static OrderStateEnum getByCode(int code) {
        for (OrderStateEnum it : OrderStateEnum.values()) {
            if (it.getCode() == code) {
                return it;
            }
        }
        return null;
    }

    // 方式二、放入map中，通过键取值
    private static Map<Integer, OrderStateEnum> zyMap = new HashMap<>();

    static {
        for (OrderStateEnum value : OrderStateEnum.values()) {
            zyMap.put(value.getCode(), value);
        }
    }

    public static OrderStateEnum getByCode(Integer code) {
        return zyMap.get(code);
    }
}
