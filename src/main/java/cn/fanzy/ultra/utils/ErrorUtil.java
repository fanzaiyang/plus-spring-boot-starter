package cn.fanzy.ultra.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 异常信息提取工具
 *
 * @author fanzaiyang
 * @since  2021/09/07
 */
public class ErrorUtil {

    /**
     * 格式化后的异常存储信息
     */
    private static final Map<String, String> ERRORS = new WeakHashMap<>();

    /**
     * <p>
     * 根据异常从配置信息中获取异常信息
     * </p>
     * <p>
     * 提取过程如下
     * <ul>
     * <li>先根据异常类的完整名字获取异常提示信息</li>
     * <li>如果第一步中没有获取异常信息，则根据异常类的名字(不区分大小)获取异常提示信息</li>
     * <li>如果还是没有获取到异常提示信息，且用户配置的提示信息不为空，则使用用户配置的第一个提示信息作为异常提示信息</li>
     * <li>如果还是没有获取到异常提示信息，就是用原来的异常类里的信息</li>
     * </ul>
     *
     * @param e          异常信息
     * @param defaultMsg 自定义提示信息
     * @return 异常提示信息
     */
    public static String getErrorMsg(Throwable e, String defaultMsg) {
        if (null == e) {
            return "未知异常";
        }
        // 全称信息提示
        String msg = ERRORS.get(e.getClass().getName());
        // 根据简称查找
        if (StrUtil.isBlank(msg)) {
            msg = ERRORS.get(e.getClass().getSimpleName().toLowerCase());
        }

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
            if (StrUtil.startWithIgnoreCase(e.getClass().getSimpleName(), "NotLogin")) {
                return 401;
            }
            return defaultCode;
        }
        field.setAccessible(true);
        try {
            Object code = field.get(e);
            System.out.println("错误码：" + code);
            if (ObjectUtil.isValidIfNumber(code)) {
                return (int) code;
            }
            if (StrUtil.startWithIgnoreCase(e.getClass().getName(), "NotLogin")) {
                return 401;
            }
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
            if (StrUtil.startWithIgnoreCase(e.getClass().getName(), "NotLogin")) {
                return 401;
            }
        }
        return defaultCode;
    }

    /**
     * <p>
     * 根据异常从配置信息中获取异常信息
     * </p>
     * <p>
     * 提取过程如下
     * <ul>
     * <li>先根据异常类的完整名字获取异常提示信息</li>
     * <li>如果第一步中没有获取异常信息，则根据异常类的名字(不区分大小)获取异常提示信息</li>
     * <li>如果还是没有获取到异常提示信息，就是用原来的异常类里的信息</li>
     * </ul>
     *
     * @param e 异常信息
     * @return 异常提示信息
     */
    public static String getErrorMsg(Throwable e) {
        return getErrorMsg(e, null);
    }

}
