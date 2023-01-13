package cn.apecode.blog.constant;

import io.swagger.annotations.ApiModelProperty;

/**
 * @description: 第三方登录参数常量
 * @author: apecode
 * @date: 2023-01-05 12:36
 **/
public class SocialLoginConst {

    @ApiModelProperty(value = "oauth2登录方式", name = "GRANT_TYPE", dataType = "String")
    public static final String GRANT_TYPE = "grant_type";

    @ApiModelProperty(value = "oauth2客户端id", name = "CLIENT_ID", dataType = "String")
    public static final String CLIENT_ID = "client_id";

    @ApiModelProperty(value = "oauth2客户端密码", name = "CLIENT_SECRET", dataType = "String")
    public static final String CLIENT_SECRET = "client_secret";

    @ApiModelProperty(value = "code", name = "CODE", dataType = "String")
    public static final String CODE = "code";

    @ApiModelProperty(value = "oauth2回调域名", name = "REDIRECT_URI", dataType = "String")
    public static final String REDIRECT_URI = "redirect_uri";

    @ApiModelProperty(value = "QQ openId", name = "QQ_OPEN_ID", dataType = "String")
    public static final String QQ_OPEN_ID = "openid";

    @ApiModelProperty(value = "访问令牌", name = "ACCESS_TOKEN", dataType = "String")
    public static final String ACCESS_TOKEN = "access_token";

    @ApiModelProperty(value = "QQ AppId", name = "OAUTH_CONSUMER_KEY", dataType = "String")
    public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
}
