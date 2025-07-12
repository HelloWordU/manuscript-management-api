package com.rz.manuscript.pojo.request;

import lombok.Data;

@Data
public class ConfirmCompleteRequest {

    private Integer id;
    private Integer productId;
    private Integer typeId;
}
