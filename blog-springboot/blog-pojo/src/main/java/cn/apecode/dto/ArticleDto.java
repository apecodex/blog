package cn.apecode.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 文章
 * @author: apecode
 * @date: 2022-07-04 00:03
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "文章", description = "文章")
public class ArticleDto {

    @ApiModelProperty(value = "作者", name = "author", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto author;

    @ApiModelProperty(value = "文章id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "文章标题", name = "articleTitle", dataType = "String")
    private String articleTitle;

    @ApiModelProperty(value = "文章缩略图", name = "articleCover", dataType = "String")
    private String articleCover;

    @ApiModelProperty(value = "文章内容", name = "articleContent", required = true, dataType = "String")
    private String articleContent;

    @ApiModelProperty(value = "分类id", name = "categoryId", dataType = "String")
    private String categoryId;

    @ApiModelProperty(value = "分类名称", name = "categoryName", dataType = "String")
    private String categoryName;

    @ApiModelProperty(value = "点赞量", name = "likeCount", dataType = "Integer")
    private Integer likeCount;

    @ApiModelProperty(value = "浏览量", name = "viewsCount", dataType = "Integer")
    private Integer viewsCount;

    @ApiModelProperty(value = "文章标签", name = "tags", dataType = "List<TagDto>")
    private List<TagDto> tags;

    @ApiModelProperty(value = "文章类型", name = "type", dataType = "String", notes = "1原创 2转载 3翻译")
    private String type;

    @ApiModelProperty(value = "原文链接", name = "originalUrl", dataType = "String")
    private String originalUrl;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", name = "updateTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "上一篇文章", dataType = "ArticlePaginationDto")
    private ArticlePaginationDto lastArticle;

    @ApiModelProperty(value = "下一篇文章", dataType = "ArticlePaginationDto")
    private ArticlePaginationDto nextArticle;

    @ApiModelProperty(value = "推荐文章列表", dataType = "List<ArticleRecommendDto>")
    private List<ArticleRecommendDto> recommendArticleList;

    @ApiModelProperty(value = "最新文章列表", dataType = "List<ArticleRecommendDto>")
    private List<ArticleRecommendDto> newsArticleList;

    @ApiModelProperty(value = "最新评论列表", dataType = "List<NewCommentDto>")
    private List<NewCommentDto> newsCommentList;

    @ApiModelProperty(value = "当前访问用户的经纬度", name = "rectangle", dataType = "String")
    private String rectangle;

}
