package cn.apecode.blog.service.impl;

import cn.apecode.blog.dto.*;
import cn.apecode.blog.entity.*;
import cn.apecode.blog.enums.CommentTypeEnum;
import cn.apecode.blog.exception.BizException;
import cn.apecode.blog.mapper.*;
import cn.apecode.blog.service.CommentService;
import cn.apecode.blog.service.RedisService;
import cn.apecode.blog.service.WebsiteService;
import cn.apecode.blog.utils.SecurityUtils;
import cn.apecode.blog.utils.IpUtils;
import cn.apecode.blog.utils.HTMLUtils;
import cn.apecode.blog.utils.UserUtils;
import cn.apecode.blog.utils.CommonUtils;
import cn.apecode.blog.utils.RandomUtils;
import cn.apecode.blog.utils.PageUtils;
import cn.apecode.blog.vo.CommentVo;
import cn.apecode.blog.vo.WebsiteConfigVo;
import cn.apecode.blog.vo.ReviewVo;
import cn.apecode.blog.vo.PageResult;
import cn.apecode.blog.vo.ConditionVo;
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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static cn.apecode.blog.constant.RedisPrefixConst.COMMENT_USER_LIKE;
import static cn.apecode.blog.constant.RedisPrefixConst.COMMENT_LIKE_COUNT;
import static cn.apecode.blog.constant.CommonConst.FALSE;
import static cn.apecode.blog.constant.RabbitMQPrefixConst.EMAIL_EXCHANGE_NAME;
import static cn.apecode.blog.constant.RabbitMQPrefixConst.EMAIL_ROUTING_KEY_NAME;

