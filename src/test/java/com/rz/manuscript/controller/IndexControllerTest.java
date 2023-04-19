package com.rz.manuscript.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IndexControllerTest {

    @Resource
    private IndexController indexController;

    @Test
    public void  reCalcRateAllTest()
    {
        indexController.reCalcRateAll();
    }

}