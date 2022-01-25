package com.example.demo.controller;

import cn.fanzy.ultra.annotation.PathRestController;
import cn.fanzy.ultra.model.Json;
import com.example.demo.args.DemoArgs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"测试"})
@Slf4j
@PathRestController("/demo")
public class DemoController {


    @ApiOperation(value = "请求参数")
    @PostMapping("/params")
    public Json<DemoArgs> testParams(@RequestBody DemoArgs args, HttpServletRequest request){
        return Json.success(args);
    }
}
