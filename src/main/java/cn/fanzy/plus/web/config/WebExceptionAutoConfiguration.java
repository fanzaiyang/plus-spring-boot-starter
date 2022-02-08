package cn.fanzy.plus.web.config;

import cn.fanzy.plus.enums.ResultEnum;
import cn.fanzy.plus.exception.CustomException;
import cn.fanzy.plus.model.Json;
import cn.fanzy.plus.utils.ErrorUtil;
import cn.fanzy.plus.web.properties.WebExceptionProperties;
import cn.fanzy.plus.web.properties.WebFilterProperties;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.UUID;


/**
 * 全局异常捕获自动配置
 *
 * @author fanzaiyang
 * @since 2021/09/06
 */
@Slf4j
@ControllerAdvice
@ResponseBody
@EnableConfigurationProperties({WebFilterProperties.class, WebExceptionProperties.class})
@ConditionalOnProperty(prefix = "plus.error", name = {
        "enable"}, havingValue = "true", matchIfMissing = true)
public class WebExceptionAutoConfiguration {
    @Autowired
    private WebFilterProperties webProperties;


    /**
     * 400 - Bad Request
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object handleHttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
        String ssid = this.getRequestId(request);
        Json<String> response = new Json<String>(HttpStatus.BAD_REQUEST.value(), StrUtil.blankToDefault(e.getMessage(), "参数解析失败")).setId(ssid);
        log.error(StrUtil.format("【Plus组件】HttpMessageNotReadableException,错误的请求,失败的原因为：{}",  e.getMessage())
                , e);
        return response;
    }

    /**
     * 400 - Bad Request
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        String ssid = this.getRequestId(request);
        Json<String> response = new Json<String>(HttpStatus.BAD_REQUEST.value(), StrUtil.blankToDefault(e.getMessage(), "参数不符合要求")).setId(ssid);
        log.error(StrUtil.format("【Plus组件】IllegalArgumentException，参数解析失败,失败的原因为：{}",  e.getMessage()), e);
        return response;
    }

    /**
     * 405 - Method Not Allowed
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object handleHttpRequestMethodNotSupportedException(HttpServletRequest request,
                                                               HttpRequestMethodNotSupportedException e) {
        String ssid = this.getRequestId(request);
        Json<String> response = new Json<String>(HttpStatus.METHOD_NOT_ALLOWED.value(),
                enToCn(StrUtil.blankToDefault(e.getMessage(), "不支持当前请求方法")))
                .setId(ssid);
        log.error(StrUtil.format("【Plus组件】HttpRequestMethodNotSupportedException,不支持当前请求方法,失败的原因为：{}",  e.getMessage()));
        return response;

    }

    /**
     * 415 - Unsupported Media Type
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Object handleHttpMediaTypeNotSupportedException(HttpServletRequest request,
                                                           HttpMediaTypeNotSupportedException e) {
        String ssid = this.getRequestId(request);
        Json<String> response = new Json<String>(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), StrUtil.blankToDefault(e.getMessage(), "不支持当前媒体类型"))
                .setId(ssid);
        log.error(StrUtil.format("【Plus组件】HttpMediaTypeNotSupportedException,不支持当前媒体类型,失败的原因为：{}",  e.getMessage()), e);
        return response;
    }

    /**
     * 500 - Internal Server Error
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NullPointerException.class)
    public Object handleNullPointerException(HttpServletRequest request, NullPointerException e) {
        String ssid = this.getRequestId(request);
        Json<String> response = new Json<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), StrUtil.blankToDefault(e.getMessage(), "请求失败")).setId(ssid);
        log.error(StrUtil.format("【Plus组件】NullPointerException,请求失败,失败的原因为空指针异常!", ssid), e);
        return response;
    }

    /**
     * 500 - Internal Server Error
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServletException.class)
    public Object handleServletException(HttpServletRequest request, ServletException e) {
        String ssid = this.getRequestId(request);
        String msg = e.getMessage();
        Json<String> response = new Json<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg).setId(ssid);
        log.error(StrUtil.format("【Plus组件】ServletException,请求失败,失败的原因为：{}",  msg), e);
        return response;
    }

    /**
     * 500 - Internal Server Error
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(IOException.class)
    public Object handleIoException(HttpServletRequest request, IOException e) {
        String ssid = this.getRequestId(request);
        String msg = e.getMessage();
        Json<String> response = new Json<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg).setId(ssid);
        log.error(StrUtil.format("【Plus组件】IOException,请求失败,失败的原因为：{}",  msg), e);
        return response;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Object handleMissingServletRequestParameterException(HttpServletRequest request,
                                                                MissingServletRequestParameterException e) {
        String ssid = this.getRequestId(request);
        Json<String> response = new Json<String>(HttpStatus.BAD_REQUEST.value(), StrUtil.blankToDefault(e.getMessage(), "请求参数有误")).setId(ssid);
        log.error(StrUtil.format("【Plus组件】MissingServletRequestParameterException,请求参数有误,失败的原因为：{}",  e.getMessage()), e);
        return response;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object handleMethodArgumentTypeMismatchException(HttpServletRequest request,
                                                            MethodArgumentTypeMismatchException e) {
        String ssid = this.getRequestId(request);
        Json<String> response = new Json<String>(HttpStatus.BAD_REQUEST.value(), StrUtil.blankToDefault(e.getMessage(), "请求参数有误")).setId(ssid);
        log.error(StrUtil.format("【Plus组件】MethodArgumentTypeMismatchException,方法参数有误,失败的原因为：{}",  e.getMessage()), e);
        return response;
    }

    /**
     * 参数验证异常
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object handle(HttpServletRequest request, ValidationException e) {
        String ssid = this.getRequestId(request);
        Json<String> response = new Json<String>(HttpStatus.BAD_REQUEST.value(), StrUtil.blankToDefault(e.getMessage(), "非法参数")).setId(ssid);
        log.error(StrUtil.format("【Plus组件】ValidationException,参数校验有误,失败的原因为：{}",  e.getMessage()), e);
        return response;
    }

    /**
     * 参数验证异常
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Object handle(HttpServletRequest request, ConstraintViolationException e) {
        String ssid = this.getRequestId(request);
        Json<String> response = new Json<String>(HttpStatus.BAD_REQUEST.value(), StrUtil.blankToDefault(e.getMessage(), "非法参数")).setId(ssid);
        log.error(StrUtil.format("【Plus组件】ConstraintViolationException,参数约束有误,失败的原因为：{}",  e.getMessage()), e);
        return response;
    }

    /**
     * 数组越界 - Internal Server Error
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public Object handleIndexOutOfBoundsException(HttpServletRequest request, IndexOutOfBoundsException e) {
        String ssid = this.getRequestId(request);
        Json<String> response = new Json<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), StrUtil.blankToDefault(e.getMessage(), "未查询到对应的数据"))
                .setId(ssid);
        log.error(StrUtil.format("【Plus组件】IndexOutOfBoundsException,请求失败,出现数组越界,失败的原因为：{}",  e.getMessage()), e);
        return response;
    }

    /**
     * 500 - 自定义异常
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(CustomException.class)
    public Object handleCustomException(HttpServletRequest request, CustomException e) {
        String ssid = this.getRequestId(request);
        Json<String> response = new Json<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage())
                .setId(ssid);
        log.error(StrUtil.format("【Plus组件】CustomException,请求失败,失败的原因为：{}",  e.getMessage()), e);
        return response;
    }

    /**
     * 500 - IllegalStateException
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(IllegalStateException.class)
    public Object handleIllegalStateException(HttpServletRequest request, IllegalStateException e) {
        String ssid = this.getRequestId(request);
        Json<Object> response = Json.error(ErrorUtil.getErrorMsg(e, "请求失败")).setId(ssid);
        log.error(StrUtil.format("【Plus组件】IllegalStateException，请求失败,拦截到未知异常：{}",  e.getMessage()), e);
        return response;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public Object handleException(HttpServletRequest request, SQLSyntaxErrorException e) {
        String ssid = this.getRequestId(request);
        Json<Object> response = new Json<>(ErrorUtil.getErrorCode(e, ResultEnum.BIZ_ERROR.getCode()),
                ErrorUtil.getErrorMsg(e, "请求失败")).setId(ssid);
        log.error(StrUtil.format("【Plus组件】SQLSyntaxErrorException,请求失败,拦截到SQLSyntaxErrorException异常：{}",  e.getMessage()), e);
        return response;
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(SQLException.class)
    public Object handleException(HttpServletRequest request, SQLException e) {
        String ssid = this.getRequestId(request);
        Json<Object> response = new Json<>(ErrorUtil.getErrorCode(e, ResultEnum.BIZ_ERROR.getCode()),
                ErrorUtil.getErrorMsg(e, "请求失败")).setId(ssid);
        log.error(StrUtil.format("【Plus组件】SQLException,请求失败,拦截到SQLException异常：{}",  e.getMessage()), e);
        return response;
    }


    /**
     * 404 - Internal Server Error
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleException(HttpServletRequest request, NoHandlerFoundException e) {
        String ssid = this.getRequestId(request);
        Json<Object> response = new Json<>(404, enToCn(e.getMessage())).setId(ssid);
        log.error(StrUtil.format("【Plus组件】NoHandlerFoundException,请求失败,拦截到未找到处理程序异常：{}",  e.getMessage()));
        return response;
    }

    /**
     * 处理valid验证参数问题
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Json<Object> processException(HttpServletRequest request, MethodArgumentNotValidException e) {
        String ssid = this.getRequestId(request);
        if (e.getBindingResult().getFieldErrors().size() > 0) {
            String defaultMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
            log.error(StrUtil.format("【Plus组件】MethodArgumentNotValidException,请求失败,拦截到参数校验异常：{}",  e.getMessage()), e);
            return new Json<>(ResultEnum.INVALID_PARAMS.getCode(), defaultMessage).setId(ssid);
        }
        return Json.error(e.getMessage());
    }

    /**
     * 处理文件上传异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public Json<Object> processException(HttpServletRequest request, MaxUploadSizeExceededException e) {
        String ssid = this.getRequestId(request);
        log.error(StrUtil.format("【Plus组件】MaxUploadSizeExceededException,请求失败,拦截到文件上传超过限制异常：{}",  e.getMessage()), e);
        return new Json<>(ResultEnum.BAD_REQUEST.getCode(), e.getMessage()).setId(ssid);
    }

    /**
     * 500 - IllegalStateException
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(RuntimeException.class)
    public Object handleRuntimeException(HttpServletRequest request, RuntimeException e) {
        String ssid = this.getRequestId(request);
        Json<Object> response = new Json<>(ErrorUtil.getErrorCode(e, ResultEnum.BIZ_ERROR.getCode()),
                ErrorUtil.getErrorMsg(e, "请求失败")).setId(ssid);
        log.error(StrUtil.format("【Plus组件】RuntimeException,请求失败,拦截到运行时异常：{}",  e.getMessage()), e);
        return response;
    }

    /**
     * 500 - Internal Server Error
     *
     * @param request HttpServletRequest
     * @param e       希望捕获的异常
     * @return 发生异常捕获之后的响应
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Object handleException(HttpServletRequest request, Exception e) {
        String ssid = this.getRequestId(request);
        Json<Object> response = new Json<>(ErrorUtil.getErrorCode(e, ResultEnum.SYS_ERROR.getCode()),
                ErrorUtil.getErrorMsg(e, "请求失败")).setId(ssid);
        log.error(StrUtil.format("【Plus组件】Exception,请求失败,拦截到未知异常：{}",  e.getMessage()), e);
        return response;
    }

    /**
     * 获取请求的id
     *
     * @param request HttpServletRequest
     * @return 请求的ID
     */
    private String getRequestId(HttpServletRequest request) {
        return UUID.randomUUID().toString();
    }

    @PostConstruct
    public void checkConfig() {
        log.debug("【Plus组件】: 开启 <全局异常拦截> 相关的配置");
    }


    private String enToCn(String en) {
        if (StrUtil.equalsIgnoreCase(en, "Request method 'GET' not supported")) {
            return "该接口不支持GET方式！";
        }
        if (StrUtil.equalsIgnoreCase(en, "Request method 'POST' not supported")) {
            return "该接口不支持POST方式！";
        }
        if (StrUtil.startWithIgnoreCase(en, "No handler found for")) {
            return "该接口不存在！" + StrUtil.subAfter(en, "No handler found for ", false);
        }
        if (StrUtil.equalsIgnoreCase(en, "Request method 'PUT' not supported")) {
            return "该接口不支持PUT方式！";
        }
        if (StrUtil.equalsIgnoreCase(en, "Request method 'DELETE' not supported")) {
            return "该接口不支持DELETE方式！";
        }
        return en;
    }
}