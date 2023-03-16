package cn.apecode.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @description: 前台用户信息
 * @author: apecode
 * @date: 2022-06-15 18:59
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "前台用户信息", description = "前台用户信息")
public class UserInfoFrontDto {

    @ApiModelProperty(value = "邮箱号", name = "email", dataType = "String")
    private String email;

    @ApiModelProperty(value = "8位数id", name = "uid", dataType = "String")
    private String uid;

    @ApiModelProperty(value = "登录类型", name = "loginType", dataType = "Integer")
    private Integer loginType;

    @ApiModelProperty(value = "用户名", name = "nickname", dataType = "String")
    private String nickname;

    @ApiModelProperty(value = "用户头像", name = "avatar", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "用户简介", name = "intro", dataType = "String")
    private String intro;

    @ApiModelProperty(value = "个人网站", name = "webSite", dataType = "String")
    private String webSite;

    @ApiModelProperty(value = "是否绑定QQ（1是/0否）", name = "bindQQ", dataType = "Boolean")
    private Boolean bindQQ;

    @ApiModelProperty(value = "是否开启邮件通知消息(0否/1是)", name = "isEmailNotice", dataType = "Boolean")
    private Boolean isEmailNotice;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
