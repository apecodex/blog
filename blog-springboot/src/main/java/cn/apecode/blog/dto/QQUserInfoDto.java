package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: QQ用户信息
 * @author: apecode
 * @date: 2023-01-05 14:22
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QQUserInfoDto {

    @ApiModelProperty(value = "用户昵称", name = "nickname", dataType = "String")
    private String nickname;

    @ApiModelProperty(value = "用户QQ头像", name = "figureurl_qq_1", dataType = "String")
    private String figureurl_qq_2;
}
