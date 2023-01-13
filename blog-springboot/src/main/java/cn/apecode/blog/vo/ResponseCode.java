package cn.apecode.blog.vo;

import cn.apecode.blog.enums.StatusCodeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import static cn.apecode.blog.enums.StatusCodeEnum.SUCCESS;
import static cn.apecode.blog.enums.StatusCodeEnum.FAIL;


/**
 * @description: 响应体
 * @author: apecode
 * @date: 2022-05-27 00:01
 **/
@Data
@ApiModel(value = "响应体", description = "数据响应体")
public class ResponseCode<T> {

    @ApiModelProperty(value = "状态")
    private boolean status;

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "信息")
    private String message;

    @ApiModelProperty
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
