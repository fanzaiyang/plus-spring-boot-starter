package cn.fanzy.ultra.web.service;

/**
 * 日志组件-当前登录人相关
 *
 * @author fanzaiyang
 * @since 2022-01-30
 */
public interface LogCallbackService {

    /**
     * 日志执行回调函数
     *
     * @param ip           IP地址
     * @param user         当前用户
     * @param url          请求url
     * @param method       请求方法名
     * @param swaggerName  Swagger的名称
     * @param requestData  请求数据
     * @param responseData 响应数据
     * @param second       执行秒数
     */
    void callback(String ip, String user, String url, String method, String swaggerName, String requestData, String responseData, long second);
}
