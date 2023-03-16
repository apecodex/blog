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
 * @description: 注册
 * @author: apecode
 * @date: 2022-06-09 21:41
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "注册", description = "前端注册")
public class RegisterVo {

    @ApiModelProperty(value = "邮箱号", name = "email", dataType = "String", required = true)
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱号不能为空")
    private String email;

    @ApiModelProperty(value = "密码", name = "password", dataType = "String", required = true)
    @Size(min = 6, max = 20, message = "新密码不能少于6位，最多20位")
    @Pattern(regexp = "(?=.*([a-zA-Z].*))(?=.*[0-9].*)[a-zA-Z0-9-*/+.~!@#$%^&*()]{6,20}$", message = "至少包含数字跟字母")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "验证码", name = "code",  dataType = "String", required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;

    @ApiModelProperty(value = "滑动验证码", name = "captchaVerification", dataType = "String", required = true)
    private String captchaVerification;

}
