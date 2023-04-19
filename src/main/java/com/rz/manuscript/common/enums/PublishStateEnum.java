package com.rz.manuscript.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum PublishStateEnum {
    //0 未发布 1 提交订单  2 已发布 3 已确认发布
    未发布(0, "未发布"),
    提交订单(1, "提交订单"),
    已发布(2, "已发布"),
    已确认发布(3, "已确认发布");
    private int code;
    private String value;
    PublishStateEnum(int code, String value) {
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
    public static PublishStateEnum getByCode(int code) {
        for (PublishStateEnum it : PublishStateEnum.values()) {
            if (it.getCode() == code) {
                return it;
            }
        }
        return null;
    }
    // 方式二、放入map中，通过键取值
    private static Map<Integer,PublishStateEnum > zyMap = new HashMap<>();
    static {
        for (PublishStateEnum value : PublishStateEnum .values()) {
            zyMap.put(value.getCode(),value);
        }
    }
    public static PublishStateEnum getByCode(Integer code){
        return zyMap.get(code);
    }
}
