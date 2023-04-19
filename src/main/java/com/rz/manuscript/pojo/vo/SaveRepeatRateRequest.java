package com.rz.manuscript.pojo.vo;

import lombok.Data;

@Data
public class SaveRepeatRateRequest {
    private Integer id;
    private Double repeatRate;

    private Integer maxRepeatRateManuscriptId;
}
