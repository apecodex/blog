package cn.apecode.blog.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_user_info")
@Builder
@ApiModel(value = "UserInfo对象", description = "用户信息")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户信息id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户唯一id")
    private String uid;

    @ApiModelProperty("用户邮箱")
    private String email;

    @ApiModelProperty("用户头像")
    private String avatar;

    @ApiModelProperty("用户简介")
    private String intro;

    @ApiModelProperty("用户网站")
    private String webSite;

    @ApiModelProperty("是否开启邮件通知消息(0否/1是)")
    private Boolean isEmailNotice;

    @ApiModelProperty("是否绑定QQ（1是/0否）")
    private Boolean bindQQ;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
