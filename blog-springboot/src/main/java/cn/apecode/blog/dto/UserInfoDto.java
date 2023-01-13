package cn.apecode.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @description: 用户信息
 * @author: apecode
 * @date: 2022-05-28 16:14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "用户信息", description = "用户登录后返回的信息")
public class UserInfoDto {

    @ApiModelProperty(value = "用户昵称", dataType = "String")
    private String nickname;

    @ApiModelProperty(value = "用户UID", dataType = "String")
    private String uid;

    @ApiModelProperty(value = "用户邮箱", dataType = "String")
    private String email;

    @ApiModelProperty(value = "头像", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "文章点赞集合", dataType = "Set<Object>")
    private Set<Object> articleLikeSet;

    @ApiModelProperty(value = "评论点赞集合", dataType = "Set<Object>")
    private Set<Object> commentLikeSet;

    @ApiModelProperty(value = "说说点赞集合", dataType = "Set<Object>")
    private Set<Object> talkLikeSet;

    @ApiModelProperty(value = "Jwt Token", dataType = "String")
    private String token;

    @ApiModelProperty(value = "Token Head", dataType = "String")
    private String tokenHead;
}
