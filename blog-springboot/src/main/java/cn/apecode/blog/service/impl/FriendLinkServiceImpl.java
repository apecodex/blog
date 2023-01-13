package cn.apecode.blog.service.impl;

import cn.apecode.blog.dto.EmailDto;
import cn.apecode.blog.dto.FriendLinkBackDto;
import cn.apecode.blog.dto.FriendLinkFrontDto;
import cn.apecode.blog.dto.UserDetailsDto;
import cn.apecode.blog.entity.FriendLink;
import cn.apecode.blog.entity.Notice;
import cn.apecode.blog.exception.BizException;
import cn.apecode.blog.mapper.FriendLinkMapper;
import cn.apecode.blog.service.FriendLinkService;
import cn.apecode.blog.service.NoticeService;
import cn.apecode.blog.service.WebsiteService;
import cn.apecode.blog.utils.*;
import cn.apecode.blog.vo.*;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static cn.apecode.blog.constant.RabbitMQPrefixConst.EMAIL_EXCHANGE_NAME;
import static cn.apecode.blog.constant.RabbitMQPrefixConst.EMAIL_ROUTING_KEY_NAME;


/**
 * <p>
 * 友情链接 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Service
public class FriendLinkServiceImpl extends ServiceImpl<FriendLinkMapper, FriendLink> implements FriendLinkService {

    @Autowired
    private FriendLinkMapper friendLinkMapper;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private WebsiteService websiteService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @param friendLink
     * @description: 保存或修改友链
     * @auther apecode
     * @date 2022/6/24 21:24
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateFriendLink(FriendLinkVo friendLink) {
        FriendLink link = BeanCopyUtils.copyObject(friendLink, FriendLink.class);
        if (StringUtils.isNotBlank(friendLink.getId())) {
            Integer id = SecurityUtils.decrypt(friendLink.getId());
            if (Objects.isNull(id)) throw new BizException("友链id有误");
            // 判断友链是否存在
            FriendLink existsFriendLink = friendLinkMapper.selectOne(Wrappers.<FriendLink>lambdaQuery().eq(FriendLink::getId, id));
            if (Objects.isNull(existsFriendLink)) throw new BizException("友链不存在");
            link.setId(id);
        } else {
            link.setUserId(UserUtils.getLoginUser().getUserInfoId());
        }
        link.setUpdateTime(CommonUtils.getLocalDateTime());
        this.saveOrUpdate(link);
    }

    /**
     * @param friendLinkId
     * @description: 删除友链
     * @auther apecode
     * @date 2022/6/24 22:23
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteFriendLink(String friendLinkId) {
        Integer id = SecurityUtils.decrypt(friendLinkId);
        if (Objects.isNull(id)) throw new BizException("友链id有误");
        friendLinkMapper.deleteById(id);
    }

    /**
     * @param condition
     * @return {@link PageResult<FriendLinkBackDto>}
     * @description: 获取后台友链列表
     * @auther apecode
     * @date 2022/6/24 22:30
     */
    @Override
    public PageResult<FriendLinkBackDto> listFriendLinkBack(ConditionVo condition) {
        Page<FriendLink> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        IPage<FriendLinkBackDto> friendLinkBackDtoIPage = friendLinkMapper.listFriendLinkBack(page, condition);
        friendLinkBackDtoIPage.getRecords().stream().peek(friendLink -> friendLink.setId(SecurityUtils.encrypt(friendLink.getId()))).collect(Collectors.toList());
        return new PageResult<>(friendLinkBackDtoIPage.getRecords(), (int) friendLinkBackDtoIPage.getTotal());
    }

    /**
     * @return {@link PageResult<FriendLinkFrontDto>}
     * @description: 获取友链列表
     * @auther apecode
     * @date 2022/6/24 22:58
     */
    @Override
    public PageResult<FriendLinkFrontDto> listFriendLinkFront() {
        Page<FriendLink> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        IPage<FriendLinkFrontDto> friendLinkFrontDtoIPage = friendLinkMapper.listFriendLinkFront(page);
        return new PageResult<>(friendLinkFrontDtoIPage.getRecords(), (int) friendLinkFrontDtoIPage.getTotal());
    }

    /**
     * @param friendLink
     * @description: 用户保存或修改友链
     * @auther apecode
     * @date 2022/6/24 23:21
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateFriendLinkByUser(FriendLinkUserVo friendLink) {
        FriendLink link = BeanCopyUtils.copyObject(friendLink, FriendLink.class);
        if (StringUtils.isNoneBlank(friendLink.getId())) {
            Integer id = SecurityUtils.decrypt(friendLink.getId());
            if (Objects.isNull(id)) throw new BizException("友链id有误");
            boolean exists = friendLinkMapper.exists(Wrappers.<FriendLink>lambdaQuery().eq(FriendLink::getId, id).eq(FriendLink::getUserId, UserUtils.getLoginUser().getUserInfoId()));
            if (!exists) throw new BizException("修改失败，友链与账号不匹配");
            link.setId(id);
        } else {
            // 查询该用户是否存在友链
            boolean exists = friendLinkMapper.exists(Wrappers.<FriendLink>lambdaQuery().eq(FriendLink::getUserId, UserUtils.getLoginUser().getUserInfoId()));
            if (exists) throw new BizException("无需重复添加友链");
        }
        link.setUserId(UserUtils.getLoginUser().getUserInfoId());
        link.setIsReview(true);
        link.setUpdateTime(CommonUtils.getLocalDateTime());
        this.saveOrUpdate(link);
        // 获取当前用户
        UserDetailsDto nowUser = UserUtils.getLoginUser();
        CompletableFuture.runAsync(() -> sendNoticeReview(friendLink.getId(), nowUser));
    }

    /**
     * @description: 审核通知
     * @auther apecode
     * @date 25/9/2022 PM7:42
     */
    private void sendNoticeReview(String id, UserDetailsDto nowUser) {
        Notice notice = null;
        EmailDto emailDto = null;
        if (StringUtils.isNoneBlank(id)) {
            notice = Notice.builder()
                    .title("友链审核提醒")
                    .content("来自" + nowUser.getNickname() + " 的修改友链，请前往友链管理页面审核")
                    .status(false)
                    .updateTime(CommonUtils.getLocalDateTime())
                    .build();
            emailDto = EmailDto.builder()
                    .text("来自" + nowUser.getNickname() + " 的修改友链，请前往友链管理页面审核")
                    .isHtml(false)
                    .type(3)
                    .build();
        } else {
            notice = Notice.builder()
                    .title("友链审核提醒")
                    .content("来自" + nowUser.getNickname() + "的友链申请，请前往友链管理页面审核")
                    .status(false)
                    .updateTime(CommonUtils.getLocalDateTime())
                    .build();
            emailDto = EmailDto.builder()
                    .text("来自" + nowUser.getNickname() + "的友链申请，请前往友链管理页面审核")
                    .isHtml(false)
                    .type(3)
                    .build();
        }
        noticeService.save(notice);
        WebsiteConfigVo websiteConfigure = websiteService.getWebsiteConfigure();
        // 发送邮件通知
        if (Objects.nonNull(websiteConfigure)) {
            emailDto.setEmail(websiteConfigure.getReceiveEmail());
            emailDto.setSubject("来自" + websiteConfigure.getWebsiteName() + "网站的友链通知");
            String msgId = RandomUtils.getUUID(15);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setCorrelationId(msgId);
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE_NAME, EMAIL_ROUTING_KEY_NAME, new Message(JSON.toJSONBytes(emailDto), messageProperties));
        }
    }

    /**
     * @param friendLinkId
     * @description: 用户删除友链
     * @auther apecode
     * @date 2022/6/24 23:37
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteFriendLinkById(String friendLinkId) {
        Integer id = SecurityUtils.decrypt(friendLinkId);
        if (Objects.isNull(id)) throw new BizException("友链id有误");
        FriendLink friendLink = friendLinkMapper.selectById(id);
        if (Objects.isNull(friendLink)) {
            throw new BizException("友链不存在");
        }
        if (friendLink.getUserId() != UserUtils.getLoginUser().getUserInfoId()) {
            throw new BizException("删除失败，无权限删除其他友链");
        }
        friendLinkMapper.deleteById(id);
    }

    /**
     * @return {@link List<FriendLinkFrontDto>}
     * @description: 根据用户id获取用户友链
     * @auther apecode
     * @date 2022/6/25 0:12
     */
    @Override
    public List<FriendLinkBackDto> getFriendLinkByUserInfoId() {
        List<FriendLink> friendLinks = friendLinkMapper.selectList(Wrappers.<FriendLink>lambdaQuery().eq(FriendLink::getUserId, UserUtils.getLoginUser().getUserInfoId()));
        return friendLinks.stream().map((friendLink -> {
            FriendLinkBackDto friendLinkBackDto = BeanCopyUtils.copyObject(friendLink, FriendLinkBackDto.class);
            friendLinkBackDto.setId(SecurityUtils.encrypt(String.valueOf(friendLink.getId())));
            return friendLinkBackDto;
        })).collect(Collectors.toList());
    }

    /**
     * @param review
     * @description: 审核友链
     * @auther apecode
     * @date 2022/6/25 0:27
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void reviewFriendLinkByIds(ReviewVo review) {
        if (!review.getIdList().isEmpty()) {
            List<FriendLink> friendLinkList = review.getIdList().stream().map(id -> {
                Integer fId = SecurityUtils.decrypt(id);
                if (Objects.isNull(fId)) throw new BizException("友链id '" + id + "' 有误");
                return FriendLink.builder()
                        .id(fId)
                        .isReview(review.getIsReview())
                        .remark(review.getRemark())
                        .build();
            }).collect(Collectors.toList());
            this.updateBatchById(friendLinkList);
            // 审核通过发送通知给用户
            if (review.getIsReview()) {
                CompletableFuture.runAsync(() -> sendNoticeUserReviewed(friendLinkList));
            }
        }
    }

    /**
     * @param friendLinkList
     * @description: 通知用户已审核
     * @auther apecode
     * @date 25/9/2022 PM8:02
     */
    private void sendNoticeUserReviewed(List<FriendLink> friendLinkList) {
        List<Integer> ids = friendLinkList.stream().map(FriendLink::getId).collect(Collectors.toList());
        List<FriendLink> friendLinks = friendLinkMapper.selectList(Wrappers.<FriendLink>lambdaQuery().select(FriendLink::getUserId).in(FriendLink::getId, ids));
        List<Notice> noticeList = friendLinks.stream().map(friendLink -> Notice.builder()
                .userId(friendLink.getUserId())
                .title("审核提醒")
                .content("您的友链申请已通过审核")
                .url(null)
                .status(false)
                .updateTime(CommonUtils.getLocalDateTime())
                .build()).collect(Collectors.toList());
        noticeService.saveBatch(noticeList);
    }
}
