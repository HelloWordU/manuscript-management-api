package com.rz.manuscript;

import com.rz.manuscript.pojo.vo.MonitoringPlantPageDataVo;
import com.rz.manuscript.service.IMonitoringPlantformStatisticService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ManuscriptApplicationTests {

    @Resource
    private IMonitoringPlantformStatisticService iMonitoringPlantformStatisticService;
    @Test
    void contextLoads() {
    }

    @Test
    void  getPageDataByCategoryIdTest()
    {
        MonitoringPlantPageDataVo pageDataByCategoryId = iMonitoringPlantformStatisticService.getPageDataByCategoryId(1);
        String info = "";
    }

}
