package com.rz.manuscript.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class NegativeRateVo {
    private List<Integer> wyData;
    private List<Integer> ttData;
    private List<Integer> txData;
    private List<Integer> xlData;
    private List<Integer> shData;
    private List<Integer> fhwData;
    private List<String> timeData;
}
