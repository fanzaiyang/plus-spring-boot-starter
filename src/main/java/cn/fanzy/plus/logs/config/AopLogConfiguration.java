package cn.fanzy.plus.logs.config;

import cn.fanzy.plus.swagger.SwaggerProperties;
import cn.fanzy.plus.utils.SpringUtils;
import cn.fanzy.plus.logs.properties.AopLogProperties;
import cn.fanzy.plus.logs.service.LogCallbackService;
import cn.fanzy.plus.logs.service.LogUserService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 全局参数校验功能自动配置
 *
 * @author fanzaiyang
 * @since 2021/09/06
 */
@Configuration
@Aspect
@EnableConfigurationProperties(AopLogProperties.class)
@ConditionalOnProperty(prefix = "plus.aop.log", name = {"enable"}, havingValue = "true", matchIfMissing = true)
@Slf4j
public class AopLogConfiguration {

    @Autowired
    private LogUserService logUserService;
    @Autowired
    private LogCallbackService logCallbackService;
    private static final AntPathMatcher matcher = new AntPathMatcher();
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
     * @return {@link Object}
     * @throws Throwable throwable
     */
    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = SpringUtils.getRequest();
        TimeInterval interval = DateUtil.timer();
        Date start = new Date();
        // 忽略swagger的日志
        List<String> list = SwaggerProperties.SWAGGER_LIST.stream().filter(item -> matcher.match(item, request.getRequestURI()))
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(list)) {
            return joinPoint.proceed();
        }
        // 执行的业务名
        String swaggerName = getApiName((MethodSignature)joinPoint.getSignature());
        // 执行的方法名
        String methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        // 参数名数组 构造参数组集合
        Map<String, Object> param = buildReqParam(joinPoint);

        // 执行程序，并返回结果
        Object proceed = joinPoint.proceed();

        Date end = new Date();
        try {
            logCallbackService.callback(SpringUtils.getClientIp(request), logUserService.getCurrentUser(), request.getRequestURL().toString(),
                    methodName,
                    swaggerName,
                    JSONUtil.toJsonStr(param), JSONUtil.toJsonStr(proceed),
                    start, end,
                    interval.intervalSecond()
            );
        } catch (Exception e) {
            log.error("执行回调失败！", e);
        }
        return proceed;
    }

    private Map<String, Object> buildReqParam(ProceedingJoinPoint joinPoint) {
        Map<String, Object> param = new HashMap<>();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            Object arg = joinPoint.getArgs()[i];
            if (!(arg instanceof HttpServletRequest || arg instanceof HttpServletResponse)) {
                param.put(parameterNames[i], arg);
            }
        }
        return param;
    }
    private String getApiName(MethodSignature signature){
        String swaggerName = "";
        // 类名 swagger中文注释名
        Api api = signature.getMethod().getDeclaringClass().getAnnotation(Api.class);
        if (api != null) {
            swaggerName = String.join(",", api.tags());
        }
        // 方法名  swagger中文注释名
        ApiOperation operation = signature.getMethod().getAnnotation(ApiOperation.class);
        if (operation != null) {
            swaggerName = StrUtil.isBlank(swaggerName) ? operation.value() : swaggerName + "->" + operation.value();
        }
        return swaggerName;
    }

    /**
     * 检查配置
     */
    @PostConstruct
    public void checkConfig() {

        log.debug("【Plus组件】: 开启 <全局日志> 相关的配置");
    }
}
