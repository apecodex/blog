package cn.apecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @description: 消息通知
 * @author: apecode
 * @date: 2022-07-15 16:55
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "消息通知", description = "消息通知")
public class NoticeVo {

    @NotBlank(message = "通知标题不能为空")
    @ApiModelProperty(value = "通知标题", name = "title", dataType = "String")
    private String title;

    @NotBlank(message = "通知内容不能为空")
    @ApiModelProperty(value = "通知内容", name = "content", dataType = "String")
    private String content;

    @ApiModelProperty(value = "跳转url", name = "url", dataType = "String")
    private String url;

    @ApiModelProperty(value = "接收的用户id", name = "userId", dataType = "String")
    private String userId;

}
