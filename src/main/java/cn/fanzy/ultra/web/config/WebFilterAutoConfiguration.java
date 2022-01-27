package cn.fanzy.ultra.web.config;

import cn.fanzy.ultra.model.Json;
import cn.fanzy.ultra.utils.ErrorUtil;
import cn.fanzy.ultra.utils.HttpUtil;
import cn.fanzy.ultra.web.properties.WebFilterProperties;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.DispatcherServlet;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;

/**
 * web增强支持
 *
 * @author fanzaiyang
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass(DispatcherServlet.class)
@EnableConfigurationProperties({WebFilterProperties.class})
@ConditionalOnProperty(prefix = "plus.web", name = {"enable"}, havingValue = "true", matchIfMissing = true)
public class WebFilterAutoConfiguration {

    /**
     * 请求跟踪拦截器用于增加一个请求追踪标志
     *
     * @return Filter
     */
    @Bean("requestTrackingFilter")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnMissingBean(name = "requestTrackingFilter")
    public Filter requestTrackingFilter(WebFilterProperties webProperties) {
        return (request, response, chain) -> {
            String ssid = IdUtil.fastSimpleUUID();
            request.setAttribute(webProperties.getSsidName(), ssid);
            try {
                chain.doFilter(request, response);
            } catch (Exception e) {
                log.error("发生系统异常！", e);
                HttpUtil.out((HttpServletResponse) response, Json.error(ErrorUtil.getErrorMsg(e)).setData(e.getCause()));
            }

        };
    }

    /**
     * 配置检查
     */
    @PostConstruct
    public void checkConfig() {

        log.debug("【Plus组件】: 开启 <web增强支持> 相关的配置");
    }
}
