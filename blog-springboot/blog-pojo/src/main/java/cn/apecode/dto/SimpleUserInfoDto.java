package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 部分用户信息
 * @author: apecode
 * @date: 2022-06-23 13:50
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "少部分用户信息", description = "少部分用户信息")
public class SimpleUserInfoDto {

    @ApiModelProperty(value = "8位数id", name = "uid", dataType = "String")
    private String uid;

    @ApiModelProperty(value = "用户名", name = "nickname", dataType = "String")
    private String nickname;

    @ApiModelProperty(value = "用户头像", name = "avatar", dataType = "String")
    private String avatar;

    @ApiModelProperty(value = "用户简介", name = "intro", dataType = "String")
    private String intro;

    @ApiModelProperty(value = "用户网站", name = "webSite", dataType = "String")
    private String webSite;

}
