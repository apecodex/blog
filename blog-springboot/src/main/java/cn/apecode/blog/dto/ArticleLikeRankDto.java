package cn.apecode.blog.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 文章点赞排行
 * @author: apecode
 * @date: 2022-07-13 23:26
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "文章点赞排行", description = "文章点赞排行")
public class ArticleLikeRankDto {

    @ApiModelProperty(value = "标题", name = "articleTitle", dataType = "String")
    private String articleTitle;

    @ApiModelProperty(value = "点赞量", name = "likeCount", dataType = "Integer")
    private Integer likeCount;

}
