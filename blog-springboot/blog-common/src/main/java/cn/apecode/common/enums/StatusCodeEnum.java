package cn.apecode.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @description: 状态码
 * @author: apecode
 * @date: 2022-05-27 00:25
 **/
@Getter
@AllArgsConstructor
public enum StatusCodeEnum {

    SUCCESS(1000, "成功"),
    UNAUTHORIZED(1001, "请登录"),
    USERNAME_PASSWORD_ERROR(1002, "用户名或密码错误"),
    AUTHORIZED(1003, "无权限"),
    EXPIRATION(1004, "登录状态过期，请重新登录"),
    EMAIL_EXIST(1005, "此邮箱已被注册"),
    EMAIL_NO_EXIST(1006, "此邮箱不存在"),
    NICKNAME_EXIST(1007, "昵称已存在"),
    DISABLE(1008, "账号被禁用"),
    FAIL(1009, "失败"),
    VALID_ERROR(1010, "参数不正确"),
    ACCESS_LIMIT(1011, "请求过于频繁，请稍后再试"),
    QQ_LOGIN_ERROR(1012, "QQ登录错误"),
    PAGE_NOT_FOUND(404, "页面不存在"),
    SERVER_ERROR(500, "服务器错误"),
    BAD_REQUEST(400, "请求错误");

    /*
    * 状态码
    * */
    private final Integer code;
    /*
    * 状态描述
    * */
    private final String desc;
}
