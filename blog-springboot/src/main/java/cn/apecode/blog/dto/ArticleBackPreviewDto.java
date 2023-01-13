package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 后台文章预览
 * @author: apecode
 * @date: 2022-07-05 13:45
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台文章预览", description = "后台文章预览")
public class ArticleBackPreviewDto {

    @ApiModelProperty(value = "文章标题", name = "articleTitle", dataType = "String")
    private String articleTitle;

    @ApiModelProperty(value = "文章缩略图", name = "articleCover", dataType = "String")
    private String articleCover;

    @ApiModelProperty(value = "文章内容", name = "articleContent", required = true, dataType = "String")
    private String articleContent;

    @ApiModelProperty(value = "分类名称", name = "categoryName", dataType = "String")
    private String categoryName;

    @ApiModelProperty(value = "文章标签", name = "tagName", dataType = "List<String>")
    private List<String> tagName;

    @ApiModelProperty(value = "文章类型", name = "type", dataType = "Integer", notes = "1原创 2转载 3翻译")
    private Integer type;

    @ApiModelProperty(value = "原文链接", name = "originalUrl", dataType = "String")
    private String originalUrl;
}
