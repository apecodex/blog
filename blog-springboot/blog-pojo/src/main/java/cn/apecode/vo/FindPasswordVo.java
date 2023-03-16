package cn.apecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @description: 更新用户密码
 * @author: apecode
 * @date: 2022-06-09 16:24
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "找回用户密码", description = "前端输入新的密码更新用户密码")
public class FindPasswordVo {

    @ApiModelProperty(name = "email", value = "邮箱", required = true, dataType = "String")
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "新密码不能少于6位，最多20位")
    @Pattern(regexp = "(?=.*([a-zA-Z].*))(?=.*[0-9].*)[a-zA-Z0-9-*/+.~!@#$%^&*()]{6,20}$", message = "至少包含数字跟字母")
    @ApiModelProperty(value = "新密码", name = "newPassword", required = true, dataType = "String")
    private String newPassword;

    @ApiModelProperty(name = "code", value = "验证码", required = true, dataType = "String")
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty(value = "滑动验证码", name = "captchaVerification", dataType = "String", required = true)
    private String captchaVerification;
}
