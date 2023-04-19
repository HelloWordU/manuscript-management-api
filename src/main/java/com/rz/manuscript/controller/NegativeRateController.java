package com.rz.manuscript.controller;

import com.rz.manuscript.common.ResultEntity;
import com.rz.manuscript.common.ResultEntityList;
import com.rz.manuscript.entity.NegativeRate;
import com.rz.manuscript.pojo.vo.NegativeRateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/negativeRate")
@Api(tags = "负面率统计")
public class NegativeRateController {
    @Resource
    private HttpServletRequest request;

    Random random = new Random();

    @ApiOperation(value = "获取全网负面率", httpMethod = "GET")
    @GetMapping("/getAll")
    public ResultEntityList<NegativeRate> getAll() {
//        HttpSession session = request.getSession();
//        Integer userId = Integer.getInteger(session.getAttribute("userId").toString());
//        Integer companyId = CurrentLoginUserManager.currentLoginUser.get(userId).getCompanyId();
        List<NegativeRate> data = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int i = c.get(Calendar.HOUR_OF_DAY);
        int j = i;
        int totalCount = 0;
        for (; i >= 0; i--) {
            NegativeRate negativeRate = new NegativeRate();
            negativeRate.setKey(i + "时");
            negativeRate.setValue(randomState());
            data.add(negativeRate);
            totalCount++;
        }
        if (totalCount < 24) {
            for (int m = 1; m < 24 - j; m++) {
                NegativeRate negativeRate = new NegativeRate();
                negativeRate.setKey((24 - m) + "时");
                negativeRate.setValue(randomState());
                data.add(negativeRate);
                totalCount++;
            }
        }
        Collections.reverse(data);
        return new ResultEntityList<>(200, data, "获取成功");
    }


    @ApiOperation(value = "获取五大门户头条负面率", httpMethod = "GET")
    @GetMapping("/getGateWay")
    public ResultEntityList<NegativeRate> getGateWay() {
//        HttpSession session = request.getSession();
//        Integer userId = Integer.getInteger(session.getAttribute("userId").toString());
//        Integer companyId = CurrentLoginUserManager.currentLoginUser.get(userId).getCompanyId();
        List<NegativeRate> data = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int i = c.get(Calendar.HOUR_OF_DAY);
        int j = i;
        int totalCount = 0;
        for (; i >= 0; i--) {
            NegativeRate negativeRate = new NegativeRate();
            negativeRate.setKey(i + "时");
            negativeRate.setValue(randomState());
            data.add(negativeRate);
            totalCount++;
        }
        if (totalCount < 24) {
            for (int m = 1; m < 24 - j; m++) {
                NegativeRate negativeRate = new NegativeRate();
                negativeRate.setKey((24 - m) + "时");
                negativeRate.setValue(randomState());
                data.add(negativeRate);
                totalCount++;
            }
        }
        Collections.reverse(data);
        return new ResultEntityList<>(200, data, "获取成功");
    }

    @ApiOperation(value = "获取五大门户头条负面率(折线图)", httpMethod = "GET")
    @GetMapping("/getGateWayRate")
    public ResultEntity<NegativeRateVo> getGateWayRate() {
//        HttpSession session = request.getSession();
//        Integer userId = Integer.getInteger(session.getAttribute("userId").toString());
//        Integer companyId = CurrentLoginUserManager.currentLoginUser.get(userId).getCompanyId();
        NegativeRateVo res = new NegativeRateVo();
        List<NegativeRate> data = getNegativeRates();
        res.setTimeData(data.stream().map(item->item.getKey()).collect(Collectors.toList()));
        res.setFhwData(data.stream().map(item->item.getValue()).collect(Collectors.toList()));
        data = getNegativeRates();
        res.setShData(data.stream().map(item->item.getValue()).collect(Collectors.toList()));

        data = getNegativeRates();
        res.setTtData(data.stream().map(item->item.getValue()).collect(Collectors.toList()));

        data = getNegativeRates();
        res.setTxData(data.stream().map(item->item.getValue()).collect(Collectors.toList()));

        data = getNegativeRates();
        res.setWyData(data.stream().map(item->item.getValue()).collect(Collectors.toList()));

        data = getNegativeRates();
        res.setXlData(data.stream().map(item->item.getValue()).collect(Collectors.toList()));

        return new ResultEntity<>(200, res, "获取成功");
    }

    private List<NegativeRate> getNegativeRates() {
        List<NegativeRate> data = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int i = c.get(Calendar.HOUR_OF_DAY);
        int j = i;
        int totalCount = 0;
        for (; i >= 0; i--) {
            NegativeRate negativeRate = new NegativeRate();
            negativeRate.setKey(i + "时");
            negativeRate.setValue(random.nextInt(10) );
            data.add(negativeRate);
            totalCount++;
        }
        if (totalCount < 24) {
            for (int m = 1; m < 24 - j; m++) {
                NegativeRate negativeRate = new NegativeRate();
                negativeRate.setKey((24 - m) + "时");
                negativeRate.setValue(random.nextInt(10) );
                data.add(negativeRate);
                totalCount++;
            }
        }
        Collections.reverse(data);
        return data;
    }


    private Integer randomState() {
        return random.nextInt(2);
    }
}
