package cn.apecode.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户账号
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_user_auth")
@ApiModel(value = "UserAuth对象", description = "用户账号")
public class UserAuth implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户账号id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户账号")
    private String username;

    @ApiModelProperty("用户密码")
    private String password;

    @ApiModelProperty("用户信息id")
    private Integer userInfoId;

    @ApiModelProperty("登录类型")
    private Integer loginType;

    @ApiModelProperty("登录IP")
    private String ipAddress;

    @ApiModelProperty("经纬度")
    private String rectangle;

    @ApiModelProperty("登录来源")
    private String ipSource;

    @ApiModelProperty("访问浏览器")
    private String browser;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("最后一次登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("是否启用（1是/0否）")
    private Boolean enable;

    @ApiModelProperty("逻辑删除（1是/0否）")
    private Boolean isDelete;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
