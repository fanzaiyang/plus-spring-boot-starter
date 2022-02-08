package cn.fanzy.plus.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * 异常信息提取工具
 *
 * @author fanzaiyang
 * @since 2021/09/07
 */
@Slf4j
public class ErrorUtil {

    /**
     * <p>
     * 根据异常从配置信息中获取异常信息
     * </p>
     *
     * @param e          异常信息
     * @param defaultMsg 自定义提示信息
     * @return 异常提示信息
     */
    public static String getErrorMsg(Throwable e, String defaultMsg) {
        if (null == e) {
            return "未知异常";
        }

        String msg = e.getMessage();
        // 返回默认信息
        if (StrUtil.isBlank(e.getMessage()) || StrUtil.equalsAnyIgnoreCase(e.getMessage(), "null")) {
            return StrUtil.isNotBlank(defaultMsg) ? defaultMsg : msg;
        }
        msg = e.getMessage();
        if (e.getCause() != null) {
            msg = e.getCause().getMessage();
            if (e.getCause().getCause() != null) {
                msg = e.getCause().getCause().getMessage();
            }
        }
        return msg;
    }

    public static int getErrorCode(Throwable e, int defaultCode) {
        Field field = ClassUtil.getDeclaredField(e.getClass(), "errorCode");
        if (field == null) {
            field = ClassUtil.getDeclaredField(e.getClass(), "code");
        }
        if (field == null) {
            return defaultCode;
        }
        field.setAccessible(true);
        try {
            Object code = field.get(e);
            if (ObjectUtil.isValidIfNumber(code)) {
                return (int) code;
            }
        } catch (IllegalAccessException ex) {
            log.error("获取错误码异常！",ex);
        }
        return defaultCode;
    }

    /**
     * <p>
     * 根据异常从配置信息中获取异常信息
     * </p>
     *
     * @param e 异常信息
     * @return 异常提示信息
     */
    public static String getErrorMsg(Throwable e) {
        return getErrorMsg(e, null);
    }

}
