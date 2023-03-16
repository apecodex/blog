package cn.apecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "留言", description = "添加留言")
public class MessageVo {

    @NotBlank(message = "留言内容不能为空")
    @ApiModelProperty(name = "content", value = "留言内容", dataType = "String")
    private String content;
    @ApiModelProperty(name = "theme", value = "主题", dataType = "String")
    private String theme;
}
