package com.rz.manuscript;

import com.alibaba.fastjson.JSON;
import com.rz.manuscript.entity.NetHot;
import com.rz.manuscript.pojo.vo.MonitoringPlantPageDataVo;
import com.rz.manuscript.service.IMonitoringPlantformStatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.xml.bind.util.JAXBSource;
import java.util.Date;

@SpringBootTest
class ManuscriptApplicationTests {

    @Resource
    private IMonitoringPlantformStatisticService iMonitoringPlantformStatisticService;

    @Test
    void contextLoads() {
    }

    @Test
    void getPageDataByCategoryIdTest() {
        NetHot netHot = new NetHot();
        netHot.setLastModifyTime(new Date());
        String info = JSON.toJSONString(netHot);
        MonitoringPlantPageDataVo pageDataByCategoryId = iMonitoringPlantformStatisticService.getPageDataByCategoryId(1);
          info = "";
    }

}
