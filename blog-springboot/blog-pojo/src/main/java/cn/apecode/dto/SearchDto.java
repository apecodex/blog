package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 搜索
 * @author: apecode
 * @date: 2022-07-05 16:51
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "搜索结果", description = "搜索结果")
public class SearchDto {

    @ApiModelProperty(value = "搜索结果数量", name = "count", dataType = "Integer")
    private Integer count;

    @ApiModelProperty(value = "文章搜索结果", name = "articleSearch", dataType = "List<ArticleSearchDto>")
    private List<ArticleSearchDto> articleSearch;

    @ApiModelProperty(value = "分类搜索结果", name = "categorySearch", dataType = "List<CategorySearchDto>")
    private List<CategorySearchDto> categorySearch;

    @ApiModelProperty(value = "标签搜索结果", name = "tagSearch", dataType = "List<TagSearchDto>")
    private List<TagSearchDto> tagSearch;

}
