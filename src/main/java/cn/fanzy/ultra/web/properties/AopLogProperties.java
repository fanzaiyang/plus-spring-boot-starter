package cn.fanzy.ultra.web.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * Aop模块配置项参数
 *
 * @author fanzaiyang
 * @since 2021/09/06
 */
@ConfigurationProperties(prefix = "plus.aop.log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AopLogProperties {

    /**
     * 是否开启全局参数校验拦截,默认为true
     */
    private Boolean enable = true;

}
