package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "分类文章数量", description = "分类文章数量")
public class CategoryArticleCountDto {

    @ApiModelProperty(value = "id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "分类名称", name = "name", dataType = "String")
    private String name;

    @ApiModelProperty(value = "文章数量", name = "articleCount", dataType = "Integer")
    private Integer articleCount;

}
