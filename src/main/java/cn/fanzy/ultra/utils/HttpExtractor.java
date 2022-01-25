package cn.fanzy.ultra.utils;


import cn.fanzy.ultra.exception.CustomException;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


/**
 * http basic认证信息提取器
 *
 * @author fanzaiyang
 * @since  2021/09/07
 */
public class HttpExtractor {
    private final static String BASIC = "basic ";

    private final static String AUTH_FLAG = "Authorization";

    private final static String COLON = ":";

    /**
     * <p>
     * 从请求里提取出basic认证信息
     * </p>
     * 如果不是basic认证则返回为null
     *
     * @param request 请求
     * @return basic认证信息, 只有两个元素，数组形式为 {用户名,密码}
     * @throws CustomException 提取basic认证信息时出现问题
     */
    public String[] extractBaiscAuth(HttpServletRequest request) throws CustomException {
        String header = request.getHeader(AUTH_FLAG);

        if (header == null || !header.toLowerCase().startsWith(BASIC)) {
            return null;
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(header.substring(6).getBytes(StandardCharsets.UTF_8));
            String token = new String(decoded, StandardCharsets.UTF_8);

            int delim = token.indexOf(COLON);

            if (delim == -1) {
                throw new CustomException("无效的Basic");
            }
            return new String[]{token.substring(0, delim), token.substring(delim + 1)};
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }

    }
}
