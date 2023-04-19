package com.rz.manuscript.common.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserTypeEnum {

    //0 未发布 1 提交订单  2 已发布 3 已确认发布
    员工(1, "员工"),
    供应商(2, "供应商"),
    客户(3, "客户");
    private int code;
    private String value;
    UserTypeEnum(int code, String value) {
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
    public static UserTypeEnum getByCode(int code) {
        for (UserTypeEnum it : UserTypeEnum.values()) {
            if (it.getCode() == code) {
                return it;
            }
        }
        return null;
    }
    // 方式二、放入map中，通过键取值
    private static Map<Integer,UserTypeEnum > zyMap = new HashMap<>();
    static {
        for (UserTypeEnum value : UserTypeEnum .values()) {
            zyMap.put(value.getCode(),value);
        }
    }
    public static UserTypeEnum getByCode(Integer code){
        return zyMap.get(code);
    }
}
