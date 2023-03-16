package cn.apecode.mapper;

import cn.apecode.dto.*;
import cn.apecode.entity.Article;
import cn.apecode.vo.ConditionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章信息 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * @description: 查看文章的推荐文章
     * @param articleId
     * @return {@link List< ArticleRecommendDto >}
     * @auther apecode
     * @date 2022/7/4 23:24
    */
    List<ArticleRecommendDto> listRecommendArticle(@Param("articleId") Integer articleId);

    /**
     * @description: 查询id对应文章
     * @param articleId
     * @return {@link ArticleDto}
     * @auther apecode
     * @date 2022/7/4 23:56
    */
    ArticleDto getArticleById(@Param("articleId") Integer articleId);

    /**
     * @description: 获取首页文章
     * @param limitCurrent
     * @param size
     * @return {@link List< ArticleHomeDto >}
     * @auther apecode
     * @date 2022/7/5 12:37
    */
    List<ArticleHomeDto> listArticle(@Param("current") Long limitCurrent, @Param("size") Long size);

    /**
     * @description: 获取后台文章
     * @param limitCurrent
     * @param size
     * @param condition
     * @return {@link List< ArticleBackDto >}
     * @auther apecode
     * @date 2022/7/5 15:39
    */
    List<ArticleBackDto> listArticleBack(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("condition") ConditionVo condition);

    /**
     * @description: 查询后台文章总量
     * @param condition
     * @return {@link Integer}
     * @auther apecode
     * @date 2022/7/5 16:16
    */
    Integer articleBackCount(@Param("condition") ConditionVo condition);

    /**
     * @description: 文章统计列表
     * @return {@link List< ArticleStatisticsDto >}
     * @auther apecode
     * @date 2022/7/14 0:14
    */
    List<ArticleStatisticsDto> listArticleStatistics();


    /**
     * @description: 根据条件搜索文章
     * @param limitCurrent
     * @param size
     * @param conditionVo
     * @return {@link List<ConditionSearchArticleDto>}
     * @auther apecode
     * @date 16/10/2022 PM7:04
    */
    List<ConditionSearchArticleDto> getArticlesByCondition(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("condition") ConditionVo conditionVo);

    /**
     * @description: 获取文章归档
     * @return {@link List<ArchiveMonthDto>}
     * @auther apecode
     * @date 10/11/2022 PM3:21
    */
    List<ArchiveMonthDto> listArchives();

    /**
     * @description: 数量
     * @param conditionVo
     * @return {@link int}
     * @auther apecode
     * @date 14/11/2022 PM2:17
    */
    int getArticlesByConditionCount(@Param("condition") ConditionVo conditionVo);
}
