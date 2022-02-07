package cn.fanzy.ultra.logs.service;

import java.util.Date;

/**
 * 日志回调类
 *
 * @author fanzaiyang
 * @since 2022-01-30
 */
public interface LogCallbackService {

    /**
     * 回调
     * 日志执行回调函数
     * <pre>实现此方法，可拿到日志调用参数，并进一步加工处理。</pre>
     *
     * @param ip           IP地址
     * @param user         当前用户
     * @param url          请求url
     * @param method       请求方法名
     * @param swaggerName  Swagger的名称
     * @param requestData  请求数据
     * @param responseData 响应数据
     * @param start        开始时间
     * @param end          结束时间
     * @param second       执行秒数
     */
    void callback(String ip, String user, String url, String method, String swaggerName, String requestData, String responseData, Date start,Date end, long second);
}
