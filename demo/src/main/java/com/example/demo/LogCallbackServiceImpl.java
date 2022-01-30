package com.example.demo;

import cn.fanzy.ultra.web.service.LogCallbackService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
//@Service
public class LogCallbackServiceImpl implements LogCallbackService {

    @Override
    public void callback(String ip, String user, String url, String method, String swaggerName, String requestData, String responseData, long second) {
      log.info("执行回调。。。");
    }
}
