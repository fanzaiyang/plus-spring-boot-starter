package cn.fanzy.ultra.web.config;

import cn.fanzy.ultra.swagger.SwaggerProperties;
import cn.fanzy.ultra.web.properties.ValidProperties;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 全局参数校验功能自动配置
 *
 * @author fanzaiyang
 * @since 2021/09/06
 */
@Configuration
@Aspect
@EnableConfigurationProperties(ValidProperties.class)
@ConditionalOnProperty(prefix = "plus.aop", name = {"enable"}, havingValue = "true", matchIfMissing = true)
@Slf4j
public class AopLogConfiguration {

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.GetMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.PutMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void pointCut() {
    }

    /**
     * 执行环绕通知
     *
     * @param joinPoint JoinPoint
     */
    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        // 忽略swagger的日志
        AntPathMatcher matcher = new AntPathMatcher();
        List<String> list = SwaggerProperties.SWAGGER_LIST.stream().filter(item -> matcher.match(item, request.getRequestURI()))
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(list)) {
            return;
        }
        // ip地址
        String ipAddr = getRemoteHost(request);
        // 请求路径
        String requestUrl = request.getRequestURL().toString();

        // 获取请求参数进行打印
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;

        // 类名
        // swagger中文注释名
        Api api = methodSignature.getMethod().getDeclaringClass().getAnnotation(Api.class);
        String classCommentName = "";
        if (api != null) {
            classCommentName = api.tags()[0];
        }

        String[] sourceName = signature.getDeclaringTypeName().split("\\.");
        String className = sourceName[sourceName.length - 1] +
                (StrUtil.isBlank(classCommentName) ? "" : "[" + classCommentName + "]");
        // 方法名
        // swagger中文注释名
        ApiOperation operation = methodSignature.getMethod().getAnnotation(ApiOperation.class);
        String methodCommentName = "";
        if (api != null) {
            methodCommentName = operation.value();
        }
        String methodName = signature.getName() + (StrUtil.isBlank(methodCommentName) ? "" : "[" + methodCommentName + "]");
        // 参数名数组
        String[] parameterNames = ((MethodSignature) signature).getParameterNames();
        // 构造参数组集合
        List<Object> argList = new ArrayList<>();
        for (Object arg : joinPoint.getArgs()) {
            // request/response无法使用toJSON
            if (arg instanceof HttpServletRequest) {
                argList.add("HttpServletRequest");
            } else if (arg instanceof HttpServletResponse) {
                argList.add("HttpServletResponse");
            } else {
                argList.add(JSONUtil.parse(arg));
            }
        }
        log.info("请求源IP【{}】 -> 请求URL【{}】->执行方法：{}.{} -> 原始请求参数：{} -> {}",
                ipAddr, requestUrl,
                className, methodName,
                JSONUtil.toJsonStr(parameterNames), JSONUtil.toJsonStr(argList));
    }

    @PostConstruct
    public void checkConfig() {

        log.debug("【公共组件】: 开启 <全局参数校验功能> 相关的配置");
    }

    /**
     * 获取目标主机的ip
     *
     * @param request HttpServletRequest
     * @return String
     * @return String
     */
    private String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.contains("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }
}
