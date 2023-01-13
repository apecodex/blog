package cn.apecode.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @description: 后台用户信息
 * @author: apecode
 * @date: 2022-06-09 10:16
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
@ApiModel(value = "后台用户信息", description = "返回用户详细信息")
public class UserInfoBackDto {

    @ApiModelProperty(value = "用户账号id", name = "userAuthId", dataType = "String")
    private String userAuthId;

    @ApiModelProperty(value = "用户信息id", name = "userInfoId", dataType = "String")
    private String userInfoId;

    @ApiModelProperty(value = "用户uid", name = "uid", dataType = "String")
    private String uid;

    @ApiModelProperty(value = "用户昵称", name = "nickname", dataType = "String")
    private String nickname;

    @ApiModelProperty(value = "邮箱", name = "email", dataType = "String")
    private String email;

    @ApiModelProperty(value = "头像", name = "avatar", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "简介", name = "intro", dataType = "String")
    private String intro;

    @ApiModelProperty(value = "个人网站", name = "webSite", dataType = "String")
    private String webSite;

    @ApiModelProperty(value = "是否开启邮件通知消息(0否/1是)", name = "isEmailNotice", dataType = "Boolean")
    private Boolean isEmailNotice;

    @ApiModelProperty(value = "是否绑定QQ（1是/0否）", name = "bindQQ", dataType = "Boolean")
    private Boolean bindQQ;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private String createTime;

    @ApiModelProperty(value = "是否启用（1是/0否）", name = "enable", dataType = "Boolean")
    private Boolean enable;

    @ApiModelProperty(value = "登录方式", name = "loginType", dataType = "Integer")
    private Integer loginType;

    @ApiModelProperty(value = "用户角色", name = "roleList", dataType = "List<UserRoleDto>")
    private List<UserRoleDto> roleList;

    @ApiModelProperty(value = "登录IP", name = "ipAddress", dataType = "String")
    private String ipAddress;

    @ApiModelProperty(value = "IP来源", name = "ipSource", dataType = "String")
    private String ipSource;

    @ApiModelProperty(value = "经纬度", name = "rectangle", dataType = "String")
    private String rectangle;

    @ApiModelProperty(value = "最后一次登录时间", name = "lastLoginTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private String lastLoginTime;
}
