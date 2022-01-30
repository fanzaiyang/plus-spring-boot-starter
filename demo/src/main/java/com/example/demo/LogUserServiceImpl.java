package com.example.demo;

import cn.fanzy.ultra.web.service.LogUserService;

//@Service
public class LogUserServiceImpl implements LogUserService {
    @Override
    public String getCurrentUser() {
        return "Test";
    }
}
