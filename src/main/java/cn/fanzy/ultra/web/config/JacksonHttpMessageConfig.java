package cn.fanzy.ultra.web.config;

import cn.fanzy.ultra.web.properties.JsonProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Configuration
@EnableConfigurationProperties(JsonProperties.class)
@ConditionalOnProperty(prefix = "plus.json", name = {"enable"}, havingValue = "true", matchIfMissing = true)
public class JacksonHttpMessageConfig implements WebMvcConfigurer {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //调用父类的配置
        converters.add(0, new JacksonHttpMessageConverter());
    }

    /**
     * 配置检查
     */
    @PostConstruct
    public void checkConfig() {
        log.debug("【Plus组件】: 开启 <Json消息处理> 相关的配置");
    }
}
