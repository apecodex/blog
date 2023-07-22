package cn.apecode.websocket.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author apecode
 * @description OnlineUser
 * @date 2/7/2023 PM6:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "在线用户", description = "在线用户")
public class OnlineUser {

    @ApiModelProperty(value = "用户uid")
    private String uid;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "头像")
    private String avatar;
    @ApiModelProperty(value = "IP")
    private String ipAddress;
    @ApiModelProperty(value = "归属地")
    private String ipSource;

}
