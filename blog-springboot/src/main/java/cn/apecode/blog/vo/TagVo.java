package cn.apecode.blog.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @description: 标签
 * @author: apecode
 * @date: 2022-06-26 00:05
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "标签", description = "修改标签")
public class TagVo {

    @ApiModelProperty(name = "id", value = "标签id", dataType = "String", required = true)
    private String id;

    @NotBlank(message = "标签名不能为空")
    @Size(max = 20, message = "最多20个字")
    @ApiModelProperty(name = "name", value = "标签名称", dataType = "String", required = true)
    private String name;

}
