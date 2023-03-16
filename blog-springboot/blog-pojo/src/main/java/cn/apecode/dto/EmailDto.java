package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 验证码邮件
 * @author: apecode
 * @date: 2022-06-09 21:14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "邮件", description = "发送邮件的实体类")
public class EmailDto {

    @ApiModelProperty(value = "邮箱号", name = "email", dataType = "String")
    private String email;

    @ApiModelProperty(value = "主题", name = "subject", dataType = "String")
    private String subject;

    @ApiModelProperty(value = "发送内容", name = "text", dataType = "String")
    private String text;

    @ApiModelProperty(value = "邮件类型", name = "type", dataType = "Integer")
    private Integer type;

    @ApiModelProperty(value = "主题id", name = "topicId", dataType = "Integer")
    private Integer topicId;

    @ApiModelProperty(value = "是否HTML", notes = "isHtml", dataType = "Boolean")
    private Boolean isHtml;
}
