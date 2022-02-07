package cn.fanzy.plus.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * 日志回调类
 *
 * @author fanzaiyang
 * @since 2022-01-30
 */
@Slf4j
public class LogOutUtil {

    private final static String format = "\n->IP【{}】用户【{}】访问【{}】执行【{}】({})\n    请求【{}】\n    响应【{}】\n    耗时【{}】秒（{}～{}）";

    /**
     * 日志输出回调函数
     *
     * @param ip           IP地址
     * @param user         当前用户
     * @param url          请求url
     * @param method       请求方法名
     * @param swaggerName  Swagger的名称
     * @param requestData  请求数据
     * @param responseData 响应数据
     * @param second       执行秒数
     * @param start        开始时间
     * @param end          结束时间
     */
    public static void out(String ip, String user, String url, String method, String swaggerName, String requestData, String responseData, Date start, Date end, long second) {
        log.info(format, ip, user, url, swaggerName, method,
                requestData, responseData,
                second,
                DateUtil.format(start, DatePattern.NORM_DATETIME_MS_PATTERN), DateUtil.format(end, DatePattern.NORM_DATETIME_MS_PATTERN)
        );
    }
}
