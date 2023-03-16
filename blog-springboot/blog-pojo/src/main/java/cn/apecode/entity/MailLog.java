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
 * 邮件发送记录
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Accessors(chain = true)
@TableName("t_mail_log")
@ApiModel(value = "MailLog对象", description = "邮件发送记录")
public class MailLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("消息id")
    @TableId(value = "msgId", type = IdType.AUTO)
    private String msgId;

    @ApiModelProperty("接收邮箱")
    private String email;

    @ApiModelProperty("邮件发送类型(1验证码/2评论/3通知/4留言)")
    private Integer type;

    @ApiModelProperty("主题id")
    private Integer topicId;

    @ApiModelProperty("状态（0:消息投递中 1:投递成功 2:投递失败）")
    private Integer status;

    @ApiModelProperty("路由键")
    private String routeKey;

    @ApiModelProperty("交换机")
    private String exchange;

    @ApiModelProperty("重试次数")
    private Integer count;

    @ApiModelProperty("重试时间")
    private LocalDateTime tryTime;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
