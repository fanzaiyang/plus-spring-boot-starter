package com.example.demo.controller;

import cn.fanzy.ultra.annotation.PathRestController;
import cn.fanzy.ultra.model.Json;
import cn.fanzy.ultra.utils.HttpUtil;
import com.example.demo.args.DemoArgs;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(tags = {"测试"})
@ApiSupport(author = "范在阳",order = 1)
@Slf4j
@PathRestController("/demo")
public class DemoController {


    @ApiOperation(value = "请求参数")
    @ApiOperationSupport(order = 2)
    @PostMapping("/params")
    public Json<DemoArgs> testParams(@RequestBody DemoArgs args, HttpServletRequest request){
        return Json.success(args);
    }
    @ApiOperation(value = "请求参数2")
    @ApiOperationSupport(order = 8)
    @PostMapping("/params2")
    public Json<DemoArgs> testParams2(DemoArgs args){
        return Json.success(args);
    }
    @ApiOperation(value = "请求参数3")
    @ApiOperationSupport(order = 1,author = "张三")
    @GetMapping("/params3")
    public Json<DemoArgs> testParams3(DemoArgs args){
        return Json.success(args);
    }


    @GetMapping("/outweb")
    public void outWeb(String args, HttpServletResponse response){

        HttpUtil.outWeb(response,"<h1>Hello,Web!"+args+"</h1>");
    }

    @GetMapping("/go")
    public void redirect(String args, HttpServletResponse response){
        try {
            HttpUtil.redirect("https://www.baidu.com?form="+args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
