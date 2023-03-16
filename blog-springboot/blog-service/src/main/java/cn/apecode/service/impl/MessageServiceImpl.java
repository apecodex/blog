package cn.apecode.service.impl;

import cn.apecode.common.exception.BizException;
import cn.apecode.common.utils.CommonUtils;
import cn.apecode.common.utils.HTMLUtils;
import cn.apecode.common.utils.RandomUtils;
import cn.apecode.common.utils.SecurityUtils;
import cn.apecode.dto.EmailDto;
import cn.apecode.dto.MessageBackDto;
import cn.apecode.dto.MessageFrontDto;
import cn.apecode.entity.Message;
import cn.apecode.entity.Notice;
import cn.apecode.mapper.MessageMapper;
import cn.apecode.mapper.NoticeMapper;
import cn.apecode.service.MessageService;
import cn.apecode.service.WebsiteService;
import cn.apecode.utils.IpUtils;
import cn.apecode.utils.PageUtils;
import cn.apecode.utils.UserUtils;
import cn.apecode.vo.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static cn.apecode.common.constant.CommonConst.DEFAULT_AVATAR;
import static cn.apecode.common.constant.CommonConst.DEFAULT_TOURIST;
import static cn.apecode.common.constant.RabbitMQPrefixConst.EMAIL_EXCHANGE_NAME;
import static cn.apecode.common.constant.RabbitMQPrefixConst.EMAIL_ROUTING_KEY_NAME;

