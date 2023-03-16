package cn.apecode.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @description: 邮件日志
 * @author: apecode
 * @date: 2022-07-13 18:57
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台邮件日志", description = "后台邮件日志")
public class MailLogBackDto {

    @ApiModelProperty(value = "消息id", name = "msgId", dataType = "String")
    private String msgId;

    @ApiModelProperty(value = "主题id", name = "topicId", dataType = "String")
    private String topicId;

    @ApiModelProperty(value = "接收邮箱", name = "email", dataType = "String")
    private String email;

    @ApiModelProperty(value = "邮件发送类型(1验证码/2评论/3通知)", name = "type", dataType = "String")
    private String type;

    @ApiModelProperty(value = "状态（0:消息投递中 1:投递成功 2:投递失败）", name = "status", dataType = "Integer")
    private Integer status;

    @ApiModelProperty(value = "路由键", name = "routeKey", dataType = "String")
    private String routeKey;

    @ApiModelProperty(value = "交换机", name = "exchange", dataType = "String")
    private String exchange;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
