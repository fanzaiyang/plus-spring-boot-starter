package com.example.demo;

import cn.fanzy.plus.logs.service.LogUserService;

//@Service
public class LogUserServiceImpl implements LogUserService {
    @Override
    public String getCurrentUser() {
        return "Test";
    }
}
