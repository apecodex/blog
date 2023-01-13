package cn.apecode.blog.exception;

import cn.apecode.blog.vo.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static cn.apecode.blog.enums.StatusCodeEnum.SERVER_ERROR;

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
        log.error("Server error: {}", e.getLocalizedMessage());
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
        return ResponseCode.fail(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }
}
