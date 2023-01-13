package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 标签搜索结果
 * @author: apecode
 * @date: 2022-07-05 17:11
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "标签搜索结果", description = "标签搜索结果")
public class TagSearchDto {

    @ApiModelProperty(value = "标签id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "标签名称", name = "name", dataType = "String")
    private String name;

}
