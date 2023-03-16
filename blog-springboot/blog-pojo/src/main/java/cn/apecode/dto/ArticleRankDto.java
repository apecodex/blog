package cn.apecode.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 文章浏览量排行
 * @author: apecode
 * @date: 2022-07-13 23:24
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "文章浏览量排行", description = "文章浏览量排行")
public class ArticleRankDto {

    @ApiModelProperty(value = "标题", name = "articleTitle", dataType = "String")
    private String articleTitle;

    @ApiModelProperty(value = "浏览量", name = "viewsCount", dataType = "Integer")
    private Integer viewsCount;

}