/**
 * <p>
 * ???????????? ???????????????
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
     * @description: ????????????
     * @auther apecode
     * @date 2022/7/7 19:51
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveComment(CommentVo commentVo) {
        // ????????????????????????
        WebsiteConfigVo websiteConfigure = websiteService.getWebsiteConfigure();
        Boolean isReview = websiteConfigure.getIsCommentReview();
        Integer topicId = SecurityUtils.decrypt(commentVo.getTopicId());
        Integer parentId = null;
        Integer replyUserId = null;
        Integer replyCommentId = null;
        if (Objects.isNull(topicId)) throw new BizException("??????id??????");
        // ???????????????????????????
        if (StringUtils.isNotBlank(commentVo.getParentId())) {
            parentId = SecurityUtils.decrypt(commentVo.getParentId());
            if (Objects.isNull(parentId)) throw new BizException("??????id??????");
            boolean exists = commentMapper.exists(Wrappers.<Comment>lambdaQuery().eq(Comment::getId, parentId));
            if (!exists) throw new BizException("???????????????");
        }
        // ????????????????????????
        if (StringUtils.isNoneBlank(commentVo.getReplyCommentId())) {
            replyCommentId = SecurityUtils.decrypt(commentVo.getReplyCommentId());
            if (Objects.isNull(replyCommentId)) throw new BizException("??????id??????");
            Comment replyUserComment = commentMapper.selectOne(Wrappers.<Comment>lambdaQuery().eq(Comment::getId, replyCommentId));
            if (Objects.isNull(replyUserComment)) throw new BizException("???????????????");
            replyUserId = replyUserComment.getUserId();
        }

        String ipAddress = IpUtils.getIpAddress(request);
        IpSourceDto ipSourceFromAmap = IpUtils.getIpSourceFromAmap(ipAddress);
        String source;
        String location;
        if (Objects.nonNull(ipSourceFromAmap)) {
            source = IpUtils.cutProvince(ipSourceFromAmap);
            location = ipSourceFromAmap.getLocation();
            if (location.split(",")[0].equals("null")) {
                location = null;
            }
        } else {
            source = IpUtils.getIpSource(ipAddress);
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
                .browser(UserUtils.getLoginUser().getBrowser())
                .os(UserUtils.getLoginUser().getOs())
                .isReview(isReview)
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        commentMapper.insert(comment);
        // ?????????????????????id
        UserDetailsDto nowUser = UserUtils.getLoginUser();
        // ?????????????????????????????????????????????
        CompletableFuture.runAsync(() -> emailNoticeUser(comment, websiteConfigure, nowUser));
        CompletableFuture.runAsync(() -> emailNoticeAuthor(comment, websiteConfigure, nowUser));
    }

    /**
     * @param comment
     * @param websiteConfigure
     * @description: ????????????????????????
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
                text = nowUser.getNickname() + "?????????????????????" + article.getArticleTitle() + "?????????<a href=" + url + ">??????</a>??????????????????";
                noticeText = "???????????????" + article.getArticleTitle() + "?????????????????????";
                break;
            case TALK:
                Talk talk = talkMapper.selectOne(Wrappers.<Talk>lambdaQuery().eq(Talk::getId, comment.getTopicId()));
                userId = talk.getUserId();
                text = nowUser.getNickname() + "???????????????" + talk.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "?????????????????????<a href=" + url + ">??????</a>??????????????????";
                noticeText = "??????" + talk.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "?????????????????????????????????";
                break;
        }
        // ????????????????????????????????????????????????
        if (Objects.nonNull(userId) && !userId.equals(nowUser.getUserInfoId())) {
            UserInfo userInfo = userInfoMapper.selectById(userId);
            if (Objects.nonNull(userInfo)) {
                if (websiteConfigure.getIsEmailNotice() && userInfo.getIsEmailNotice() && StringUtils.isNotBlank(userInfo.getEmail())) {
                    EmailDto emailDto = EmailDto.builder()
                            .email(userInfo.getEmail())
                            .subject("??????" + websiteConfigure.getWebsiteName() + "?????????????????????")
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
                        .title("????????????")
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
     * @description: ?????????????????????
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
                        .subject("??????" + websiteConfigure.getWebsiteName() + "???????????????????????????")
                        .text("????????????????????????" + nowUser.getNickname() + "?????????????????????????????????????????????")
                        .isHtml(false)
                        .type(3)
                        .build();
            }
            notice = Notice.builder()
                    .title("??????????????????")
                    .content("????????????????????????" + nowUser.getNickname() + "?????????????????????????????????????????????")
                    .status(false)
                    .updateTime(CommonUtils.getLocalDateTime())
                    .build();
        } else {
            if (Objects.nonNull(comment.getReplyUserId()) && !comment.getReplyUserId().equals(comment.getUserId())) {
                String url = websiteConfigure.getUrl() + Objects.requireNonNull(CommentTypeEnum.getCommentEnum(comment.getType())).getPath() + SecurityUtils.encrypt(String.valueOf(comment.getTopicId()));
                if (websiteConfigure.getIsEmailNotice()) {
                    // ??????????????????
                    UserInfo userInfo = userInfoMapper.selectById(comment.getReplyUserId());
                    // ??????????????????????????????
                    if (Objects.nonNull(userInfo) && userInfo.getIsEmailNotice() && StringUtils.isNotBlank(userInfo.getEmail())) {
                        emailDto = EmailDto.builder()
                                .email(userInfo.getEmail())
                                .subject("??????" + websiteConfigure.getWebsiteName() + "?????????????????????")
                                .text("?????????????????????" + nowUser.getNickname() + "???????????????<a href=" + url + ">??????</a>??????????????????")
                                .topicId(comment.getId())
                                .isHtml(true)
                                .type(2)
                                .build();
                    }
                }
                notice = Notice.builder()
                        .userId(comment.getReplyUserId())
                        .title("????????????")
                        .content("?????????????????????" + nowUser.getNickname() + "???????????????????????????")
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
     * @description: ????????????
     * @auther apecode
     * @date 2022/7/8 1:10
     */
    @Override
    public PageResult<CommentFrontDto> listComment(String topicId, Integer type) {
        Integer id = SecurityUtils.decrypt(topicId);
        if (Objects.isNull(id)) throw new BizException("??????id??????");
        // ??????????????????
        Long count = commentMapper.selectCount(Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getTopicId, id)
                .eq(Comment::getType, type)
                .eq(Comment::getIsReview, FALSE)
                .eq(Comment::getIsDelete, FALSE)
                .isNull(Comment::getParentId));
        if (count.intValue() == 0) return new PageResult<>();
        // ????????????id????????????????????????????????????
        List<CommentFrontDto> parentComments = commentMapper.listParentComment(PageUtils.getLimitCurrent(), PageUtils.getSize(), id, type);
        if (CollectionUtils.isEmpty(parentComments)) return new PageResult<>();
        // ?????????id
        List<String> parentIds = parentComments.stream().map(CommentFrontDto::getId).collect(Collectors.toList());
        // ?????????id????????????????????????
        List<ReplyDto> replyDtoList = commentMapper.listReplies(PageUtils.getLimitCurrent(), PageUtils.getSize(), parentIds);
        // ????????????id???????????????
        Map<String, Integer> replyCountMap = commentMapper.replyCountByCommentIds(parentIds).stream().peek(reply -> reply.setCommentId(SecurityUtils.encrypt(reply.getCommentId()))).collect(Collectors.toMap(ReplyCountDto::getCommentId, ReplyCountDto::getReplyCount));
        String location = IpUtils.getNowUserRectangle(request);
        // ???????????????id??????
        replyDtoList.stream().peek(reply -> {
            if (reply.getParentId().equals(reply.getReplyCommentId())) {
                reply.setReplyUser(null);
            }
            // ??????????????????????????????
            Double likeCount = redisService.zScore(COMMENT_LIKE_COUNT, Integer.valueOf(reply.getId()));
            if (Objects.nonNull(likeCount)) {
                reply.setLikeCount(likeCount.intValue());
            }
            // ??????????????????
            String distance = IpUtils.getDistance(location, reply.getDistance());
            reply.setDistance(distance);
            reply.setId(SecurityUtils.encrypt(reply.getId()));
            reply.setReplyCommentId(SecurityUtils.encrypt(reply.getReplyCommentId()));
            reply.setParentId(SecurityUtils.encrypt(reply.getParentId()));
        }).collect(Collectors.toList());
        // ????????????id??????????????????
        Map<String, List<ReplyDto>> replyMap = replyDtoList.stream().collect(Collectors.groupingBy(ReplyDto::getParentId));
        // ??????????????????
        parentComments.stream().peek(parent -> {
            // ??????????????????????????????
            Double likeCount = redisService.zScore(COMMENT_LIKE_COUNT, Integer.valueOf(parent.getId()));
            if (Objects.nonNull(likeCount)) {
                parent.setLikeCount(likeCount.intValue());
            }
            // ??????????????????
            String distance = IpUtils.getDistance(location, parent.getDistance());
            parent.setDistance(distance);
            parent.setId(SecurityUtils.encrypt(parent.getId()));
            parent.setReplyList(replyMap.get(parent.getId()));
            parent.setReplyCount(replyCountMap.get(parent.getId()));
        }).collect(Collectors.toList());
        return new PageResult<>(parentComments, count.intValue());
    }

    /**
     * @param commentId
     * @description: ????????????
     * @auther apecode
     * @date 2022/7/9 1:00
     */
    @Override
    public void likeComment(String commentId) {
        Integer id = SecurityUtils.decrypt(commentId);
        if (Objects.isNull(id)) throw new BizException("??????id??????");
        // ??????????????????
        String commentUserLike = COMMENT_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        if (redisService.sIsMember(commentUserLike, id)) {
            // ?????????????????????id
            redisService.sRemove(commentUserLike, id);
            // ???????????????-1
            redisService.zDecr(COMMENT_LIKE_COUNT, id, 1D);
        } else {
            // ????????????????????????id
            redisService.sAdd(commentUserLike, id);
            // ???????????????+1
            redisService.zIncr(COMMENT_LIKE_COUNT, id, 1D);
        }
    }

    /**
     * @param commentId
     * @return {@link List<ReplyDto>}
     * @description: ????????????????????????
     * @auther apecode
     * @date 2022/7/9 13:11
     */
    @Override
    public List<ReplyDto> listRepliesByCommentId(String commentId) {
        Integer id = SecurityUtils.decrypt(commentId);
        if (Objects.isNull(id)) throw new BizException("??????id??????");
        // ????????????????????????????????????
        List<ReplyDto> replyDtoList = commentMapper.listRepliesByCommentId(PageUtils.getLimitCurrent(), PageUtils.getSize(), id);
        replyDtoList.stream().peek(reply -> {
            if (reply.getParentId().equals(reply.getReplyCommentId())) {
                reply.setReplyUser(null);
            }
            // ??????????????????????????????
            Double likeCount = redisService.zScore(COMMENT_LIKE_COUNT, Integer.valueOf(reply.getId()));
            if (Objects.nonNull(likeCount)) {
                reply.setLikeCount(likeCount.intValue());
            }
            // ??????????????????
            String location = IpUtils.getNowUserRectangle(request);
            String distance = IpUtils.getDistance(location, reply.getDistance());
            reply.setDistance(distance);
            reply.setId(SecurityUtils.encrypt(reply.getId()));
            reply.setReplyCommentId(SecurityUtils.encrypt(reply.getReplyCommentId()));
            reply.setParentId(SecurityUtils.encrypt(reply.getParentId()));
        }).collect(Collectors.toList());
        return replyDtoList;
    }

    /**
     * @param review
     * @description: ????????????
     * @auther apecode
     * @date 2022/7/9 23:09
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCommentReview(ReviewVo review) {
        if (!review.getIdList().isEmpty()) {
            List<Comment> commentList = review.getIdList().stream().map(id -> {
                Integer idDecrypt = SecurityUtils.decrypt(id);
                if (Objects.isNull(idDecrypt)) throw new BizException("??????id??????");
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
     * @description: ????????????
     * @auther apecode
     * @date 2022/7/9 18:33
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteComment(String commentId) {
        Integer id = SecurityUtils.decrypt(commentId);
        if (Objects.isNull(id)) throw new BizException("??????id??????");
        // ?????????????????????
        List<Integer> ids = commentMapper.selectList(Wrappers.<Comment>lambdaQuery().eq(Comment::getParentId, id)).stream().map(Comment::getId).collect(Collectors.toList());
        ids.add(id);
        this.removeByIds(ids);
    }

    /**
     * @param condition
     * @return {@link PageResult< CommentBackDto >}
     * @description: ??????????????????
     * @auther apecode
     * @date 2022/7/9 21:21
     */
    @Override
    public PageResult<CommentBackDto> listCommentBack(ConditionVo condition) {
        // ?????????????????????
        Integer count = commentMapper.countComment(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        List<CommentBackDto> commentBackDtoList = commentMapper.listCommentBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        // ?????????id
        List<String> parentIds = commentBackDtoList.stream().filter((comment) -> Objects.isNull(comment.getParentId())).collect(Collectors.toList()).stream().map(CommentBackDto::getId).collect(Collectors.toList());
        // ??????????????????
        Map<String, Integer> replyCountMap = commentMapper.replyCountByCommentIds(parentIds).stream().filter(reply -> Objects.nonNull(reply.getReplyCount())).collect(Collectors.toMap(ReplyCountDto::getCommentId, ReplyCountDto::getReplyCount));
        commentBackDtoList.stream().peek(comment -> {
            comment.setReplyCount(replyCountMap.get(comment.getId()));
            comment.setParentId(null);
            // ??????????????????????????????
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
