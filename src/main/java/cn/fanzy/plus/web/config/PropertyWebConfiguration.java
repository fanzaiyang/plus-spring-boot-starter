package cn.fanzy.plus.web.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

/**
 * 商业网络配置
 *
 * @author fanzaiyang
 * @since 2021/10/21
 */
@Configuration
@Slf4j
@PropertySource(value = {"classpath:application-plusweb.properties"})
public class PropertyWebConfiguration {
    /**
     * 配置检查
     */
    @PostConstruct
    public void checkConfig() {
        log.debug("【Plus组件】: 开启 <自定义配置文件> 相关的配置");
    }
}
