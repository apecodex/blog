package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 分类
 * @author: apecode
 * @date: 2022-06-26 23:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "分类", description = "分类")
public class CategoryFrontDto {

    @ApiModelProperty(value = "分类列表", name = "categories", dataType = "List<CategoryArticleCountDto>")
    private List<CategoryArticleCountDto> categories;

    @ApiModelProperty(value = "最新文章列表", name = "newsArticleList", dataType = "List<ArticleRecommendDto>")
    private List<ArticleRecommendDto> newsArticleList;

    @ApiModelProperty(value = "最新评论列表", name = "newsCommentList", dataType = "List<NewCommentDto>")
    private List<NewCommentDto> newsCommentList;

}
