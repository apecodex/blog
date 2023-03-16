package cn.apecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;

/**
 * @description: 说说
 * @author: apecode
 * @date: 2022-07-06 13:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "说说", description = "添加或修改说说")
public class TalkVo {

    @ApiModelProperty(value = "说说id", name = "id", dataType = "String")
    private String id;

    @NotBlank(message = "说说内容不能为空")
    @ApiModelProperty(value = "说说内容", name = "content", dataType = "String")
    private String content;

    @ApiModelProperty(value = "是否顶置", name = "isTop", dataType = "Boolean")
    private Boolean isTop;

    @ApiModelProperty(value = "状态(1公开/2私密/3草稿)", name = "status", dataType = "Integer")
    private Integer status;

    @ApiModelProperty(value = "配图", name = "files", dataType = "ArrayList<String>")
    private ArrayList<String> files;

}
