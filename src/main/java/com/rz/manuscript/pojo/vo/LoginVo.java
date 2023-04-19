package com.rz.manuscript.pojo.vo;

import lombok.Data;

@Data
public class LoginVo {
    private String checkCode;
    private String userName;
    private String password;
    private String captchaToken;
    private Integer loginType;
}
