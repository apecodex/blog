package cn.apecode.service.impl;

import cn.apecode.common.exception.BizException;
import cn.apecode.common.utils.CommonUtils;
import cn.apecode.common.utils.SecurityUtils;
import cn.apecode.dto.*;
import cn.apecode.entity.Article;
import cn.apecode.entity.ArticleTag;
import cn.apecode.entity.Tag;
import cn.apecode.mapper.ArticleMapper;
import cn.apecode.mapper.ArticleTagMapper;
import cn.apecode.mapper.CommentMapper;
import cn.apecode.mapper.TagMapper;
import cn.apecode.service.TagService;
import cn.apecode.utils.PageUtils;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.PageResult;
import cn.apecode.vo.TagVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * 文章标签 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final TagMapper tagMapper;
    private final ArticleTagMapper articleTagMapper;
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;

    /**
     * @param tagsName
     * @return {@link List<TagDto>}
     * @description: 添加标签
     * @auther apecode
     * @date 2022/6/25 17:43
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<TagDto> saveTags(List<String> tagsName) {
        // 临时标签名，不在数据库中的标签单独添加
        List<String> tempName = new ArrayList<>();
        List<TagDto> tagDtoList = new ArrayList<>();
        // 获取所有标签
        List<Tag> tagList = tagMapper.selectList(Wrappers.<Tag>lambdaQuery().select(Tag::getId, Tag::getName));
        tagsName.forEach(name -> {
            for (Tag tag : tagList) {
                if (name.equals(tag.getName())) {
                    TagDto tagDto = TagDto.builder()
                            .id(SecurityUtils.encrypt(String.valueOf(tag.getId())))
                            .name(tag.getName())
                            .build();
                    tagDtoList.add(tagDto);
                    return;
                }
            }
            tempName.add(name);
        });
        List<Tag> tags = tempName.stream().map(name -> Tag.builder().name(name).updateTime(CommonUtils.getLocalDateTime()).build()).collect(Collectors.toList());
        if (this.saveBatch(tags)) {
            List<Tag> list = this.list(Wrappers.<Tag>lambdaQuery().in(Tag::getName, tempName.toArray()));
            List<TagDto> tagDtos = list.stream().map(tag -> TagDto.builder().id(SecurityUtils.encrypt(String.valueOf(tag.getId()))).name(tag.getName()).build()).collect(Collectors.toList());
            tagDtoList.addAll(tagDtos);
        }
        return tagDtoList;
    }

    /**
     * @param condition
     * @return {@link PageResult<TagBackDto>}
     * @description: 查询后台标签列表
     * @auther apecode
     * @date 2022/6/25 19:48
     */
    @Override
    public PageResult<TagBackDto> listTagBack(ConditionVo condition) {
        Page<Tag> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        IPage<TagBackDto> tagBackDtoIPage = tagMapper.listTagBack(page, condition);
        tagBackDtoIPage.getRecords().stream().peek(tag -> tag.setId(SecurityUtils.encrypt(String.valueOf(tag.getId())))).collect(Collectors.toList());
        return new PageResult<>(tagBackDtoIPage.getRecords(), (int) tagBackDtoIPage.getTotal());
    }

    /**
     * @param tagId
     * @description: 删除标签
     * @auther apecode
     * @date 2022/6/25 20:00
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTag(String tagId) {
        Integer id = SecurityUtils.decrypt(tagId);
        if (Objects.isNull(id)) throw new BizException("标签id有误");
        boolean exists = articleTagMapper.exists(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getTagId, id));
        if (exists) throw new BizException("删除失败，此标签有文章使用");
        tagMapper.deleteById(id);
    }

    /**
     * @param condition
     * @return {@link List<TagDto>}
     * @description: 搜索文章标签
     * @auther apecode
     * @date 2022/6/25 23:25
     */
    @Override
    public List<TagDto> listTagFromSearch(ConditionVo condition) {
        List<Tag> tagList = new ArrayList<>();
        if (StringUtils.isNotBlank(condition.getKeywords())) {
            tagList = tagMapper.selectList(Wrappers.<Tag>lambdaQuery()
                    .like(StringUtils.isNoneBlank(condition.getKeywords()), Tag::getName, condition.getKeywords())
                    .orderByDesc(Tag::getId));
        }
        return tagList.stream().map(tag -> TagDto.builder()
                .id(SecurityUtils.encrypt(String.valueOf(tag.getId())))
                .name(tag.getName()).build()).collect(Collectors.toList());
    }

    /**
     * @return {@link PageResult<TagFrontDto>}
     * @description: 查询标签列表
     * @auther apecode
     * @date 2022/6/25 23:56
     */
    @Override
    public TagFrontDto listTagFront() {
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
        List<Tag> tags = tagMapper.selectList(Wrappers.<Tag>lambdaQuery().orderByDesc(Tag::getId));
        List<TagDto> tagDtos = tags.stream().map((tag) -> TagDto.builder()
                .id(SecurityUtils.encrypt(String.valueOf(tag.getId())))
                .name(tag.getName())
                .build()).collect(Collectors.toList());
        TagFrontDto tagFrontDto = TagFrontDto.builder().tags(tagDtos).build();
        // 封装信息
        try {
            tagFrontDto.setNewsArticleList(newsArticleList.get());
            tagFrontDto.setNewsCommentList(newsCommentList.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tagFrontDto;
    }

    /**
     * @param tag
     * @description: 修改标签
     * @auther apecode
     * @date 2022/6/26 0:07
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTag(TagVo tag) {
        if (StringUtils.isNotBlank(tag.getId())) {
            Integer id = SecurityUtils.decrypt(tag.getId());
            if (Objects.isNull(id)) throw new BizException("标签id有误");
            // 判断标签是否存在
            boolean exists = tagMapper.exists(Wrappers.<Tag>lambdaQuery().eq(Tag::getId, id));
            if (!exists) throw new BizException("标签不存在");
            boolean existsTagName = tagMapper.exists(Wrappers.<Tag>lambdaQuery().eq(Tag::getName, tag.getName()));
            if (existsTagName) throw new BizException("标签名已存在");
            Tag t = Tag.builder().id(id).name(tag.getName()).build();
            this.updateById(t);
        }
    }

}
