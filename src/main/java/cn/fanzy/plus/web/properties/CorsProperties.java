/**
 *
 */
package cn.fanzy.plus.web.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 跨域支持属性配置
 *
 * @author fanzaiyang
 * @since 2021/09/06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "plus.cors")
public class CorsProperties {
    /**
     * 是否开启跨域支持,默认开启
     */
    private Boolean enable = true;
    /**
     * 跨域设置允许的路径，默认为所有路径
     */
    private String url = "/**";
    /**
     * 跨域设置允许的Origins，默认为所有
     */
    private String allowedOrigins = "*";
    /**
     * 跨域设置允许的请求方法，默认为所有
     */
    private String allowedMethods = "*";
    /**
     * 跨域设置允许的请求头，默认为所有
     */
    private String allowedHeaders = "*";
    /**
     * 跨域设置是否允许携带凭据，默认为true
     */
    private Boolean allowCredentials = true;

}
