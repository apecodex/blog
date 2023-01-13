package cn.apecode.blog.dto;

import cn.apecode.blog.entity.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 后台博客信息
 * @author: apecode
 * @date: 2022-07-13 23:10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "博客后台信息", description = "博客后台信息")
public class BlogBackInfoDto {

    @ApiModelProperty(value = "文章数量", dataType = "Integer")
    private Integer articleCount;

    @ApiModelProperty(value = "访问量", dataType = "Integer")
    private Integer viewsCount;

    @ApiModelProperty(value = "独立用户访问量", dataType = "Integer")
    private Integer onlyViewCount;

    @ApiModelProperty(value = "用户量", dataType = "Integer")
    private Integer userCount;

    @ApiModelProperty(value = "留言量", dataType = "Integer")
    private Integer messageCount;

    @ApiModelProperty(value = "分类量", dataType = "categoryCount")
    private Integer categoryCount;

    @ApiModelProperty(value = "标签量", dataType = "Integer")
    private Integer tagCount;

    @ApiModelProperty(value = "说说量", dataType = "Integer")
    private Integer talkCount;

    @ApiModelProperty(value = "未阅通知量", dataType = "Integer")
    private Integer noReadNoticeCount;

    @ApiModelProperty(value = "用户访问区域统计", dataType = "List<UserVisitAreaStatisticsDto>")
    private List<UserVisitAreaStatisticsDto> userVisitAreaStatisticsList;

    @ApiModelProperty(value = "文章统计列表", dataType = "List<ArticleStatisticsDto>")
    private List<ArticleStatisticsDto> articleStatisticsList;

    @ApiModelProperty(value = "用户7天访问量统计列表", dataType = "List<VisitStatisticsDto>")
    private List<VisitStatisticsDto> visitStatisticsList;

    @ApiModelProperty(value = "文章浏览量排行", dataType = "List<ArticleRankDto>")
    private List<ArticleRankDto> articleRankList;

    @ApiModelProperty(value = "文章点赞排行", dataType = "List<ArticleLikeRankDto>")
    private List<ArticleLikeRankDto> articleLikeRankList;

    @ApiModelProperty(value = "文章分类", dataType = "List<CategoryFrontDto>")
    private List<CategoryArticleCountDto> categoryList;

    @ApiModelProperty(value = "文章标签", dataType = "List<String>")
    private List<String> tagsName;

    @ApiModelProperty(value = "系统内存信息", dataType = "MemoryInfoDto")
    private MemoryInfoDto systemMemory;

    @ApiModelProperty(value = "系统硬盘信息", dataType = "List<DiskInfoDto>")
    private List<DiskInfoDto> systemDiskInfo;
}
