package cn.fanzy.ultra.web.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * web过滤器属性,web增强支持支持属性配置
 *
 * @author fanzaiyang
 * @since 2021/09/06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "plus.web")
public class WebFilterProperties {

    /**
     * 是否开启增强支持,默认开启
     */
    private Boolean enable = true;

    /**
     * 请求追踪标识符的名字
     */
    private String ssidName = "request-ssid";

}
