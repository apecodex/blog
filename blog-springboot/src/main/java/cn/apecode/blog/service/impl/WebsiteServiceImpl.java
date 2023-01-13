package cn.apecode.blog.service.impl;

import cn.apecode.blog.dto.*;
import cn.apecode.blog.entity.Article;
import cn.apecode.blog.entity.Tag;
import cn.apecode.blog.entity.Website;
import cn.apecode.blog.mapper.*;
import cn.apecode.blog.service.RedisService;
import cn.apecode.blog.service.WebsiteService;
import cn.apecode.blog.strategy.context.UploadFileStrategyContext;
import cn.apecode.blog.utils.*;
import cn.apecode.blog.vo.WebsiteConfigVo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static cn.apecode.blog.constant.RedisPrefixConst.BLOG_VIEWS_COUNT;
import static cn.apecode.blog.constant.RedisPrefixConst.UNIQUE_VISITOR;
import static cn.apecode.blog.constant.RedisPrefixConst.VISITOR_AREA;
import static cn.apecode.blog.constant.RedisPrefixConst.ARTICLE_VIEWS_COUNT;
import static cn.apecode.blog.constant.RedisPrefixConst.ARTICLE_LIKE_COUNT;
import static cn.apecode.blog.constant.RedisPrefixConst.ABOUT_ME;
import static cn.apecode.blog.constant.RedisPrefixConst.WEBSITE_CONFIG;
import static cn.apecode.blog.enums.FilePathEnum.CONFIG;

