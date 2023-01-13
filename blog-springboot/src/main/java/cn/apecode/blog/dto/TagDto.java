package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 标签
 * @author: apecode
 * @date: 2022-06-25 17:40
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "标签", description = "标签")
public class TagDto {

    @ApiModelProperty(value = "标签id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "标签名称", name = "name", dataType = "String")
    private String name;

}
