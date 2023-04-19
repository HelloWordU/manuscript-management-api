package com.rz.manuscript.pojo.vo;

import lombok.Data;

@Data
public class ModifyPwdRequestVO {
    private String passWord;
    private String newPassWord;
    private String newPassWordConfirm;
}
