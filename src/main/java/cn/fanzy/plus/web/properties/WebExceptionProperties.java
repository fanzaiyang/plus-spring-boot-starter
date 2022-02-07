package cn.fanzy.plus.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;


/**
 * 全局异常捕获属性配置
 *
 * @author fanzaiyang
 * @since  2021/09/06
 */
@Data
@ConfigurationProperties(prefix = "plus.error")
public class WebExceptionProperties {
    /**
     * 是否开启全局异常拦截功能，默认为开启
     */
    private Boolean enable = true;
    /**
     * 简单异常提示信息存储 <br/>
     * key：异常类型的名字，如 ConstraintViolationException <br/>
     * value：提示信息
     */
    private Map<String, String> map = new HashMap<>();
    /**
     * 完整异常信息提示<br/>
     * key：异常类型的完整名字，如 com.if.common.tool.exception.ServiceException
     * <br/>
     * value：提示信息，例如 用户信息不能为空
     */
    private Map<String, String> full = new HashMap<>();

}
