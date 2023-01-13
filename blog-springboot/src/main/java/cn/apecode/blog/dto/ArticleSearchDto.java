package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 搜索文章
 * @author: apecode
 * @date: 2022-07-05 16:58
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "搜索文章", description = "搜索文章")
public class ArticleSearchDto {

    @ApiModelProperty(value = "文章id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "文章标题", name = "articleTitle", dataType = "String")
    private String articleTitle;

    @ApiModelProperty(value = "文章内容", name = "articleContent", dataType = "String")
    private String articleContent;

}
