package cn.apecode.common.exception;

import cn.apecode.common.utils.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static cn.apecode.common.enums.StatusCodeEnum.SERVER_ERROR;
import static cn.apecode.common.enums.StatusCodeEnum.BAD_REQUEST;

/**
 * @description: 全局异常处理
 * @author: apecode
 * @date: 2022-05-27 01:11
 **/

@Slf4j
@RestControllerAdvice
public class GlobalException {

    /**
     * @return {@link ResponseCode<?>}
     * @description: 处理服务异常
     * @auther apecode
     * @date 2022/5/27 1:16
     */
    @ExceptionHandler(value = {BizException.class})
    public ResponseCode<?> errorHandler(BizException e) {
        log.info("处理服务异常 - 错误码：{} - 错误信息：{}", e.getCode(), e.getMessage());
        return ResponseCode.fail(e.getCode(), e.getMessage());
    }

    /**
     * @param e
     * @return {@link ResponseCode<?>}
     * @description: 处理接口异常
     * @auther apecode
     * @date 2022/5/27 21:09
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseCode<?> errorHandler(Exception e) {
        log.error("处理接口异常: {}", e.getMessage());
        return ResponseCode.customize(false, SERVER_ERROR);
    }

    /**
     * @param e
     * @return {@link ResponseCode<?>}
     * @description: 处理参数校验异常
     * @auther apecode
     * @date 2022/5/27 21:09
     */
    @ExceptionHandler(value = {BindException.class})
    public ResponseCode<?> errorHandler(BindException e) {
        log.error("参数校验异常：{}", Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        return ResponseCode.fail(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    /**
     * @param e
     * @return {@link ResponseCode<?>}
     * @description: 客户端已经断开连接
     * @auther apecode
     * @date 2023/7/22 13:54
     */
    @ExceptionHandler(value = ClientAbortException.class)
    public ResponseCode<?> handlerClientAbortException(Exception e) {
        log.warn("in clientAbortException");
        return null;
    }

    /**
     * @description: 请求方式错误
     * @param e
     * @return {@link ResponseCode<?>}
     * @auther apecode
     * @date 2023/7/25 16:51
    */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseCode<?> methodNotSupportErrorHandler(Exception e) {
        log.error("请求方式有误：{}", e.getMessage());
        return ResponseCode.customize(false, BAD_REQUEST);
    }

}
