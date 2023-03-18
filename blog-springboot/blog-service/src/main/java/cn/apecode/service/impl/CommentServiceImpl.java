package cn.apecode.service.impl;

import cn.apecode.service.RedisService;
import cn.apecode.utils.IpUtils;
import cn.apecode.utils.PageUtils;
import cn.apecode.dto.*;
import cn.apecode.entity.*;
import cn.apecode.common.enums.CommentTypeEnum;
import cn.apecode.common.exception.BizException;
import cn.apecode.mapper.*;
import cn.apecode.service.CommentService;
import cn.apecode.service.WebsiteService;
import cn.apecode.common.utils.*;
import cn.apecode.utils.UserUtils;
import cn.apecode.vo.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static cn.apecode.common.constant.CommonConst.FALSE;
import static cn.apecode.common.constant.RabbitMQPrefixConst.EMAIL_EXCHANGE_NAME;
import static cn.apecode.common.constant.RabbitMQPrefixConst.EMAIL_ROUTING_KEY_NAME;
import static cn.apecode.common.constant.RedisPrefixConst.COMMENT_LIKE_COUNT;
import static cn.apecode.common.constant.RedisPrefixConst.COMMENT_USER_LIKE;
import static cn.apecode.common.constant.CommonConst.COUNTER;
import static cn.apecode.common.constant.CommonConst.LOCAL;
import static cn.apecode.common.constant.CommonConst.UNKNOWN;

/**
 * <p>
 * 文章评论 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-27
 */
