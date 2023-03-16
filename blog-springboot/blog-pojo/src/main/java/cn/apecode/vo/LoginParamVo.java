package cn.apecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @description: 登录参数
 * @author: apecode
 * @date: 2022-05-27 20:38
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "登录参数", description = "用户登录的参数")
public class LoginParamVo {

    @ApiModelProperty(name = "username", value = "邮箱", required = true, dataType = "String")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String username;

    @ApiModelProperty(name = "password", value = "密码", required = true, dataType = "String")
    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "请完成验证")
    @ApiModelProperty(value = "验证码", required = true, dataType = "String")
    private String captchaVerification;
}
