package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: apecode
 * @date: 2022-06-26 19:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "分类选项", description = "分类选项")
public class CategoryOptionDto {

    @ApiModelProperty(value = "分类id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "分类名称", name = "name", dataType = "String")
    private String name;

}
