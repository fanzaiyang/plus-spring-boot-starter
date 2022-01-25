package cn.fanzy.ultra.model;

import cn.fanzy.ultra.enums.ResultEnum;
import cn.fanzy.ultra.utils.HttpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

/**
 * 统一响应结果
 *
 * @author fanzaiyang
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Json<T> {
    @ApiModelProperty(value = "请求ID", required = true, notes = "全局唯一的请求ID")
    private String id;
    @ApiModelProperty(value = "全局响应码", required = true, notes = "全局响应码，200表示成功", position = 1)
    private int code;

    @ApiModelProperty(value = "全局响应消息", required = true, notes = "全局响应消息", position = 2)
    private String message;

    @ApiModelProperty(value = "全局响应内容", required = true, notes = "全局响应内容，所有的业务数据放在此属性下。", position = 3)
    private T data;

    @ApiModelProperty(value = "当前时间", required = true, notes = "当前时间：yyyy-MM-dd HH:mm:ss", position = 4)
    private String now = DateUtil.now();

    @ApiModelProperty(value = "是否成功", required = true, notes = "true：成功，false：失败", position = 5)
    private boolean success = false;

    @ApiModelProperty(value = "额外内容", required = true, notes = "特殊业务下需要此属性，默认null", position = 6)
    private Object exData;

    public Json() {
        this.id = HttpUtil.getRequestId();
    }

    public Json(ResultEnum result) {
        this.id = HttpUtil.getRequestId();
        this.code = result.getCode();
        this.message = result.getMessage();
        this.success = result.getCode() == 200;
        this.now = DateUtil.now();
    }

    public Json(ResultEnum result, String message) {
        this.id = HttpUtil.getRequestId();
        this.code = result.getCode();
        this.message = StrUtil.blankToDefault(message, result.getMessage());
        this.success = result.getCode() == 200;
        this.now = DateUtil.now();
    }

    public Json(int code, String message) {
        this.id = HttpUtil.getRequestId();
        this.code = code;
        this.message = message;
        this.success = code == 200;
        this.now = DateUtil.now();
    }

    public Json(ResultEnum result, T data) {
        this.id = HttpUtil.getRequestId();
        this.code = result.getCode();
        this.message = result.getMessage();
        this.success = result.getCode() == 200;
        this.now = DateUtil.now();
        this.data = data;
    }

    /**
     * 成功
     *
     * @return JsonContent
     */
    public static <T> Json<T> success() {
        return success(null);
    }

    /**
     * 成功
     */
    public static <T> Json<T> success(T object) {
        return new Json<>(ResultEnum.OK, object);
    }

    /**
     * 失败
     *
     * @return JsonContent
     */
    public static <T> Json<T> error(ResultEnum result) {
        return new Json<>(result);
    }

    /**
     * 失败
     *
     * @return JsonContent
     */
    public static <T> Json<T> error(int code, String errorMsg) {
        return new Json<>(code, errorMsg);
    }

    /**
     * 失败
     *
     * @return JsonContent
     */
    public static <T> Json<T> error(int code, Exception e) {
        return new Json<>(code, e.getMessage());
    }

    /**
     * 失败
     *
     * @return JsonContent
     */
    public static <T> Json<T> error(Exception exception) {
        return new Json<>(ResultEnum.BIZ_ERROR, exception.getMessage());
    }

    public static <T> Json<T> error() {
        return new Json<>(ResultEnum.BIZ_ERROR);
    }

    /**
     * 失败
     *
     * @return JsonContent
     */
    public static <T> Json<T> error(String errMsg) {
        return error(new RuntimeException(errMsg));
    }

    /**
     * 生成一个默认的表示参数有误的响应对象(响应码400)
     *
     * @return 表示参数有误的响应对象(响应码400)
     */
    public static Json<Object> badParam() {
        return new Json<>(ResultEnum.INVALID_PARAMS);
    }

    /**
     * 根据响应提示信息生成一个表示参数有误的响应对象(响应码400)
     *
     * @param msg 响应提示信息
     * @return 表示参数有误的响应对象(响应码400)
     */
    public static Json<Object> badParam(String msg) {
        return new Json<>(HttpStatus.BAD_REQUEST.value(), msg);
    }

    /**
     * 根据响应提示信息和响应数据生成一个表示参数有误的响应对象(响应码400)
     *
     * @param <T>  响应数据的数据类型
     * @param msg  响应提示信息
     * @param data 响应数据
     * @return 表示参数有误的响应对象(响应码400)
     */
    public static <T> Json<T> badParam(String msg, T data) {
        Json<Object> objectJsonContent = new Json<>();
        return new Json<T>().setCode(HttpStatus.BAD_REQUEST.value())
                .setId(HttpUtil.getRequestId())
                .setMessage(msg).setData(data)
                .setSuccess(false);
    }

    /**
     * 生成一个默认的表示资源未授权的响应对象(401响应码)
     *
     * @return 表示资源未授权的响应对象(401响应码)
     */
    public static Json<Object> unAuth() {
        return new Json<>(ResultEnum.UNAUTHORIZED);
    }

    /**
     * 根据响应提示信息生成一个表示资源未授权的响应对象(401响应码)
     *
     * @param msg 响应提示信息
     * @return 表示资源未授权的响应对象(401响应码)
     */
    public static Json<Object> unAuth(String msg) {
        return new Json<>(HttpStatus.UNAUTHORIZED.value(), msg);
    }

    /**
     * 根据响应提示信息和响应数据生成一个表示资源未授权的响应对象(401响应码)
     *
     * @param <T>  响应数据的类型
     * @param msg  响应提示信息
     * @param data 响应数据
     * @return 表示资源未授权的响应对象(401响应码)
     */
    public static <T> Json<T> unAuth(String msg, T data) {
        return new Json<T>().setCode(HttpStatus.UNAUTHORIZED.value())
                .setMessage(msg).setData(data)
                .setSuccess(false)
                .setId(HttpUtil.getRequestId());
    }

    /**
     * 生成一个默认的表示资源不可用的响应对象(403响应码)
     *
     * @return 表示资源不可用的响应对象(403响应码)
     */
    public static Json<Object> notAllow() {
        return new Json<>(ResultEnum.UN_ACCESSED);
    }

    /**
     * 根据响应提示信息生成表示资源不可用的响应对象(403响应码)
     *
     * @param msg 响应提示信息
     * @return 表示资源不可用的响应对象(403响应码)
     */
    public static Json<Object> notAllow(String msg) {
        return new Json<>(HttpStatus.FORBIDDEN.value(), msg);
    }

    /**
     * 生成一个默认的表示资源不存在的响应对象(404响应码)
     *
     * @return 表示资源不存在的响应对象(404响应码)
     */
    public static Json<Object> notFound() {
        return new Json<>(ResultEnum.NOT_FOUND);
    }

    public boolean isSuccess() {
        return this.code == 200;
    }
}
