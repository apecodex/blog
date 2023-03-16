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
 * @description: 后台文章
 * @author: apecode
 * @date: 2022-07-05 14:17
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "后台文章", description = "后台文章")
public class ArticleBackDto {

    @ApiModelProperty(value = "作者", name = "author", dataType = "SimpleUserInfoDto")
    private SimpleUserInfoDto author;

    @ApiModelProperty(value = "文章id", name = "id", dataType = "String")
    private String id;

    @ApiModelProperty(value = "文章标题", name = "articleTitle", dataType = "String")
    private String articleTitle;

    @ApiModelProperty(value = "文章缩略图", name = "articleCover", dataType = "String")
    private String articleCover;

    @ApiModelProperty(value = "分类名称", name = "categoryName", dataType = "String")
    private String categoryName;

    @ApiModelProperty(value = "点赞量", name = "likeCount", dataType = "Integer")
    private Integer likeCount;

    @ApiModelProperty(value = "浏览量", name = "viewsCount", dataType = "Integer")
    private Integer viewsCount;

    @ApiModelProperty(value = "文章标签", name = "tagNames", dataType = "List<String>")
    private List<String> tagNames;

    @ApiModelProperty(value = "文章类型", name = "type", dataType = "Integer", notes = "1原创 2转载 3翻译")
    private Integer type;

    @ApiModelProperty(value = "是否置顶(0否/1是)", name = "isTop", dataType = "Boolean")
    private Boolean isTop;

    @ApiModelProperty(value = "状态值(1公开/2私密/3草稿)", name = "status", dataType = "Integer")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除（1是/0否）", name = "isDelete", dataType = "Boolean")
    private Boolean isDelete;

    @ApiModelProperty(value = "创建时间", name = "createTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间", name = "updateTime", dataType = "LocalDateTime")
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
