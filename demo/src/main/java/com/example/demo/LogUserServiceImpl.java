package com.example.demo;

import cn.fanzy.ultra.web.service.LogUserService;
import org.springframework.stereotype.Service;

@Service
public class LogUserServiceImpl implements LogUserService {
    @Override
    public String getCurrentUser() {
        return "Test";
    }
}
