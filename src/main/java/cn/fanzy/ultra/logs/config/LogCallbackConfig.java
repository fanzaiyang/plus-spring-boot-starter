package cn.fanzy.ultra.logs.config;

import cn.fanzy.ultra.utils.LogOutUtil;
import cn.fanzy.ultra.logs.properties.AopLogProperties;
import cn.fanzy.ultra.logs.service.LogCallbackService;
import cn.fanzy.ultra.logs.service.LogUserService;
import lombok.extern.slf4j.Slf4j;
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
public class LogCallbackConfig {

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
