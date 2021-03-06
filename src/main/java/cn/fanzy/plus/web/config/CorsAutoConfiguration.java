/**
 *
 */
package cn.fanzy.plus.web.config;

import cn.fanzy.plus.web.properties.CorsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;


/**
 * 跨域支持自动配置
 *
 * @author fanzaiyang
 * @since 2021/09/06
 */
@Configuration
@EnableConfigurationProperties({CorsProperties.class})
@ConditionalOnProperty(prefix = "plus.cors", name = {"enable"}, havingValue = "true", matchIfMissing = true)
@Slf4j
public class CorsAutoConfiguration {

    @Autowired
    private CorsProperties corsProperties;

    /**
     * 配置跨域支持
     *
     * @return WebMvcConfigurer
     */
    @Bean("corsAllowedConfigurer")
    @ConditionalOnMissingBean(name = "corsAllowedConfigurer")
    public WebMvcConfigurer corsAllowedConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping(corsProperties.getUrl())
                        .allowedOriginPatterns(corsProperties.getAllowedOrigins())
                        .allowedMethods(corsProperties.getAllowedMethods())
                        .allowedHeaders(corsProperties.getAllowedHeaders())
                        .allowCredentials(corsProperties.getAllowCredentials());
            }
        };
    }

    /**
     * 注入一个跨域支持过滤器
     *
     * @since SpringBoot 2.4.0
     * @return 跨域支持过滤器
     */
    @Bean("corsAllowedFilter")
    @ConditionalOnMissingBean(name = "corsAllowedFilter")
    public CorsFilter corsAllowedFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader(corsProperties.getAllowedHeaders());
        corsConfiguration.addAllowedMethod(corsProperties.getAllowedMethods());
        corsConfiguration.addAllowedOriginPattern(corsProperties.getAllowedOrigins());
        source.registerCorsConfiguration(corsProperties.getUrl(), corsConfiguration);
        return new CorsFilter(source);
    }


    /**
     * 配置检查
     */
    @PostConstruct
    public void checkConfig() {

        log.debug("【Plus组件】: 开启 <跨域支持> 相关的配置");
    }

}
