package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 社交登录Token
 * @author: apecode
 * @date: 2023-01-04 22:38
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocialTokenDto {

    @ApiModelProperty(value = "开放id", name = "openId", dataType = "String")
    private String openId;

    @ApiModelProperty(value = "访问令牌", name = "accessToken", dataType = "String")
    private String accessToken;

    @ApiModelProperty(value = "登录类型", name = "loginType", dataType = "Integer")
    private Integer loginType;

}
