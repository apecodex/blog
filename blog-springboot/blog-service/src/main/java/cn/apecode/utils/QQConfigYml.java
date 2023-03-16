package cn.apecode.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: qq配置属性
 * @author: apecode
 * @date: 2023-01-05 12:56
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "qq")
public class QQConfigYml {

    @ApiModelProperty(value = "QQ appId")
    private String appId;

    @ApiModelProperty(value = "校验token地址")
    private String checkTokenUrl;

    @ApiModelProperty(value = "QQ用户信息地址")
    private String userInfoUrl;

}
