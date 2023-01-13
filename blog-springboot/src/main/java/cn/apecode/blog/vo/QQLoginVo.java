package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @description: QQ登录
 * @author: apecode
 * @date: 2023-01-05 12:15
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "QQ登录信息", description = "QQ登录信息")
public class QQLoginVo {

    @NotBlank(message = "openId不能为空")
    @ApiModelProperty(name = "openId", value = "qq openId", dataType = "String")
    private String openId;

    @NotBlank(message = "accessToken不能为空")
    @ApiModelProperty(name = "accessToken", value = "qq accessToken", dataType = "String")
    private String accessToken;

}