/**
 * <p>
 * 留言 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@RequiredArgsConstructor
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    private final MessageMapper messageMapper;
    private final WebsiteService websiteService;
    private final NoticeMapper noticeMapper;
    private final RabbitTemplate rabbitTemplate;
    private final HttpServletRequest request;

    /**
     * @param condition
     * @return {@link PageResult<MessageBackDto>}
     * @description: 获取后台留言列表
     * @auther apecode
     * @date 2022/6/24 16:26
     */
    @Override
    public PageResult<MessageBackDto> listMessageBack(ConditionVo condition) {
        Page<Message> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        IPage<MessageBackDto> messageBackDtoIPage = messageMapper.listMessageBack(page, condition);
        messageBackDtoIPage.getRecords().stream().peek(message -> message.setId(SecurityUtils.encrypt(message.getId()))).collect(Collectors.toList());
        messageBackDtoIPage.getRecords().stream().filter(message -> Objects.nonNull(message.getUser())).peek(item -> item.setAvatar(item.getUser().getAvatar())).collect(Collectors.toList());
        return new PageResult<>(messageBackDtoIPage.getRecords(), (int) messageBackDtoIPage.getTotal());
    }

    /**
     * @param messageVo
     * @description: 添加留言
     * @auther apecode
     * @date 2022/6/24 16:37
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMessage(MessageVo messageVo) {
        // 是否需要审核
        WebsiteConfigVo websiteConfigure = websiteService.getWebsiteConfigure();
        // 判断是否是登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Message message = new Message();
        message.setContent(HTMLUtils.filter(messageVo.getContent()))
                .setTheme(StringUtils.isNotBlank(messageVo.getTheme()) ? messageVo.getTheme() : "default")
                .setIsReview(websiteConfigure.getIsMessageReview())
                .setUpdateTime(CommonUtils.getLocalDateTime());
        // 未登录
        if (authentication instanceof AnonymousAuthenticationToken) {
            String ipAddress = IpUtils.getIpAddress(request);
            String source = IpUtils.getIpSource(ipAddress);
            message.setIpAddress(ipAddress)
                    .setIpSource(source)
                    .setNickname(DEFAULT_TOURIST + CommonUtils.getTouristMd5ByIpAddress(ipAddress))
                    .setAvatar(StringUtils.isNotBlank(websiteConfigure.getTouristAvatar()) ? websiteConfigure.getTouristAvatar() : DEFAULT_AVATAR);
        } else {
            // 已登录
            message.setNickname(UserUtils.getLoginUser().getNickname())
                    .setUserId(UserUtils.getLoginUser().getUserInfoId())
                    .setIpAddress(UserUtils.getLoginUser().getIpAddress())
                    .setIpSource(UserUtils.getLoginUser().getIpSource());
        }
        this.save(message);
        // 新留言通知
        CompletableFuture.runAsync(() -> notice(message, websiteConfigure));
    }

    /**
     * @param message
     * @param websiteConfigure
     * @description: 留言通知
     * @auther apecode
     * @date 2022/7/15 15:41
     */
    private void notice(Message message, WebsiteConfigVo websiteConfigure) {
        EmailDto emailDto = null;
        Notice notice;
        if (message.getIsReview()) {
            if (websiteConfigure.getIsEmailNotice() && StringUtils.isNotBlank(websiteConfigure.getReceiveEmail())) {
                emailDto = EmailDto.builder()
                        .email(websiteConfigure.getReceiveEmail())
                        .subject("来自" + websiteConfigure.getWebsiteName() + "网站的留言审核提醒")
                        .text("您收到了一条新的留言，请前往后台管理页面审核")
                        .isHtml(false)
                        .type(3)
                        .build();
            }
            notice = Notice.builder()
                    .title("留言审核提醒")
                    .content("您收到了一条新的留言，请前往后台管理页面审核")
                    .status(false)
                    .updateTime(CommonUtils.getLocalDateTime())
                    .build();
        } else {
            if (websiteConfigure.getIsEmailNotice() && websiteConfigure.getIsMessageNotice() && StringUtils.isNotBlank(websiteConfigure.getReceiveEmail())) {
                emailDto = EmailDto.builder()
                        .email(websiteConfigure.getReceiveEmail())
                        .subject("来自" + websiteConfigure.getWebsiteName() + "网站的留言提醒")
                        .text("您收到了一条新的留言，请前往留言页面查看")
                        .isHtml(false)
                        .type(4)
                        .topicId(message.getId())
                        .build();
            }
            notice = Notice.builder()
                    .title("留言提醒")
                    .content("您收到了一条新的留言，请前往留言页面查看")
                    .status(false)
                    .updateTime(CommonUtils.getLocalDateTime())
                    .build();
        }
        if (Objects.nonNull(emailDto)) {
            String msgId = RandomUtils.getUUID(15);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setCorrelationId(msgId);
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE_NAME, EMAIL_ROUTING_KEY_NAME, new org.springframework.amqp.core.Message(JSON.toJSONBytes(emailDto), messageProperties));
        }
        if (Objects.nonNull(notice)) noticeMapper.insert(notice);
    }

    /**
     * @param ids
     * @description: 删除留言
     * @auther apecode
     * @date 2022/6/24 18:36
     */
    @Override
    public void deleteMessage(List<String> ids) {
        List<Integer> listMessageId = ids.stream().map(id -> {
            Integer mId = SecurityUtils.decrypt(id);
            if (Objects.isNull(mId)) throw new BizException("留言id '" + id + "' 有误");
            return mId;
        }).collect(Collectors.toList());
        this.removeBatchByIds(listMessageId);
    }

    /**
     * @param review
     * @description: 审核留言
     * @auther apecode
     * @date 2022/6/24 18:42
     */
    @Override
    public void updateMessageReview(ReviewVo review) {
        if (!review.getIdList().isEmpty()) {
            List<Message> messageList = review.getIdList().stream().map(id -> {
                Integer mId = SecurityUtils.decrypt(id);
                if (Objects.isNull(mId)) throw new BizException("留言id有误");
                return Message.builder()
                        .id(mId)
                        .isReview(review.getIsReview())
                        .build();
            }).collect(Collectors.toList());
            this.updateBatchById(messageList);
        }
    }

    /**
     * @param condition
     * @return {@link PageResult<MessageFrontDto>}
     * @description: 获取留言列表
     * @auther apecode
     * @date 2022/6/24 18:55
     */
    @Override
    public PageResult<MessageFrontDto> listMessageFront(ConditionVo condition) {
        Page<Message> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        IPage<MessageFrontDto> messageFrontDtoIPage = messageMapper.listMessageFront(page, condition);
        messageFrontDtoIPage.getRecords().stream().filter(message -> Objects.nonNull(message.getUser())).peek(item -> item.setAvatar(item.getUser().getAvatar())).collect(Collectors.toList());
        return new PageResult<>(messageFrontDtoIPage.getRecords(), (int) messageFrontDtoIPage.getTotal());
    }
}
