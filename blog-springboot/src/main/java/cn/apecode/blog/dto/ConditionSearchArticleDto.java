package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "条件搜索文章", description = "通过分类id标签id搜索文章")
public class ConditionSearchArticleDto {

    @ApiModelProperty(value = "文章id", name = "articleId", dataType = "String")
    private String articleId;

    @ApiModelProperty(value =  "文章标题", name = "articleTitle",  dataType = "String")
    private String articleTitle;

    @ApiModelProperty(value = "文章封面", name = "articleCover", dataType = "String")
    private String articleCover;

    @ApiModelProperty(value = "文章类型", name = "type", dataType = "String")
    private String type;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "String")
    private String createTime;

    @ApiModelProperty(value = "分类id", name = "categoryId", dataType = "String")
    private String categoryId;

    @ApiModelProperty(value = "分类名称", name = "categoryName", dataType = "String")
    private String categoryName;

    @ApiModelProperty(value = "列表", name = "tags", dataType = "TagSearchDto")
    private List<TagSearchDto> tags;
}
