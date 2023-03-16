package cn.apecode.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 社交账号信息
 * @author: apecode
 * @date: 2023-01-05 11:06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialUserInfoDto {

    @ApiModelProperty(value = "用户昵称", name = "nickname", dataType = "String")
    private String nickname;

    @ApiModelProperty(value = "用户头像", name = "avatar", dataType = "String")
    private String avatar;

}
