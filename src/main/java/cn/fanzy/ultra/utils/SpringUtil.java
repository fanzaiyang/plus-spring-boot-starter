package cn.fanzy.ultra.utils;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * spring上下文
 *
 * @author fanzaiyang
 * @since 2021/07/05
 */
@Slf4j
@Component
public class SpringUtil implements ApplicationContextAware, DisposableBean {

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        // NOSONAR
        // 改动1、用静态set方法给static field 赋值
        setApplicationContextStatic(applicationContext);
    }

    /**
     * 改动2、静态set方法
     *
     * @param applicationContext ApplicationContext
     */
    public static void setApplicationContextStatic(final ApplicationContext applicationContext) {
        SpringUtil.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(clazz);
    }

    /**
     * 清除applicationContext静态变量.
     */
    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicationContext未注入,请在applicationContext.xml中定义UtilsSpringContext");
        }
    }

    @Override
    public void destroy() {
        SpringUtil.cleanApplicationContext();
    }


    /**
     * 得到响应
     *
     * @return {@link HttpServletResponse}
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
    }

    /**
     * get请求
     *
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * <p>
     * 通知向此应用程序注册的所有匹配侦听器事件
     * </p>
     * 如果指定的事件不是ApplicationEvent，则将其包装在PayloadApplicationEvent中。
     *
     * @param event 需要发布的事件
     */
    public static void publishEvent(Object event) {
        applicationContext.publishEvent(event);
    }

    public static String getEnv() {
        return SpringUtil.getBean(Environment.class).getProperty("spring.profiles.active");
    }

    public static boolean isEnvProd() {
        return StrUtil.equals(getEnv(), "prod");
    }

}
