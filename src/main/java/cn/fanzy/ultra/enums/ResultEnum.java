package cn.fanzy.ultra.enums;

import lombok.Getter;

/**
 * <h1>错误信息枚举</h1>
 * <pre>
 *     所有业务异常信息
 * </pre>
 *
 * @author fanzaiyang
 * @since 2020-12-04 09:28:54
 */
@Getter
public enum ResultEnum {
    /**
     * 操作成功
     */
    OK(200, "操作成功！"),
    /**
     * 无效参数
     */
    INVALID_PARAMS(1001, "请检查请求参数！"),
    /**
     * 系统异常
     */
    SYS_ERROR(500, "系统异常！"),
    /**
     * 400 错误的请求
     */
    BAD_REQUEST(400, "错误的请求！"),
    /**
     * 401：授权失败，拒绝访问！
     */
    UNAUTHORIZED(401, "授权失败，拒绝访问！"),
    /**
     * 403：无权限访问！
     */
    UN_ACCESSED(403, "你无权限访问该资源！"),
    /**
     * 404：无权限访问！
     */
    NOT_FOUND(404, "未找到访问该资源！"),
    /**
     * 业务操作失败！
     */
    BIZ_ERROR(-1, "操作失败！"),
    /**
     * 更新失败!
     */
    BIZ_UPDATE_ERROR(2001, "更新失败！"),
    /**
     * 添加失败!
     */
    BIZ_ADD_ERROR(2002, "添加失败！"),
    /**
     * 删除失败!
     */
    BIZ_DELETE_ERROR(2003, "删除失败！"),
    /**
     * 未找到对应对象!
     */
    BIZ_GET_ERROR(2004, "未找到对应对象！"),
    /**
     * 暂无数据!
     */
    BIZ_EMPTY(2005, "暂无数据！"),

    /**
     * 数据误差
     */
    DATA_ERROR(3001, "数据异常！"),
    ;

    private final int code;
    private final String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResultEnum getByCode(int code) {
        for (ResultEnum resultEnum : ResultEnum.values()) {
            if (code == resultEnum.getCode()) {
                return resultEnum;
            }
        }
        return null;
    }
}
