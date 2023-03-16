package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 分类搜索结果
 * @author: apecode
 * @date: 2022-07-05 17:01
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "分类搜索结果", description = "分类搜索结果")
public class CategorySearchDto {

    @ApiModelProperty(value = "分类id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "分类名称", name = "name", dataType = "String")
    private String name;

}
