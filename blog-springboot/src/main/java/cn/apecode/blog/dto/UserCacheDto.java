package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @description: 用户信息缓存
 * @author: apecode
 * @date: 2022-05-29 23:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
@ApiModel(value = "用户信息缓存", description = "用户信息缓存")
public class UserCacheDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户账号id")
    private Integer id;

    @ApiModelProperty("用户账号")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("用户UID")
    private String uid;

    @ApiModelProperty("用户信息id")
    private Integer userInfoId;

    @ApiModelProperty("登录类型")
    private Integer loginType;

    @ApiModelProperty("是否启用（1是/0否）")
    private Boolean enable;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户角色")
    private List<String> roleList;

}
