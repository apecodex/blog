package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 标签
 * @author: apecode
 * @date: 2023-03-20 13:53
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "带有文章数量的标签", description = "带有文章数量的标签")
public class TagWithArticleCountDto {

    @ApiModelProperty(value = "标签id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "标签名称", name = "name", dataType = "String")
    private String name;

    @ApiModelProperty(value = "文章数量", name = "articleCount", dataType = "Integer")
    private Integer articleCount;
}
