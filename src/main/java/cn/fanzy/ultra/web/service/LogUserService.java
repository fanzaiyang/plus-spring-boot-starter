package cn.fanzy.ultra.web.service;

/**
 * 日志组件-当前登录人相关
 *
 * @author fanzaiyang
 * @since  2022-01-30
 */
public interface LogUserService {
    /**
     * 获取当前用户
     *
     * @return {@link String}
     */
    String getCurrentUser();
}
