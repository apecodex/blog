package cn.apecode.websocket.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.security.Principal;

/**
 * @author apecode
 * @description VisitorUser
 * @date 2/7/2023 PM7:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@ApiModel(value = "游客用户", description = "游客用户")
public class VisitorUser implements Principal {

    @ApiModelProperty("游客uid")
    private String uid;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("登录ip")
    private String ipAddress;
    @ApiModelProperty("ip来源")
    private String ipSource;

    @Override
    public String getName() {
        return uid;
    }
}
