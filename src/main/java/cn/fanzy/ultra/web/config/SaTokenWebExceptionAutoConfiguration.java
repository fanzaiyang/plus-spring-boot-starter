package cn.fanzy.ultra.web.config;

import cn.dev33.satoken.exception.*;
import cn.fanzy.ultra.model.Json;
import cn.fanzy.ultra.web.properties.WebFilterProperties;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


/**
 * 全局异常捕获自动配置
 * 兼容Sa-Token
 * @author fanzaiyang
 * @since 2021/09/06
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@ResponseBody
@ConditionalOnClass(SaTokenException.class)
public class SaTokenWebExceptionAutoConfiguration {
    @Autowired
    private WebFilterProperties webProperties;


    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotLoginException.class)
    public Object handleRuntimeException(HttpServletRequest request, NotLoginException e) {
        String ssid = this.getRequestId(request);
        Json<Object> response = new Json<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()).setId(ssid);
        log.error(StrUtil.format("【公共组件】请求{},请求失败,拦截到运行时异常：{}", ssid, e.getMessage()), e);
        return response;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DisableLoginException.class)
    public Object handleRuntimeException(HttpServletRequest request, DisableLoginException e) {
        String ssid = this.getRequestId(request);
        Json<Object> response = new Json<>(HttpStatus.UNAUTHORIZED.value(), e.getMessage()).setId(ssid);
        log.error(StrUtil.format("【公共组件】请求{},请求失败,拦截到运行时异常：{}", ssid, e.getMessage()), e);
        return response;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotRoleException.class)
    public Object handleRuntimeException(HttpServletRequest request, NotRoleException e) {
        String ssid = this.getRequestId(request);
        Json<Object> response = new Json<>(HttpStatus.FORBIDDEN.value(), e.getMessage()).setId(ssid);
        log.error(StrUtil.format("【公共组件】请求{},请求失败,拦截到运行时异常：{}", ssid, e.getMessage()), e);
        return response;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotPermissionException.class)
    public Object handleRuntimeException(HttpServletRequest request, NotPermissionException e) {
        String ssid = this.getRequestId(request);
        Json<Object> response = new Json<>(HttpStatus.FORBIDDEN.value(), e.getMessage()).setId(ssid);
        log.error(StrUtil.format("【公共组件】请求{},请求失败,拦截到运行时异常：{}", ssid, e.getMessage()), e);
        return response;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotSafeException.class)
    public Object handleRuntimeException(HttpServletRequest request, NotSafeException e) {
        String ssid = this.getRequestId(request);
        Json<Object> response = new Json<>(HttpStatus.PRECONDITION_REQUIRED.value(), e.getMessage()).setId(ssid);
        log.error(StrUtil.format("【公共组件】请求{},请求失败,拦截到运行时异常：{}", ssid, e.getMessage()), e);
        return response;
    }

    /**
     * 获取请求的id
     *
     * @param request HttpServletRequest
     * @return 请求的ID
     */
    private String getRequestId(HttpServletRequest request) {
        String ssid = (String) request.getAttribute(webProperties.getSsidName());
        return StrUtil.isBlank(ssid) ? UUID.randomUUID().toString() : ssid;
    }

    @PostConstruct
    public void checkConfig() {
        log.debug("【公共组件】: 开启 <全局鉴权异常拦截> 相关的配置");
    }

}