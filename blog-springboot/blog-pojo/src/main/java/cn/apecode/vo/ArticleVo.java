package cn.apecode.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @description: 文章
 * @author: apecode
 * @date: 2022-06-27 15:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "文章", description = "文章")
public class ArticleVo {

    @ApiModelProperty(name = "id", value = "文章id", dataType = "String")
    private String id;

    @NotBlank(message = "文章标题不能为空")
    @ApiModelProperty(name = "articleTitle", value = "文章标题", required = true, dataType = "String")
    private String articleTitle;

    @ApiModelProperty(name = "articleCover", value = "文章缩略图", dataType = "String")
    private String articleCover;

    @ApiModelProperty(name = "categoryName", value = "文章分类", dataType = "String")
    private String categoryName;

    @NotBlank(message = "文章内容不能为空")
    @ApiModelProperty(name = "articleContent", value = "文章内容", required = true, dataType = "String")
    private String articleContent;

    @ApiModelProperty(name = "tagNameList", value = "文章标签", dataType = "List<String>")
    private List<String> tagNameList;

    @ApiModelProperty(name = "type", value = "文章类型", dataType = "Integer")
    private Integer type;

    @ApiModelProperty(name = "originalUrl", value = "原文链接", dataType = "String")
    private String originalUrl;

    @ApiModelProperty(name = "isTop", value = "是否置顶", dataType = "Boolean")
    private Boolean isTop;

    @ApiModelProperty(name = "status", value = "文章状态", dataType = "String", notes = "1.公开 2.私密 3.评论可见")
    private Integer status;
}
