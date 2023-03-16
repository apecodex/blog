package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 文章上下篇
 * @author: apecode
 * @date: 2022-07-04 23:13
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "文章上下篇", description = "文章上下篇")
public class ArticlePaginationDto {

    @ApiModelProperty(value = "文章id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "文章标题", name = "articleTitle", dataType = "String")
    private String articleTitle;

    @ApiModelProperty(value = "文章缩略图", name = "articleCover", dataType = "String")
    private String articleCover;

}
