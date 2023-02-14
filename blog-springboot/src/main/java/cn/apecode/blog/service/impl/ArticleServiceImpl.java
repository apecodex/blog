package cn.apecode.blog.service.impl;

import cn.apecode.blog.dto.*;
import cn.apecode.blog.entity.*;
import cn.apecode.blog.enums.ArticleTypeEnum;
import cn.apecode.blog.enums.FilePathEnum;
import cn.apecode.blog.exception.BizException;
import cn.apecode.blog.mapper.*;
import cn.apecode.blog.service.ArticleService;
import cn.apecode.blog.service.ArticleTagService;
import cn.apecode.blog.service.RedisService;
import cn.apecode.blog.service.TagService;
import cn.apecode.blog.strategy.context.UploadFileStrategyContext;
import cn.apecode.blog.utils.*;
import cn.apecode.blog.vo.*;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static cn.apecode.blog.constant.RedisPrefixConst.*;
import static cn.apecode.blog.enums.ArticleStatusEnum.DRAFT;
import static cn.apecode.blog.constant.CommonConst.PRE_TAG;
import static cn.apecode.blog.constant.CommonConst.POST_TAG;
import static cn.apecode.blog.enums.ArticleTypeEnum.getArticleTypeEnum;

/**
 * <p>
 * 文章信息 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final ArticleTagMapper articleTagMapper;
    private final ArticleTagService articleTagService;
    private final TagService tagService;
    private final TagMapper tagMapper;
    private final CommentMapper commentMapper;
    private final RedisService redisService;
    private final UploadFileStrategyContext uploadFileStrategyContext;
    private final HttpServletRequest request;

    /**
     * @param article
     * @description: 添加文章
     * @auther apecode
     * @date 2022/6/27 15:29
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateArticle(ArticleVo article) {
        Article articleEntity = BeanCopyUtils.copyObject(article, Article.class);
        // 判断文章是否存在
        if (StringUtils.isNotBlank(article.getId())) {
            Article hasArticle = getArticle(article.getId());
            if (Objects.isNull(hasArticle)) throw new BizException("文章不存在");
            articleEntity.setId(hasArticle.getId());
        } else {
            articleEntity.setUpdateTime(CommonUtils.getLocalDateTime());
        }
        Category category = saveArticleCategory(article);
        if (Objects.nonNull(category)) {
            articleEntity.setCategoryId(category.getId());
        }
        articleEntity.setUserId(UserUtils.getLoginUser().getUserInfoId());
        this.saveOrUpdate(articleEntity);
        // 保存标签
        saveArticleTags(article, articleEntity.getId());
    }

    /**
     * @param delete
     * @description: 恢复或删除文章
     * @auther apecode
     * @date 2022/6/27 23:30
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArticleDelete(DeleteVo delete) {
        // 逻辑删除
        List<Article> articleList = delete.getIdList().stream().map(articleId -> {
            Integer id = SecurityUtils.decrypt(articleId);
            if (Objects.isNull(id)) throw new BizException("文章路径 '" + articleId + "' 有误");
            return Article.builder()
                    .id(id)
                    .isDelete(delete.getIsDelete())
                    .build();
        }).collect(Collectors.toList());
        this.updateBatchById(articleList);
    }

    /**
     * @param articlePaths
     * @description: 物理删除文章
     * @auther apecode
     * @date 2022/6/27 23:43
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteArticle(List<String> articlePaths) {
        List<Integer> articleIds = articlePaths.stream().map(path -> {
            Integer id = SecurityUtils.decrypt(path);
            if (Objects.isNull(id)) throw new BizException("文章路径 '" + path + "' 有误");
            return id;
        }).collect(Collectors.toList());
        // 删除文章关联的标签
        articleTagMapper.delete(Wrappers.<ArticleTag>lambdaQuery().in(ArticleTag::getArticleId, articleIds));
        // 删除文章
        articleMapper.deleteBatchIds(articleIds);
    }

    /**
     * @param file
     * @return {@link UploadFileInfoDto}
     * @description: 上传文章封面
     * @auther apecode
     * @date 2022/6/27 23:58
     */
    @Override
    public UploadFileInfoDto uploadArticleCover(MultipartFile file) {
        return uploadFileStrategyContext.executeUploadFileStrategy(file, FilePathEnum.ARTICLE.getPath());
    }

    /**
     * @param top
     * @description: 修改文章置顶
     * @auther apecode
     * @date 2022/6/28 15:24
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArticleTop(TopVo top) {
        Article hasArticle = getArticle(top.getId());
        if (Objects.isNull(hasArticle)) throw new BizException("文章不存在");
        Article article = Article.builder()
                .id(hasArticle.getId())
                .isTop(top.getIsTop())
                .build();
        articleMapper.updateById(article);
    }

    /**
     * @return {@link List<ArchiveMonthDto>}
     * @description: 获取文章归档
     * @auther apecode
     * @date 2022/6/28 16:11
     */
    @Override
    public List<ArchiveMonthDto> listArchives() {
        List<ArchiveMonthDto> archiveMonthDtos = articleMapper.listArchives();
        // 处理id
        archiveMonthDtos.stream().peek((articleMonth) -> articleMonth.getArticles().stream().peek((articleItem) -> {
            articleItem.setId(SecurityUtils.encrypt(articleItem.getId()));
        }).collect(Collectors.toList())).collect(Collectors.toList());
        return archiveMonthDtos;
    }

    /**
     * @param articlePath
     * @return {@link ArticleDto}
     * @description: 根据id获取文章
     * @auther apecode
     * @date 2022/7/4 23:19
     */
    @Override
    public ArticleDto getArticleByArticleId(String articlePath) {
        Integer articleId = SecurityUtils.decrypt(articlePath);
        if (Objects.isNull(articleId)) throw new BizException("文章id有误");
        // 查询推荐文章
        CompletableFuture<List<ArticleRecommendDto>> recommendArticleList = CompletableFuture.supplyAsync(() -> articleMapper.listRecommendArticle(articleId).stream().peek(article -> article.setId(SecurityUtils.encrypt(article.getId()))).collect(Collectors.toList()));
        // 查询最新文章
        CompletableFuture<List<ArticleRecommendDto>> newsArticleList = CompletableFuture.supplyAsync(() -> {
            List<Article> articleList = articleMapper.selectList(Wrappers.<Article>lambdaQuery()
                    .select(Article::getId, Article::getArticleTitle, Article::getArticleCover, Article::getCreateTime)
                    .eq(Article::getIsDelete, 0)
                    .eq(Article::getStatus, 1)
                    .orderByDesc(Article::getId)
                    .last("limit 5")
            );
            return articleList.stream().map(article -> ArticleRecommendDto.builder()
                    .id(SecurityUtils.encrypt(String.valueOf(article.getId())))
                    .articleTitle(article.getArticleTitle())
                    .articleCover(article.getArticleCover())
                    .createTime(article.getCreateTime())
                    .build()).collect(Collectors.toList());
        });
        // 获取最新评论
        CompletableFuture<List<NewCommentDto>> newsCommentList = CompletableFuture.supplyAsync(() -> {
            List<NewCommentDto> newCommentDtos = commentMapper.getNewsComment();
            return newCommentDtos.stream().peek((comment) -> {
                comment.setTopicId(SecurityUtils.encrypt(comment.getTopicId()));
            }).collect(Collectors.toList());
        });
        // 查询id对应文章
        ArticleDto article = articleMapper.getArticleById(articleId);
        if (Objects.isNull(article)) throw new BizException("文章不存在");
        // 处理id
        article.setId(SecurityUtils.encrypt(article.getId()));
        article.setType(ArticleTypeEnum.getArticleTypeEnum(Integer.valueOf(article.getType())).getDesc());
        article.setCategoryId(SecurityUtils.encrypt(article.getCategoryId()));
        article.getTags().stream().peek(tag -> tag.setId(SecurityUtils.encrypt(tag.getId()))).collect(Collectors.toList());
        // 更新文章浏览量
        updateArticleViewsCount(articleId);
        // 查询上一篇
        Article lastArticle = articleMapper.selectOne(Wrappers.<Article>lambdaQuery()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDelete, 0)
                .eq(Article::getStatus, 1)
                .lt(Article::getId, articleId)
                .orderByDesc(Article::getId)
                .last("limit 1"));
        // 查询下一篇
        Article nextArticle = articleMapper.selectOne(Wrappers.<Article>lambdaQuery()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDelete, 0)
                .eq(Article::getStatus, 1)
                .gt(Article::getId, articleId)
                .orderByAsc(Article::getId)
                .last("limit 1"));
        if (Objects.nonNull(lastArticle)) {
            article.setLastArticle(ArticlePaginationDto.builder()
                    .id(SecurityUtils.encrypt(String.valueOf(lastArticle.getId())))
                    .articleTitle(lastArticle.getArticleTitle())
                    .articleCover(lastArticle.getArticleCover())
                    .build());
        }
        if (Objects.nonNull(nextArticle)) {
            article.setNextArticle(ArticlePaginationDto.builder()
                    .id(SecurityUtils.encrypt(String.valueOf(nextArticle.getId())))
                    .articleTitle(nextArticle.getArticleTitle())
                    .articleCover(nextArticle.getArticleCover())
                    .build());
        }
        // 封装点赞量和浏览量
        Double viewCount = redisService.zScore(ARTICLE_VIEWS_COUNT, articleId);
        if (Objects.nonNull(viewCount)) {
            article.setViewsCount(viewCount.intValue());
        }
        Double likeCount = redisService.zScore(ARTICLE_LIKE_COUNT, articleId);
        if (Objects.nonNull(likeCount)) {
            article.setLikeCount(likeCount.intValue());
        }
        // 设置访问用户经纬度
        String rectangleEncrypt = IpUtils.getNowUserRectangleEncrypt(request);
        if (StringUtils.isNotBlank(rectangleEncrypt)) {
            article.setRectangle(rectangleEncrypt);
        }
        // 封装文章信息
        try {
            article.setRecommendArticleList(recommendArticleList.get());
            article.setNewsArticleList(newsArticleList.get());
            article.setNewsCommentList(newsCommentList.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return article;
    }

    /**
     * @return {@link PageResult<ArticleHomeDto>}
     * @description: 获取首页文章
     * @auther apecode
     * @date 2022/7/5 12:34
     */
    @Override
    public PageResult<ArticleHomeDto> listArticle() {
        Long count = articleMapper.selectCount(Wrappers.<Article>lambdaQuery().eq(Article::getIsDelete, 0).eq(Article::getStatus, 1));
        List<ArticleHomeDto> articleHomeDtoList = articleMapper.listArticle(PageUtils.getLimitCurrent(), PageUtils.getSize());
        articleHomeDtoList.stream().peek(article -> {
            // 封装点赞量和浏览量
            Double viewCount = redisService.zScore(ARTICLE_VIEWS_COUNT, Integer.valueOf(article.getId()));
            if (Objects.nonNull(viewCount)) {
                article.setViewsCount(viewCount.intValue());
            }
            Double likeCount = redisService.zScore(ARTICLE_LIKE_COUNT, Integer.valueOf(article.getId()));
            if (Objects.nonNull(likeCount)) {
                article.setLikeCount(likeCount.intValue());
            }
            // 处理id
            article.setId(SecurityUtils.encrypt(article.getId()));
            article.setCategoryId(SecurityUtils.encrypt(article.getCategoryId()));
            article.getTags().stream().peek(tag -> tag.setId(SecurityUtils.encrypt(tag.getId()))).collect(Collectors.toList());
            article.setType(Objects.requireNonNull(getArticleTypeEnum(Integer.valueOf(article.getType()))).getDesc());
        }).collect(Collectors.toList());
        return new PageResult<>(articleHomeDtoList, count.intValue());
    }

    /**
     * @param articleId
     * @return {@link ArticleVo}
     * @description: 根据id获取后台文章
     * @auther apecode
     * @date 2022/7/5 13:51
     */
    @Override
    public ArticleVo getArticleBackByArticleId(String articleId) {
        Integer id = SecurityUtils.decrypt(articleId);
        if (Objects.isNull(id)) throw new BizException("文章id有误");
        // 查询文章信息
        Article article = articleMapper.selectById(id);
        // 查询文章分类
        Category category = categoryMapper.selectById(article.getCategoryId());
        // 查询文章标签
        List<String> listTagName = tagMapper.listTagNameByArticleId(id);
        // 封装数据
        ArticleVo articleVo = BeanCopyUtils.copyObject(article, ArticleVo.class);
        articleVo.setId(SecurityUtils.encrypt(article.getId().toString()));
        articleVo.setCategoryName(category.getName());
        articleVo.setTagNameList(listTagName);
        return articleVo;
    }

    /**
     * @param condition
     * @return {@link PageResult<ArticleBackDto>}
     * @description: 获取后台文章
     * @auther apecode
     * @date 2022/7/5 14:35
     */
    @Override
    public PageResult<ArticleBackDto> listArticleBack(ConditionVo condition) {
        condition.setCategoryId(StringUtils.isNotBlank(condition.getCategoryId()) ? String.valueOf(SecurityUtils.decrypt(condition.getCategoryId())) : null);
        condition.setTagId(StringUtils.isNotBlank(condition.getTagId()) ? String.valueOf(SecurityUtils.decrypt(condition.getTagId())) : null);
        // 查询后台文章总量
        Integer count = articleMapper.articleBackCount(condition);
        // 查询后台文章
        List<ArticleBackDto> articleBackDtoList = articleMapper.listArticleBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        articleBackDtoList.stream().peek(article -> {
            // 封装点赞量、评论量和浏览量
            Double viewCount = redisService.zScore(ARTICLE_VIEWS_COUNT, Integer.valueOf(article.getId()));
            if (Objects.nonNull(viewCount)) {
                article.setViewsCount(viewCount.intValue());
            }
            Double likeCount = redisService.zScore(ARTICLE_LIKE_COUNT, Integer.valueOf(article.getId()));
            if (Objects.nonNull(likeCount)) {
                article.setLikeCount(likeCount.intValue());
            }
            article.setId(SecurityUtils.encrypt(article.getId()));
        }).collect(Collectors.toList());
        return new PageResult<>(articleBackDtoList, count);
    }

    /**
     * @param keywords
     * @return {@link SearchDto}
     * @description: 搜索
     * @auther apecode
     * @date 2022/7/5 17:15
     */
    @Override
    public SearchDto search(String keywords) {
        // 搜索文章
        List<ArticleSearchDto> articleSearch = listArticleSearch(keywords);
        // 搜索分类
        List<CategorySearchDto> categorySearch = listCategorySearch(keywords);
        // 搜索标签
        List<TagSearchDto> tagSearch = listTagSearch(keywords);
        return SearchDto.builder()
                .count(articleSearch.size() + categorySearch.size() + tagSearch.size())
                .articleSearch(articleSearch)
                .categorySearch(categorySearch)
                .tagSearch(tagSearch)
                .build();
    }

    /**
     * @param articleId
     * @description: 点赞文章
     * @auther apecode
     * @date 2022/6/28 23:25
     */
    @Override
    public void saveArticleLike(String articleId) {
        Integer id = SecurityUtils.decrypt(articleId);
        if (Objects.isNull(id)) throw new BizException("文章路径有误");
        String articleLikeKey = ARTICLE_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        // 判断是否点过赞
        if (redisService.sIsMember(articleLikeKey, id)) {
            // 点过赞则删除文章id
            redisService.sRemove(articleLikeKey, id);
            // 文章点赞量-1
            redisService.zDecr(ARTICLE_LIKE_COUNT, id, 1D);
        } else {
            // 未点赞则增加文章id
            redisService.sAdd(articleLikeKey, id);
            // 文章点赞量+1
            redisService.zIncr(ARTICLE_LIKE_COUNT, id, 1D);
        }
    }

    /**
     * @param conditionVo
     * @return {@link PageResultWithObject<ConditionSearchArticleDto, String>}
     * @description: 根据条件搜索文章
     * @auther apecode
     * @date 16/10/2022 PM4:53
     */
    @Override
    public PageResultWithObject<ConditionSearchArticleDto, String> getArticlesByCondition(ConditionVo conditionVo) {
        if (StringUtils.isNoneBlank(conditionVo.getCategoryId()) && StringUtils.isNoneBlank(conditionVo.getTagId()))
            return new PageResultWithObject<>();
        String name;
        if (StringUtils.isNoneBlank(conditionVo.getCategoryId())) {
            Integer categoryId = SecurityUtils.decrypt(conditionVo.getCategoryId());
            if (Objects.isNull(categoryId)) throw new BizException("分类id有误");
            Category category = categoryMapper.selectById(categoryId);
            if (Objects.isNull(category)) throw new BizException("没有此分类");
            name = category.getName();
            conditionVo.setCategoryId(String.valueOf(categoryId));
        } else if (StringUtils.isNoneBlank(conditionVo.getTagId())) {
            Integer tagId = SecurityUtils.decrypt(conditionVo.getTagId());
            if (Objects.isNull(tagId)) throw new BizException("标签id有误");
            Tag tag = tagMapper.selectById(tagId);
            if (Objects.isNull(tag)) throw new BizException("没有此标签");
            name = tag.getName();
            conditionVo.setTagId(String.valueOf(tagId));
        } else return new PageResultWithObject<>();
        // 数量
        int count = articleMapper.getArticlesByConditionCount(conditionVo);
        if (count == 0) return new PageResultWithObject<>(null, 0, name);
        List<ConditionSearchArticleDto> conditionSearchArticleDtoList = articleMapper.getArticlesByCondition(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVo);
        conditionSearchArticleDtoList.stream().peek((article) -> {
            article.setArticleId(SecurityUtils.encrypt(article.getArticleId()));
            article.setCategoryId(SecurityUtils.encrypt(article.getCategoryId()));
            article.setType(Objects.requireNonNull(getArticleTypeEnum(Integer.valueOf(article.getType()))).getDesc());
            article.getTags().stream().peek((tag) -> {
                tag.setId(SecurityUtils.encrypt(tag.getId()));
            }).collect(Collectors.toList());
        }).collect(Collectors.toList());
        return new PageResultWithObject<>(conditionSearchArticleDtoList, count, name);
    }

    /**
     * @param keywords
     * @return {@link List<TagSearchDto>}
     * @description: 搜索标签
     * @auther apecode
     * @date 2022/7/5 17:35
     */
    private List<TagSearchDto> listTagSearch(String keywords) {
        if (StringUtils.isBlank(keywords)) return new ArrayList<>();
        List<Tag> tags = tagMapper.selectList(Wrappers.<Tag>lambdaQuery().select(Tag::getId, Tag::getName).like(Tag::getName, keywords));
        List<TagSearchDto> tagSearchDtoList = new ArrayList<>();
        tags.stream().peek(tag -> {
            int index = tag.getName().indexOf(keywords);
            if (index != -1) {
                String tagName = tag.getName().replaceAll(keywords, PRE_TAG + keywords + POST_TAG);
                TagSearchDto tagSearchDto = TagSearchDto.builder()
                        .id(SecurityUtils.encrypt(String.valueOf(tag.getId())))
                        .name(tagName)
                        .build();
                tagSearchDtoList.add(tagSearchDto);
            }
        }).collect(Collectors.toList());
        return tagSearchDtoList;
    }

    /**
     * @param keywords
     * @return {@link List<CategorySearchDto>}
     * @description: 搜索分类
     * @auther apecode
     * @date 2022/7/5 17:30
     */
    private List<CategorySearchDto> listCategorySearch(String keywords) {
        if (StringUtils.isBlank(keywords)) return new ArrayList<>();
        List<Category> categories = categoryMapper.selectList(Wrappers.<Category>lambdaQuery().select(Category::getId, Category::getName).like(Category::getName, keywords));
        List<CategorySearchDto> categorySearchDtoList = new ArrayList<>();
        categories.stream().peek(category -> {
            String categoryName;
            int index = category.getName().indexOf(keywords);
            if (index != -1) {
                categoryName = category.getName().replaceAll(keywords, PRE_TAG + keywords + POST_TAG);
                CategorySearchDto categorySearchDto = CategorySearchDto.builder()
                        .id(SecurityUtils.encrypt(String.valueOf(category.getId())))
                        .name(categoryName)
                        .build();
                categorySearchDtoList.add(categorySearchDto);
            }
        }).collect(Collectors.toList());
        return categorySearchDtoList;
    }

    /**
     * @param keywords
     * @return {@link List<ArticleSearchDto>}
     * @description: 搜索文章
     * @auther apecode
     * @date 2022/7/5 17:16
     */
    private List<ArticleSearchDto> listArticleSearch(String keywords) {
        if (StringUtils.isBlank(keywords)) return new ArrayList<>();
        List<Article> articles = articleMapper.selectList(Wrappers.<Article>lambdaQuery()
                .eq(Article::getIsDelete, 0)
                .eq(Article::getStatus, 1)
                .and(i -> i.like(Article::getArticleTitle, keywords)
                        .or()
                        .like(Article::getArticleContent, keywords)));
        return articles.stream().map(article -> {
            String articleContent;
            // 关键字第一次出现的位置
            int index = article.getArticleContent().indexOf(keywords);
            if (index != -1) {
                int preIndex = index > 25 ? index - 25 : 0;
                String preText = article.getArticleContent().substring(preIndex, index);
                // 获取关键字后面的文字
                int last = index + keywords.length();
                int postLength = article.getArticleContent().length() - last;
                int postIndex = postLength > 175 ? last + 175 : last + postLength;
                String postText = article.getArticleContent().substring(index, postIndex);
                // 高亮内容
                articleContent = (preText + postText).replaceAll(keywords, PRE_TAG + keywords + POST_TAG);
            } else articleContent = article.getArticleContent();
            // 文章标题高亮
            String articleTitle = article.getArticleTitle().replaceAll(keywords, PRE_TAG + keywords + POST_TAG);
            return ArticleSearchDto.builder()
                    .id(SecurityUtils.encrypt(String.valueOf(article.getId())))
                    .articleTitle(articleTitle)
                    .articleContent(articleContent)
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * @param articleId
     * @description: 更新文章浏览量
     * @auther apecode
     * @date 2022/7/5 1:10
     */
    private void updateArticleViewsCount(Integer articleId) {
        redisService.zIncr(ARTICLE_VIEWS_COUNT, articleId, 1D);
    }

    /**
     * @param article
     * @param articleId
     * @description: 保存标签
     * @auther apecode
     * @date 2022/6/27 17:09
     */
    private void saveArticleTags(ArticleVo article, Integer articleId) {
        // 编辑文章则删除该文章所有标签
        if (StringUtils.isNotBlank(article.getId())) {
            Integer id = SecurityUtils.decrypt(article.getId());
            if (Objects.isNull(id)) throw new BizException("文章路径有误");
            articleTagMapper.delete(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getArticleId, id));
        }
        // 添加文章标签
        List<String> tagNameList = article.getTagNameList();
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询已存在的标签
            List<Tag> existTagList = tagService.list(Wrappers.<Tag>lambdaQuery().in(Tag::getName, tagNameList));
            List<String> existTagNameList = existTagList.stream().map(Tag::getName).collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream().map(Tag::getId).collect(Collectors.toList());
            // 对比新增不存在的标签
            tagNameList.removeAll(existTagNameList);
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                List<Tag> tagList = tagNameList.stream().map(name -> Tag.builder().name(name).updateTime(CommonUtils.getLocalDateTime()).build()).collect(Collectors.toList());
                tagService.saveBatch(tagList);
                List<Integer> tagIdList = tagList.stream().map(Tag::getId).collect(Collectors.toList());
                existTagIdList.addAll(tagIdList);
            }
            // 提取标签id绑定文章
            List<ArticleTag> articleTagList = existTagIdList.stream().map(id -> ArticleTag.builder().articleId(articleId).tagId(id).updateTime(CommonUtils.getLocalDateTime()).build()).collect(Collectors.toList());
            articleTagService.saveBatch(articleTagList);
        }
    }

    /**
     * @param article
     * @return {@link Category}
     * @description: 保存文章分类
     * @auther apecode
     * @date 2022/6/28 17:20
     */
    private Category saveArticleCategory(ArticleVo article) {
        Category category = categoryMapper.selectOne(Wrappers.<Category>lambdaQuery().eq(Category::getName, article.getCategoryName()));
        if (Objects.isNull(category) && !article.getStatus().equals(DRAFT.getStatus())) {
            category = Category.builder()
                    .name(article.getCategoryName())
                    .updateTime(CommonUtils.getLocalDateTime())
                    .build();
            categoryMapper.insert(category);
        }
        return category;
    }

    /**
     * @param articlePath
     * @return {@link Boolean}
     * @description: 通过id判断文章是否存在
     * @auther apecode
     * @date 2022/6/28 0:36
     */
    private Article getArticle(String articlePath) {
        Integer id = SecurityUtils.decrypt(articlePath);
        if (Objects.isNull(id)) throw new BizException("文章路径有误");
        return articleMapper.selectOne(Wrappers.<Article>lambdaQuery().eq(Article::getId, id));
    }
}
