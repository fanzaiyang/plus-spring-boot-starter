package cn.fanzy.ultra.web.config;

import cn.fanzy.ultra.web.properties.AopLogProperties;
import cn.fanzy.ultra.web.service.LogCallbackService;
import cn.fanzy.ultra.web.service.LogOutService;
import cn.fanzy.ultra.web.service.LogUserService;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
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
    private final String format = "\n->IP【{}】用户【{}】访问【{}】执行【{}】({})\n    请求【{}】\n    响应【{}】\n    耗时【{}】秒（{}～{}）";

    @Bean
    @ConditionalOnMissingBean
    public LogUserService logUserService() {
        return () -> "未登录用户";
    }

    @Bean
    @ConditionalOnMissingBean
    public LogOutService logOutService() {
        return (ip, user, url, method, swaggerName, requestData, responseData, start, end, second) -> {
            log.info(format, ip, user, url, swaggerName, method,
                    requestData, responseData,
                    second,
                    DateUtil.format(start, DatePattern.NORM_DATETIME_MS_PATTERN), DateUtil.format(end, DatePattern.NORM_DATETIME_MS_PATTERN)
                    );
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public LogCallbackService logCallbackService() {
        return (ip, user, url, method, swaggerName, requestData, responseData, start, end, second) -> {
        };
    }

}
