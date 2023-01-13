package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description: 用户信息
 * @author: apecode
 * @date: 2022-06-15 16:43
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户信息", description = "用户信息")
public class UserInfoVo {

    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(name = "nickname", value = "昵称", dataType = "String", required = true)
    private String nickname;

    @ApiModelProperty(name = "intro", value = "介绍", dataType = "String")
    private String intro;

    @ApiModelProperty(name = "webSite", value = "个人网站", dataType = "String")
    private String webSite;

    @NotNull(message = "是否开启邮件通知?")
    @ApiModelProperty(name = "isEmailNotice", value = "是否开启邮件通知消息(0否/1是)", dataType = "Boolean", required = true)
    private Boolean isEmailNotice;
}
