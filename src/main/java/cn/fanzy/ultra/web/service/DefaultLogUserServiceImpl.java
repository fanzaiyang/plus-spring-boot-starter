package cn.fanzy.ultra.web.service;

import org.springframework.stereotype.Component;

@Component
public class DefaultLogUserServiceImpl implements LogUserService{
    @Override
    public String getCurrentUser() {
        return "未登录的用户";
    }
}
