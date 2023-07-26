package cn.apecode.common.utils;

import cn.apecode.common.enums.StatusCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static cn.apecode.common.enums.StatusCodeEnum.FAIL;
import static cn.apecode.common.enums.StatusCodeEnum.SUCCESS;


/**
 * @description: 响应体
 * @author: apecode
 * @date: 2022-05-27 00:01
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCode<T> implements Serializable {

    // 状态
    private boolean status;

    // 状态码
    private Integer code;

    // 信息
    private String message;

    private T data;

    public static <T> ResponseCode<T> ok() {
        return Result(true, null, SUCCESS.getCode(), SUCCESS.getDesc());
    }

    public static <T> ResponseCode<T> ok(String message) {
        return Result(true, null, SUCCESS.getCode(), message);
    }

    public static <T> ResponseCode<T> ok(T data, String message) {
        return Result(true, data, SUCCESS.getCode(), message);
    }

    public static <T> ResponseCode<T> ok(T data) {
        return Result(true, data, SUCCESS.getCode(), SUCCESS.getDesc());
    }

    public static <T> ResponseCode<T> ok(Integer code, String message) {
        return Result(true, null, code, message);
    }

    public static <T> ResponseCode<T> fail() {
        return Result(false, null, FAIL.getCode(), FAIL.getDesc());
    }

    public static <T> ResponseCode<T> fail(String message) {
        return Result(false, null, FAIL.getCode(), message);
    }

    public static <T> ResponseCode<T> fail(Integer code, String message) {
        return Result(false, null, code, message);
    }

    public static <T> ResponseCode<T> customize(StatusCodeEnum statusCodeEnum) {
        return Result(true, null, statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }

    public static <T> ResponseCode<T> customize(Boolean status, StatusCodeEnum statusCodeEnum) {
        return Result(status, null, statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }

    public static <T> ResponseCode<T> customize(T data, StatusCodeEnum statusCodeEnum) {
        return Result(true, data, statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }

    public static <T> ResponseCode<T> customize(boolean status,T data, StatusCodeEnum statusCodeEnum) {
        return Result(status, data, statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }

    public static <T> ResponseCode<T> Result(Boolean status, T data, Integer code, String message) {
        ResponseCode<T> result = new ResponseCode<>();
        result.setStatus(status);
        result.setData(data);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
