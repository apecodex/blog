package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 前台标签
 * @author: apecode
 * @date: 2022-07-04 18:06
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "前台标签", description = "前台标签展示")
public class TagFrontDto {

    @ApiModelProperty(value = "标签列表 ", name = "tags", dataType = "List<TagDto>")
    private List<TagDto> tags;

    @ApiModelProperty(value = "最新文章列表", name = "newsArticleList", dataType = "List<ArticleRecommendDto>")
    private List<ArticleRecommendDto> newsArticleList;

    @ApiModelProperty(value = "最新评论列表", name = "newsCommentList", dataType = "List<NewCommentDto>")
    private List<NewCommentDto> newsCommentList;
}
