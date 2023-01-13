package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: qq token信息
 * @author: apecode
 * @date: 2023-01-05 13:34
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QQTokenDto {

    @ApiModelProperty(value = "openId", name = "openid", dataType = "String")
    private String openid;

    @ApiModelProperty(value = "clientId", name = "client_id", dataType = "String")
    private String client_id;

}