/**
 * <p>
 * 博客设置 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Service
public class WebsiteServiceImpl extends ServiceImpl<WebsiteMapper, Website> implements WebsiteService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private TalkMapper talkMapper;
    @Autowired
    private DailyVisitMapper dailyVisitMapper;
    @Autowired
    private WebsiteMapper websiteMapper;
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UploadFileStrategyContext uploadFileStrategyContext;

    /**
     * @return {@link BlogBackInfoDto}
     * @description: 获取后台信息
     * @auther apecode
     * @date 2022/7/13 23:30
     */
    @Override
    public BlogBackInfoDto getBlogBackInfo() {
        // 文章数量
        Long articleCount = articleMapper.selectCount(Wrappers.<Article>lambdaQuery().eq(Article::getIsDelete, 0));
        // 访问量
        Integer viewsCount = (Integer) redisService.get(BLOG_VIEWS_COUNT);
        // 独立用户访问量
        Long onlyViewCount = redisService.sSize(UNIQUE_VISITOR);
        // 用户量
        Long userCount = userInfoMapper.selectCount(null);
        // 留言量
        Long messageCount = messageMapper.selectCount(null);
        // 分类量
        Long categoryCount = categoryMapper.selectCount(null);
        // 标签量
        Long tagCount = tagMapper.selectCount(null);
        // 说说量
        Long talkCount = talkMapper.selectCount(null);
        // 未阅通知数量
        Integer noReadNoticeCount = noticeMapper.noticeCount(false, true, null);
        // 用户访问区域统计
        Map<Object, Double> visitAreaMap = redisService.zAllScore(VISITOR_AREA);
        List<UserVisitAreaStatisticsDto> userVisitAreaStatisticsList = new ArrayList<>();
        visitAreaMap.forEach((key, value) -> {
            UserVisitAreaStatisticsDto userVisitAreaStatisticsDto = UserVisitAreaStatisticsDto.builder()
                    .area((String) key)
                    .count(value.intValue())
                    .build();
            userVisitAreaStatisticsList.add(userVisitAreaStatisticsDto);
        });
        // 文章统计列表
        List<ArticleStatisticsDto> articleStatisticsList = articleMapper.listArticleStatistics();
        // 用户7天访问量统计列表
        List<VisitStatisticsDto> visitStatisticsList = dailyVisitMapper.listAFewDaysVisit(7);
        // 文章浏览量排行
        Map<Object, Double> articleViewCountMap = redisService.zReverseRangeWithScore(ARTICLE_VIEWS_COUNT, 0, -1);
        // 文章点赞排行
        Map<Object, Double> articleLikeCountMap = redisService.zReverseRangeWithScore(ARTICLE_LIKE_COUNT, 0, -1);
        // 分类列表
        List<CategoryArticleCountDto> listCategoryHome = categoryMapper.listCategoryHome();
        // 标签列表
        List<Tag> tagList = tagMapper.selectList(Wrappers.<Tag>lambdaQuery().select(Tag::getName));
        List<String> tagsName = tagList.stream().map(Tag::getName).collect(Collectors.toList());
        // 系统硬盘信息
        List<DiskInfoDto> diskInfo = SystemInfoUtils.getDiskInfo();
        // 系统内存信息
        MemoryInfoDto memory = SystemInfoUtils.getMemory();
        BlogBackInfoDto blogBackInfoDto = BlogBackInfoDto.builder()
                .articleCount(articleCount.intValue())
                .viewsCount(viewsCount)
                .onlyViewCount(onlyViewCount.intValue())
                .userCount(userCount.intValue())
                .messageCount(messageCount.intValue())
                .categoryCount(categoryCount.intValue())
                .tagCount(tagCount.intValue())
                .talkCount(talkCount.intValue())
                .noReadNoticeCount(noReadNoticeCount)
                .userVisitAreaStatisticsList(userVisitAreaStatisticsList)
                .articleStatisticsList(articleStatisticsList)
                .visitStatisticsList(visitStatisticsList)
                .categoryList(listCategoryHome)
                .tagsName(tagsName)
                .systemMemory(memory)
                .systemDiskInfo(diskInfo)
                .build();
        if (CollectionUtils.isNotEmpty(articleViewCountMap)) {
            List<ArticleRankDto> articleRankList = listArticleViewCountRank(articleViewCountMap);
            blogBackInfoDto.setArticleRankList(articleRankList);
        }
        if (CollectionUtils.isNotEmpty(articleLikeCountMap)) {
            List<ArticleLikeRankDto> articleLikeRankList = listArticleLikeCountRank(articleLikeCountMap);
            blogBackInfoDto.setArticleLikeRankList(articleLikeRankList);
        }
        return blogBackInfoDto;
    }

    /**
     * @param aboutMe
     * @description: 修改关于我信息
     * @auther apecode
     * @date 2022/7/14 15:01
     */
    @Override
    public void updateAboutMe(String aboutMe) {
        redisService.set(ABOUT_ME, aboutMe);
    }

    /**
     * @return {@link String}
     * @description: 获取关于我
     * @auther apecode
     * @date 2022/7/15 14:16
     */
    @Override
    public String getAboutMe() {
        String aboutMeObj = (String) redisService.get(ABOUT_ME);
        if (Objects.isNull(aboutMeObj)) return null;
        return aboutMeObj;
    }

    /**
     * @param websiteConfig
     * @description: 更新网站配置
     * @auther apecode
     * @date 2022/7/14 15:27
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateWebsiteConfigure(WebsiteConfigVo websiteConfig) {
        Website website = Website.builder()
                .id(1)
                .config(JSON.toJSONString(websiteConfig))
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        this.saveOrUpdate(website);
        // 删除缓存
        redisService.del(WEBSITE_CONFIG);
    }

    /**
     * @return {@link WebsiteConfigVo}
     * @description: 获取网站配置
     * @auther apecode
     * @date 2022/7/14 16:58
     */
    @Override
    public WebsiteConfigVo getWebsiteConfigure() {
        WebsiteConfigVo websiteConfigVo;
        Object websiteConfigObj = redisService.get(WEBSITE_CONFIG);
        if (Objects.nonNull(websiteConfigObj)) {
            websiteConfigVo = JSON.parseObject(websiteConfigObj.toString(), WebsiteConfigVo.class);
        } else {
            String config = websiteMapper.selectById(1).getConfig();
            websiteConfigVo = JSON.parseObject(config, WebsiteConfigVo.class);
            redisService.set(WEBSITE_CONFIG, config);
        }
        return websiteConfigVo;
    }

    /**
     * @param file
     * @return {@link UploadFileInfoDto}
     * @description: 上传博客配置图片
     * @auther apecode
     * @date 2022/7/14 17:05
     */
    @Override
    public UploadFileInfoDto uploadWebSiteConfigPic(MultipartFile file) {
        return uploadFileStrategyContext.executeUploadFileStrategy(file, CONFIG.getPath());
    }

    /**
     * @return {@link BlogHomeInfoDto}
     * @description: 获取博客信息
     * @auther apecode
     * @date 2022/7/14 17:46
     */
    @Override
    public BlogHomeInfoDto getBlogInfo() {
        // 文章数量
        Long articleCount = articleMapper.selectCount(Wrappers.<Article>lambdaQuery().eq(Article::getIsDelete, 0));
        // 访问量
        Integer viewsCount = (Integer) redisService.get(BLOG_VIEWS_COUNT);
        // 独立用户访问量
        Long onlyViewCount = redisService.sSize(UNIQUE_VISITOR);
        // 分类量
        Long categoryCount = categoryMapper.selectCount(null);
        // 标签量
        Long tagCount = tagMapper.selectCount(null);
        WebsiteConfigVo websiteConfigure = this.getWebsiteConfigure();
        BlogHomeInfoDto blogHomeInfoDto = BeanCopyUtils.copyObject(websiteConfigure, BlogHomeInfoDto.class);
        blogHomeInfoDto.setArticleCount(articleCount.intValue());
        blogHomeInfoDto.setViewsCount(viewsCount);
        blogHomeInfoDto.setOnlyViewCount(onlyViewCount.intValue());
        blogHomeInfoDto.setCategoryCount(categoryCount.intValue());
        blogHomeInfoDto.setTagCount(tagCount.intValue());
        return blogHomeInfoDto;
    }

    /**
     * @param articleLikeCountMap
     * @return {@link List<ArticleLikeRankDto>}
     * @description: 文章点赞排行
     * @auther apecode
     * @date 2022/7/14 1:58
     */
    private List<ArticleLikeRankDto> listArticleLikeCountRank(Map<Object, Double> articleLikeCountMap) {
        List<Integer> articleIds = new ArrayList<>();
        articleLikeCountMap.forEach((k, v) -> articleIds.add((Integer) k));
        return articleMapper.selectList(Wrappers.<Article>lambdaQuery()
                .select(Article::getId, Article::getArticleTitle)
                .in(Article::getId, articleIds)).stream().map(article -> ArticleLikeRankDto.builder()
                .articleTitle(article.getArticleTitle())
                .likeCount(articleLikeCountMap.get(article.getId()).intValue())
                .build()).sorted(Comparator.comparing(ArticleLikeRankDto::getLikeCount).reversed()).collect(Collectors.toList());
    }

    /**
     * @param articleViewCountMap
     * @return {@link List<ArticleRankDto>}
     * @description: 查询文章排行
     * @auther apecode
     * @date 2022/7/14 1:52
     */
    private List<ArticleRankDto> listArticleViewCountRank(Map<Object, Double> articleViewCountMap) {
        List<Integer> articleIds = new ArrayList<>();
        articleViewCountMap.forEach((k, v) -> articleIds.add((Integer) k));
        return articleMapper.selectList(Wrappers.<Article>lambdaQuery()
                .select(Article::getId, Article::getArticleTitle)
                .in(Article::getId, articleIds)).stream().map(article -> ArticleRankDto.builder()
                .articleTitle(article.getArticleTitle())
                .viewsCount(articleViewCountMap.get(article.getId()).intValue())
                .build()).sorted(Comparator.comparing(ArticleRankDto::getViewsCount).reversed()).collect(Collectors.toList());
    }
}
