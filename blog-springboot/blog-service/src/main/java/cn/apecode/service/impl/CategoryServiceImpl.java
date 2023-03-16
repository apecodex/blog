package cn.apecode.service.impl;

import cn.apecode.common.exception.BizException;
import cn.apecode.common.utils.BeanCopyUtils;
import cn.apecode.common.utils.CommonUtils;
import cn.apecode.common.utils.SecurityUtils;
import cn.apecode.dto.*;
import cn.apecode.entity.Article;
import cn.apecode.entity.Category;
import cn.apecode.mapper.ArticleMapper;
import cn.apecode.mapper.CategoryMapper;
import cn.apecode.mapper.CommentMapper;
import cn.apecode.service.CategoryService;
import cn.apecode.utils.PageUtils;
import cn.apecode.vo.CategoryVo;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.PageResult;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * <p>
 * 文章分类 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;

    /**
     * @description: 添加或修改分类
     * @param category
     * @auther apecode
     * @date 2022/6/26 16:31
    */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateCategory(CategoryVo category) {
        Category c = BeanCopyUtils.copyObject(category, Category.class);
        // 判断是否存在
        if (StringUtils.isNotBlank(category.getCategoryId())) {
            Integer categoryId = SecurityUtils.decrypt(category.getCategoryId());
            if (Objects.isNull(categoryId)) throw new BizException("分类id有误");
            boolean exists = categoryMapper.exists(Wrappers.<Category>lambdaQuery().eq(Category::getId, categoryId));
            if (!exists) throw new BizException("失败，该分类不存在");
            c.setId(categoryId);
        }
        boolean existsCategoryName = categoryMapper.exists(Wrappers.<Category>lambdaQuery().eq(Category::getName, category.getName()));
        if (existsCategoryName) throw new BizException("此分类名称已存在");
        c.setUpdateTime(CommonUtils.getLocalDateTime());
        this.saveOrUpdate(c);

    }

    /**
     * @description: 删除分类
     * @param categoryId
     * @auther apecode
     * @date 2022/6/26 16:46
    */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCategory(String categoryId) {
        Integer id = SecurityUtils.decrypt(categoryId);
        if (Objects.isNull(id)) throw new BizException("分类id有误");
        //  是否有文章绑定
        boolean hasArticleBind = articleMapper.exists(Wrappers.<Article>lambdaQuery().eq(Article::getCategoryId, id));
        if (hasArticleBind) throw new BizException("删除失败，该分类有文章绑定");
        categoryMapper.deleteById(id);
    }

    /**
     * @description: 搜索文章分类
     * @param condition
     * @return {@link List<CategoryOptionDto>}
     * @auther apecode
     * @date 2022/6/26 19:35
    */
    @Override
    public List<CategoryOptionDto> listCategoryFromSearch(ConditionVo condition) {
        List<Category> categories = categoryMapper.selectList(Wrappers.<Category>lambdaQuery()
                .like(Objects.nonNull(condition.getKeywords()), Category::getName, condition.getKeywords()));
        return categories.stream().map(category -> CategoryOptionDto.builder()
                    .id(SecurityUtils.encrypt(String.valueOf(category.getId())))
                    .name(category.getName())
                    .build()).collect(Collectors.toList());
    }

    /**
     * @description: 获取后台分类列表
     * @param condition
     * @return {@link PageResult<CategoryBackDto>}
     * @auther apecode
     * @date 2022/7/4 18:54
    */
    @Override
    public PageResult<CategoryBackDto> listCategoryBack(ConditionVo condition) {
        // 查询分类数量
        Long count = categoryMapper.selectCount(Wrappers.<Category>lambdaQuery().like(StringUtils.isNotBlank(condition.getKeywords()), Category::getName, condition.getKeywords()));
        if (count.intValue() == 0) return new PageResult<>(new ArrayList<>(), 0);
        // 分页查询分类列表
        List<CategoryBackDto> categoryDtoList = categoryMapper.listCategoryBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        categoryDtoList.stream().peek(category -> category.setId(SecurityUtils.encrypt(String.valueOf(category.getId())))).collect(Collectors.toList());
        return new PageResult<>(categoryDtoList, count.intValue());
    }

    /**
     * @description: 获取分类列表
     * @return {@link CategoryFrontDto}
     * @auther apecode
     * @date 2022/7/4 18:57
    */
    @Override
    public CategoryFrontDto listCategoryFront() {
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
        List<CategoryArticleCountDto> categoryArticleCountDtos = categoryMapper.listCategoryFront();
        categoryArticleCountDtos.stream().peek(category -> category.setId(SecurityUtils.encrypt(String.valueOf(category.getId())))).collect(Collectors.toList());
        CategoryFrontDto categoryFrontDto = CategoryFrontDto.builder().categories(categoryArticleCountDtos).build();
        // 封装信息
        try {
            categoryFrontDto.setNewsArticleList(newsArticleList.get());
            categoryFrontDto.setNewsCommentList(newsCommentList.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return categoryFrontDto;
    }

}
