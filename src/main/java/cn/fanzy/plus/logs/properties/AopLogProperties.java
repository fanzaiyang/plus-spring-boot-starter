package cn.fanzy.plus.logs.properties;

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

    /**
     * 日志输出的样式
     */
    private String format="\n->IP【{}】用户【{}】访问【{}】执行【{}】({})\n    请求{}【{}】\n    响应{}【{}】\n    耗时【{}】秒";

}