@RequiredArgsConstructor
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final CommentMapper commentMapper;
    private final UserInfoMapper userInfoMapper;
    private final WebsiteService websiteService;
    private final ArticleMapper articleMapper;
    private final TalkMapper talkMapper;
    private final NoticeMapper noticeMapper;
    private final RedisService redisService;
    private final RabbitTemplate rabbitTemplate;
    private final HttpServletRequest request;

    /**
     * @param commentVo
     * @description: 添加评论
     * @auther apecode
     * @date 2022/7/7 19:51
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveComment(CommentVo commentVo) {
        // 判断是否需要审核
        WebsiteConfigVo websiteConfigure = websiteService.getWebsiteConfigure();
        Boolean isReview = websiteConfigure.getIsCommentReview();
        Integer topicId = SecurityUtils.decrypt(commentVo.getTopicId());
        Integer parentId = null;
        Integer replyUserId = null;
        Integer replyCommentId = null;
        if (Objects.isNull(topicId)) throw new BizException("主题id有误");
        // 判断父评论是否存在
        if (StringUtils.isNotBlank(commentVo.getParentId())) {
            parentId = SecurityUtils.decrypt(commentVo.getParentId());
            if (Objects.isNull(parentId)) throw new BizException("评论id有误");
            boolean exists = commentMapper.exists(Wrappers.<Comment>lambdaQuery().eq(Comment::getId, parentId));
            if (!exists) throw new BizException("评论不存在");
        }
        // 查询被回复的用户
        if (StringUtils.isNoneBlank(commentVo.getReplyCommentId())) {
            replyCommentId = SecurityUtils.decrypt(commentVo.getReplyCommentId());
            if (Objects.isNull(replyCommentId)) throw new BizException("评论id有误");
            Comment replyUserComment = commentMapper.selectOne(Wrappers.<Comment>lambdaQuery().eq(Comment::getId, replyCommentId));
            if (Objects.isNull(replyUserComment)) throw new BizException("评论不存在");
            replyUserId = replyUserComment.getUserId();
        }
        String ipAddress = IpUtils.getIpAddress(request);
        IpSourceDto ipSourceFromAmap = IpUtils.getIpSourceFromAmap(ipAddress);
        String source;
        String location;
        String geoIp;
        if (Objects.nonNull(ipSourceFromAmap)) {
            source = IpUtils.cutProvince(ipSourceFromAmap);
            // ip归属地
            if (StringUtils.isNotBlank(ipSourceFromAmap.getProvince()) && ipSourceFromAmap.getCountry().equals(COUNTER)) {
                geoIp = ipSourceFromAmap.getProvince();
            } else if (!ipSourceFromAmap.getCountry().equals(LOCAL)) {
                geoIp = ipSourceFromAmap.getCountry();
            } else geoIp = null;
            location = ipSourceFromAmap.getLocation();
            if (location.split(",")[0].equals("null")) location = null;
        } else {
            source = IpUtils.getIpSource(ipAddress);
            geoIp = IpUtils.cutProvince(source);
            location = null;
        }
        commentVo.setCommentContent(HTMLUtils.filter(commentVo.getCommentContent()));
        Comment comment = Comment.builder()
                .userId(UserUtils.getLoginUser().getUserInfoId())
                .topicId(topicId)
                .commentContent(commentVo.getCommentContent())
                .parentId(parentId)
                .type(commentVo.getType())
                .replyUserId(replyUserId)
                .replyCommentId(replyCommentId)
                .rectangle(location)
                .ipAddress(ipAddress)
                .ipSource(source)
                .geoIp(geoIp)
                .browser(UserUtils.getLoginUser().getBrowser())
                .os(UserUtils.getLoginUser().getOs())
                .isReview(isReview)
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        commentMapper.insert(comment);
        // 当前登录用户的id
        UserDetailsDto nowUser = UserUtils.getLoginUser();
        // 判断是否开启邮件通知，通知用户
        CompletableFuture.runAsync(() -> emailNoticeUser(comment, websiteConfigure, nowUser));
        CompletableFuture.runAsync(() -> emailNoticeAuthor(comment, websiteConfigure, nowUser));
    }

    /**
     * @param comment
     * @param websiteConfigure
     * @description: 通知作者有人评论
     * @auther apecode
     * @date 2022/7/14 23:55
     */
    private void emailNoticeAuthor(Comment comment, WebsiteConfigVo websiteConfigure, UserDetailsDto nowUser) {
        Integer userId = null;
        String text = "";
        String noticeText = "";
        String url = websiteConfigure.getUrl() + Objects.requireNonNull(CommentTypeEnum.getCommentEnum(comment.getType())).getPath() + SecurityUtils.encrypt(String.valueOf(comment.getTopicId()));
        switch (Objects.requireNonNull(CommentTypeEnum.getCommentEnum(comment.getType()))) {
            case ARTICLE:
                Article article = articleMapper.selectOne(Wrappers.<Article>lambdaQuery().eq(Article::getId, comment.getTopicId()));
                userId = article.getUserId();
                text = nowUser.getNickname() + "评论您的文章《" + article.getArticleTitle() + "》，请<a href=" + url + ">点我</a>前往页面查看";
                noticeText = "您的文章《" + article.getArticleTitle() + "》收到一条评论";
                break;
            case TALK:
                Talk talk = talkMapper.selectOne(Wrappers.<Talk>lambdaQuery().eq(Talk::getId, comment.getTopicId()));
                userId = talk.getUserId();
                text = nowUser.getNickname() + "评论了您在" + talk.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "发布的说说，请<a href=" + url + ">点我</a>前往页面查看";
                noticeText = "您在" + talk.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "发布的说说收到一条评论";
                break;
        }
        // 如果自己评论自己的就不必发送通知
        if (Objects.nonNull(userId) && !userId.equals(nowUser.getUserInfoId())) {
            UserInfo userInfo = userInfoMapper.selectById(userId);
            if (Objects.nonNull(userInfo)) {
                if (websiteConfigure.getIsEmailNotice() && userInfo.getIsEmailNotice() && StringUtils.isNotBlank(userInfo.getEmail())) {
                    EmailDto emailDto = EmailDto.builder()
                            .email(userInfo.getEmail())
                            .subject("来自" + websiteConfigure.getWebsiteName() + "网站的评论提醒")
                            .text(text)
                            .isHtml(true)
                            .topicId(comment.getId())
                            .type(2)
                            .build();
                    String msgId = RandomUtils.getUUID(15);
                    MessageProperties messageProperties = new MessageProperties();
                    messageProperties.setCorrelationId(msgId);
                    rabbitTemplate.convertAndSend(EMAIL_EXCHANGE_NAME, EMAIL_ROUTING_KEY_NAME, new Message(JSON.toJSONBytes(emailDto), messageProperties));
                }
                Notice notice = Notice.builder()
                        .title("评论提醒")
                        .content(noticeText)
                        .status(false)
                        .url(url)
                        .updateTime(CommonUtils.getLocalDateTime())
                        .build();
                noticeMapper.insert(notice);
            }
        }
    }

    /**
     * @param comment
     * @param websiteConfigure
     * @description: 通知用户被回复
     * @auther apecode
     * @date 2022/7/14 23:54
     */
    private void emailNoticeUser(Comment comment, WebsiteConfigVo websiteConfigure, UserDetailsDto nowUser) {
        EmailDto emailDto = null;
        Notice notice = null;
        if (comment.getIsReview()) {
            if (websiteConfigure.getIsEmailNotice() && StringUtils.isNotBlank(websiteConfigure.getReceiveEmail())) {
                emailDto = EmailDto.builder()
                        .email(websiteConfigure.getReceiveEmail())
                        .subject("来自" + websiteConfigure.getWebsiteName() + "网站的评论审核提醒")
                        .text("您收到了一条来自" + nowUser.getNickname() + "的回复，请前往后台管理页面审核")
                        .isHtml(false)
                        .type(3)
                        .build();
            }
            notice = Notice.builder()
                    .title("评论审核提醒")
                    .content("您收到了一条来自" + nowUser.getNickname() + "的回复，请前往后台管理页面审核")
                    .status(false)
                    .updateTime(CommonUtils.getLocalDateTime())
                    .build();
        } else {
            if (Objects.nonNull(comment.getReplyUserId()) && !comment.getReplyUserId().equals(comment.getUserId())) {
                String url = websiteConfigure.getUrl() + Objects.requireNonNull(CommentTypeEnum.getCommentEnum(comment.getType())).getPath() + SecurityUtils.encrypt(String.valueOf(comment.getTopicId()));
                if (websiteConfigure.getIsEmailNotice()) {
                    // 查询回复用户
                    UserInfo userInfo = userInfoMapper.selectById(comment.getReplyUserId());
                    // 用户是否开启邮箱通知
                    if (Objects.nonNull(userInfo) && userInfo.getIsEmailNotice() && StringUtils.isNotBlank(userInfo.getEmail())) {
                        emailDto = EmailDto.builder()
                                .email(userInfo.getEmail())
                                .subject("来自" + websiteConfigure.getWebsiteName() + "网站的评论提醒")
                                .text("您收到一条来自" + nowUser.getNickname() + "的回复，请<a href=" + url + ">点我</a>前往页面查看")
                                .topicId(comment.getId())
                                .isHtml(true)
                                .type(2)
                                .build();
                    }
                }
                notice = Notice.builder()
                        .userId(comment.getReplyUserId())
                        .title("评论提醒")
                        .content("您收到一条来自" + nowUser.getNickname() + "的回复，请前往查看")
                        .url(url)
                        .status(false)
                        .updateTime(CommonUtils.getLocalDateTime())
                        .build();
            }
        }
        if (Objects.nonNull(emailDto)) {
            String msgId = RandomUtils.getUUID(15);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setCorrelationId(msgId);
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE_NAME, EMAIL_ROUTING_KEY_NAME, new Message(JSON.toJSONBytes(emailDto), messageProperties));
        }
        if (Objects.nonNull(notice)) {
            noticeMapper.insert(notice);
        }
    }

    /**
     * @param topicId
     * @param type
     * @return {@link PageResult<CommentFrontDto>}
     * @description: 查询评论
     * @auther apecode
     * @date 2022/7/8 1:10
     */
    @Override
    public PageResult<CommentFrontDto> listComment(String topicId, Integer type) {
        Integer id = SecurityUtils.decrypt(topicId);
        if (Objects.isNull(id)) throw new BizException("主题id有误");
        // 查询评论数量
        Long count = commentMapper.selectCount(Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getTopicId, id)
                .eq(Comment::getType, type)
                .eq(Comment::getIsReview, FALSE)
                .eq(Comment::getIsDelete, FALSE)
                .isNull(Comment::getParentId));
        if (count.intValue() == 0) return new PageResult<>();
        // 根据主题id和评论类型查找所有父评论
        List<CommentFrontDto> parentComments = commentMapper.listParentComment(PageUtils.getLimitCurrent(), PageUtils.getSize(), id, type);
        if (CollectionUtils.isEmpty(parentComments)) return new PageResult<>();
        // 获取父id
        List<String> parentIds = parentComments.stream().map(CommentFrontDto::getId).collect(Collectors.toList());
        // 通过父id查询所有回复评论
        List<ReplyDto> replyDtoList = commentMapper.listReplies(PageUtils.getLimitCurrent(), PageUtils.getSize(), parentIds);
        // 根据评论id查询回复量
        Map<String, Integer> replyCountMap = commentMapper.replyCountByCommentIds(parentIds).stream().peek(reply -> reply.setCommentId(SecurityUtils.encrypt(reply.getCommentId()))).collect(Collectors.toMap(ReplyCountDto::getCommentId, ReplyCountDto::getReplyCount));
        String location = IpUtils.getNowUserRectangle(request);
        // 处理子评论id加密
        replyDtoList.stream().peek(reply -> {
            if (reply.getParentId().equals(reply.getReplyCommentId())) {
                reply.setReplyUser(null);
            }
            // 获取回复评论的点赞量
            Double likeCount = redisService.zScore(COMMENT_LIKE_COUNT, Integer.valueOf(reply.getId()));
            if (Objects.nonNull(likeCount)) {
                reply.setLikeCount(likeCount.intValue());
            }
            // 获取直线距离
            String distance = IpUtils.getDistance(location, reply.getDistance());
            reply.setDistance(distance);
            reply.setId(SecurityUtils.encrypt(reply.getId()));
            reply.setReplyCommentId(SecurityUtils.encrypt(reply.getReplyCommentId()));
            reply.setParentId(SecurityUtils.encrypt(reply.getParentId()));
            reply.setGeoIp(Optional.ofNullable(reply.getGeoIp()).orElse(UNKNOWN));
        }).collect(Collectors.toList());
        // 根据评论id分组回复数据
        Map<String, List<ReplyDto>> replyMap = replyDtoList.stream().collect(Collectors.groupingBy(ReplyDto::getParentId));
        // 封装评论数据
        parentComments.stream().peek(parent -> {
            // 获取回复评论的点赞量
            Double likeCount = redisService.zScore(COMMENT_LIKE_COUNT, Integer.valueOf(parent.getId()));
            if (Objects.nonNull(likeCount)) {
                parent.setLikeCount(likeCount.intValue());
            }
            // 获取直线距离
            String distance = IpUtils.getDistance(location, parent.getDistance());
            parent.setDistance(distance);
            parent.setId(SecurityUtils.encrypt(parent.getId()));
            parent.setReplyList(replyMap.get(parent.getId()));
            parent.setReplyCount(replyCountMap.get(parent.getId()));
            parent.setGeoIp(Optional.ofNullable(parent.getGeoIp()).orElse(UNKNOWN));
        }).collect(Collectors.toList());
        return new PageResult<>(parentComments, count.intValue());
    }

    /**
     * @param commentId
     * @description: 点赞评论
     * @auther apecode
     * @date 2022/7/9 1:00
     */
    @Override
    public void likeComment(String commentId) {
        Integer id = SecurityUtils.decrypt(commentId);
        if (Objects.isNull(id)) throw new BizException("评论id有误");
        // 判断是否点赞
        String commentUserLike = COMMENT_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        if (redisService.sIsMember(commentUserLike, id)) {
            // 点过则删除评论id
            redisService.sRemove(commentUserLike, id);
            // 评论点赞量-1
            redisService.zDecr(COMMENT_LIKE_COUNT, id, 1D);
        } else {
            // 未点赞则新增评论id
            redisService.sAdd(commentUserLike, id);
            // 评论点赞量+1
            redisService.zIncr(COMMENT_LIKE_COUNT, id, 1D);
        }
    }

    /**
     * @param commentId
     * @return {@link List<ReplyDto>}
     * @description: 查询评论下的回复
     * @auther apecode
     * @date 2022/7/9 13:11
     */
    @Override
    public List<ReplyDto> listRepliesByCommentId(String commentId) {
        Integer id = SecurityUtils.decrypt(commentId);
        if (Objects.isNull(id)) throw new BizException("评论id有误");
        // 通过页码查询评论下的回复
        List<ReplyDto> replyDtoList = commentMapper.listRepliesByCommentId(PageUtils.getLimitCurrent(), PageUtils.getSize(), id);
        replyDtoList.stream().peek(reply -> {
            if (reply.getParentId().equals(reply.getReplyCommentId())) {
                reply.setReplyUser(null);
            }
            // 获取回复评论的点赞量
            Double likeCount = redisService.zScore(COMMENT_LIKE_COUNT, Integer.valueOf(reply.getId()));
            if (Objects.nonNull(likeCount)) {
                reply.setLikeCount(likeCount.intValue());
            }
            // 获取直线距离
            String location = IpUtils.getNowUserRectangle(request);
            String distance = IpUtils.getDistance(location, reply.getDistance());
            reply.setDistance(distance);
            reply.setId(SecurityUtils.encrypt(reply.getId()));
            reply.setReplyCommentId(SecurityUtils.encrypt(reply.getReplyCommentId()));
            reply.setParentId(SecurityUtils.encrypt(reply.getParentId()));
            reply.setGeoIp(Optional.ofNullable(reply.getGeoIp()).orElse(UNKNOWN));
        }).collect(Collectors.toList());
        return replyDtoList;
    }

    /**
     * @param review
     * @description: 审核评论
     * @auther apecode
     * @date 2022/7/9 23:09
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCommentReview(ReviewVo review) {
        if (!review.getIdList().isEmpty()) {
            List<Comment> commentList = review.getIdList().stream().map(id -> {
                Integer idDecrypt = SecurityUtils.decrypt(id);
                if (Objects.isNull(idDecrypt)) throw new BizException("评论id有误");
                return Comment.builder()
                        .id(idDecrypt)
                        .isReview(review.getIsReview())
                        .build();
            }).collect(Collectors.toList());
            this.updateBatchById(commentList);
        }
    }

    /**
     * @param commentId
     * @description: 删除评论
     * @auther apecode
     * @date 2022/7/9 18:33
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteComment(String commentId) {
        Integer id = SecurityUtils.decrypt(commentId);
        if (Objects.isNull(id)) throw new BizException("评论id有误");
        // 查找所有子评论
        List<Integer> ids = commentMapper.selectList(Wrappers.<Comment>lambdaQuery().eq(Comment::getParentId, id)).stream().map(Comment::getId).collect(Collectors.toList());
        ids.add(id);
        this.removeByIds(ids);
    }

    /**
     * @param condition
     * @return {@link PageResult< CommentBackDto >}
     * @description: 查询后台评论
     * @auther apecode
     * @date 2022/7/9 21:21
     */
    @Override
    public PageResult<CommentBackDto> listCommentBack(ConditionVo condition) {
        // 查询后台评论量
        Integer count = commentMapper.countComment(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        List<CommentBackDto> commentBackDtoList = commentMapper.listCommentBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        // 父评论id
        List<String> parentIds = commentBackDtoList.stream().filter((comment) -> Objects.isNull(comment.getParentId())).collect(Collectors.toList()).stream().map(CommentBackDto::getId).collect(Collectors.toList());
        // 查询评论数量
        Map<String, Integer> replyCountMap = commentMapper.replyCountByCommentIds(parentIds).stream().filter(reply -> Objects.nonNull(reply.getReplyCount())).collect(Collectors.toMap(ReplyCountDto::getCommentId, ReplyCountDto::getReplyCount));
        commentBackDtoList.stream().peek(comment -> {
            comment.setReplyCount(replyCountMap.get(comment.getId()));
            comment.setParentId(null);
            // 获取回复评论的点赞量
            Double likeCount = redisService.zScore(COMMENT_LIKE_COUNT, Integer.valueOf(comment.getId()));
            if (Objects.nonNull(likeCount)) {
                comment.setLikeCount(likeCount.intValue());
            }
            CommentTypeEnum commentEnum = CommentTypeEnum.getCommentEnum(Integer.parseInt(comment.getType()));
            if (Objects.requireNonNull(commentEnum) == CommentTypeEnum.TALK) {
                comment.setArticleTitle(null);
            }
            comment.setType(commentEnum.getDesc());
            comment.setId(SecurityUtils.encrypt(comment.getId()));
        }).collect(Collectors.toList());
        return new PageResult<>(commentBackDtoList, count);
    }

}
