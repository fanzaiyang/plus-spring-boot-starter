package cn.fanzy.ultra.web.config;

import cn.fanzy.ultra.web.properties.AopLogProperties;
import cn.fanzy.ultra.web.service.LogCallbackService;
import cn.fanzy.ultra.utils.LogOutUtil;
import cn.fanzy.ultra.web.service.LogUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@AutoConfigureBefore(AopLogConfiguration.class)
@EnableConfigurationProperties(AopLogProperties.class)
@ConditionalOnProperty(prefix = "plus.aop.log", name = {"enable"}, havingValue = "true", matchIfMissing = true)
public class LogUserConfig {
    @Autowired
    private AopLogProperties aopLogProperties;

    @Bean
    @ConditionalOnMissingBean
    public LogUserService logUserService() {
        return () -> "未登录用户";
    }

    @Bean
    @ConditionalOnMissingBean
    public LogCallbackService logCallbackService() {
        return LogOutUtil::out;
    }

}
