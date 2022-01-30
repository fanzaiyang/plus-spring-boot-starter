/**
 *
 */
package cn.fanzy.ultra.web.properties;

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
@ConfigurationProperties(prefix = "plus.json")
public class JsonProperties {
    /**
     * 是否开启跨域支持,默认开启
     */
    private Boolean enable = true;

}
