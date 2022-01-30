package cn.fanzy.ultra.web.config;

import cn.fanzy.ultra.web.service.DefaultLogUserServiceImpl;
import cn.fanzy.ultra.web.service.LogUserService;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(AopLogConfiguration.class)
@ConditionalOnProperty(prefix = "plus.aop.log", name = {"enable"}, havingValue = "true", matchIfMissing = true)
public class LogUserConfig {
    @Bean
    @ConditionalOnMissingBean
    public LogUserService logUserService() {
        return new DefaultLogUserServiceImpl();
    }
}
