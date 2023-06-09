package com.rz.manuscript.controller;

import com.rz.manuscript.common.ResultEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {
//    @RequestMapping("/error")
//    public ResultEntity<Boolean> error()
//    {
//        return  new ResultEntity<>(200,"未知的异常");
//    }
    @GetMapping("/noAuthError")
    public ResultEntity<Boolean> noAuthError()
    {
        return  new ResultEntity<>(401,"用户未登录");
    }
}
