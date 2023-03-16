package cn.apecode.common.exception;

import cn.apecode.common.enums.StatusCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static cn.apecode.common.enums.StatusCodeEnum.FAIL;

/**
 * @description: 自定义异常
 * @author: apecode
 * @date: 2022-05-27 01:08
 **/
@Getter
@AllArgsConstructor
public class BizException extends RuntimeException {

    /*
     * 错误码
     * */
    private Integer code = FAIL.getCode();

    /**
     * 错误信息
     */
    private String message;

    public BizException(String message) {
        this.message = message;
    }

    public BizException(StatusCodeEnum statusCodeEnum) {
        this.code = statusCodeEnum.getCode();
        this.message = statusCodeEnum.getDesc();
    }
}
