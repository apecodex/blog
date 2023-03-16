package cn.apecode.service;

import cn.apecode.dto.*;
import cn.apecode.entity.Article;
import cn.apecode.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 文章信息 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface ArticleService extends IService<Article> {

    /**
     * @description: 添加文章
     * @param article
     * @auther apecode
     * @date 2022/6/27 15:28
    */
    void saveOrUpdateArticle(ArticleVo article);

    /**
     * @description: 恢复或删除文章
     * @param delete
     * @auther apecode
     * @date 2022/6/27 23:30
    */
    void updateArticleDelete(DeleteVo delete);

    /**
     * @description: 物理删除文章
     * @param articleIds
     * @auther apecode
     * @date 2022/6/27 23:42
    */
    void deleteArticle(List<String> articleIds);

    /**
     * @description: 上传文章封面
     * @param file
     * @return {@link UploadFileInfoDto}
     * @auther apecode
     * @date 2022/6/28 0:11
    */
    UploadFileInfoDto uploadArticleCover(MultipartFile file);

    /**
     * @description: 修改文章置顶
     * @param top
     * @auther apecode
     * @date 2022/6/28 15:24
    */
    void updateArticleTop(TopVo top);

    /**
     * @description: 获取文章归档
     * @return {@link List<ArchiveMonthDto>}
     * @auther apecode
     * @date 2022/6/28 16:11
    */
    List<ArchiveMonthDto> listArchives();

    /**
     * @description: 根据id获取文章
     * @param articleId
     * @return {@link ArticleDto}
     * @auther apecode
     * @date 2022/7/4 23:19
    */
    ArticleDto getArticleByArticleId(String articleId);

    /**
     * @description: 获取首页文章
     * @return {@link PageResult< ArticleHomeDto >}
     * @auther apecode
     * @date 2022/7/5 12:34
    */
    PageResult<ArticleHomeDto> listArticle();

    /**
     * @description: 根据id获取后台文章
     * @param articleId
     * @return {@link ArticleVo}
     * @auther apecode
     * @date 2022/7/5 13:51
    */
    ArticleVo getArticleBackByArticleId(String articleId);

    /**
     * @description: 获取后台文章
     * @param condition
     * @return {@link PageResult< ArticleBackDto >}
     * @auther apecode
     * @date 2022/7/5 14:35
    */
    PageResult<ArticleBackDto> listArticleBack(ConditionVo condition);

    /**
     * @description: 搜索
     * @param keywords
     * @return {@link SearchDto}
     * @auther apecode
     * @date 2022/7/5 17:15
    */
    SearchDto search(String keywords);

    /**
     * @description: 点赞文章
     * @param articleId
     * @auther apecode
     * @date 2022/6/28 23:25
     */
    void saveArticleLike(String articleId);

    /**
     * @description: 根据条件搜索文章
     * @param conditionVo
     * @return {@link PageResultWithObject<ConditionSearchArticleDto, String>}
     * @auther apecode
     * @date 16/10/2022 PM4:53
    */
    PageResultWithObject<ConditionSearchArticleDto, String> getArticlesByCondition(ConditionVo conditionVo);

}
