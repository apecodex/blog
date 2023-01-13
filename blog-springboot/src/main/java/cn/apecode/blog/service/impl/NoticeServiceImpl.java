package cn.apecode.blog.service.impl;

import cn.apecode.blog.dto.NoticeBackDto;
import cn.apecode.blog.dto.NoticeFrontDto;
import cn.apecode.blog.entity.Notice;
import cn.apecode.blog.entity.UserInfo;
import cn.apecode.blog.exception.BizException;
import cn.apecode.blog.mapper.NoticeMapper;
import cn.apecode.blog.mapper.UserInfoMapper;
import cn.apecode.blog.service.NoticeService;
import cn.apecode.blog.utils.*;
import cn.apecode.blog.vo.DeleteVo;
import cn.apecode.blog.vo.NoticeVo;
import cn.apecode.blog.vo.PageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 消息通知 服务实现类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * @param noticeVo
     * @description: 发送通知
     * @auther apecode
     * @date 2022/7/15 17:03
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendNotice(NoticeVo noticeVo) {
        Integer userId = null;
        if (StringUtils.isNotBlank(noticeVo.getUserId())) {
            userId = SecurityUtils.decrypt(noticeVo.getUserId());
            if (Objects.isNull(userId)) throw new BizException("用户id有误");
            // 判断用户是否存在
            UserInfo userInfo = userInfoMapper.selectById(userId);
            if (Objects.isNull(userInfo)) throw new BizException("用户不存在");
        }
        Notice notice = Notice.builder()
                .title(noticeVo.getTitle())
                .content(noticeVo.getContent())
                .url(Objects.isNull(noticeVo.getUrl()) ? null : noticeVo.getUrl())
                .userId(userId)
                .status(false)
                .updateTime(CommonUtils.getLocalDateTime())
                .build();
        noticeMapper.insert(notice);
    }

    /**
     * @param noticeIds
     * @description: 更新通知已阅
     * @auther apecode
     * @date 2022/7/16 0:07
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateNoticeRead(List<String> noticeIds) {
        if (CollectionUtils.isNotEmpty(noticeIds)) {
            List<Notice> noticeList = noticeIds.stream().map(noticeId -> {
                Integer id = SecurityUtils.decrypt(noticeId);
                if (Objects.isNull(id)) throw new BizException("通知id有误");
                return Notice.builder()
                        .id(id)
                        .status(true)
                        .build();
            }).collect(Collectors.toList());
            this.updateBatchById(noticeList);
        }
    }

    /**
     * @param deleteVo
     * @description: 删除通知
     * @auther apecode
     * @date 2022/7/16 0:18
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteNotice(DeleteVo deleteVo) {
        if (CollectionUtils.isNotEmpty(deleteVo.getIdList())) {
            List<Integer> ids = deleteVo.getIdList().stream().map(noticeId -> {
                Integer id = SecurityUtils.decrypt(noticeId);
                if (Objects.isNull(id)) throw new BizException("通知id有误");
                return id;
            }).collect(Collectors.toList());
            this.removeByIds(ids);
        }
    }

    @Override
    public NoticeFrontDto getNoticeById(String noticeId) {
        Integer id = SecurityUtils.decrypt(noticeId);
        if (Objects.isNull(id)) throw new BizException("通知id有误");
        Notice notice = noticeMapper.selectById(id);
        if (Objects.isNull(notice)) throw new BizException("通知不存在");
        NoticeFrontDto noticeFrontDto = BeanCopyUtils.copyObject(notice, NoticeFrontDto.class);
        noticeFrontDto.setId(SecurityUtils.encrypt(String.valueOf(notice.getId())));
        return noticeFrontDto;
    }

    /**
     * @param isRead
     * @return {@link PageResult<NoticeFrontDto>}
     * @description: 获取用户通知列表
     * @auther apecode
     * @date 2022/7/16 0:50
     */
    @Override
    public PageResult<NoticeFrontDto> listNoticeFront(Boolean isRead) {
        Page<Notice> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        IPage<NoticeFrontDto> noticeFrontDtoIPage = noticeMapper.listNotice(page, isRead, UserUtils.getLoginUser().getUserInfoId());
        noticeFrontDtoIPage.getRecords().stream().peek(notice -> notice.setId(SecurityUtils.encrypt(notice.getId()))).collect(Collectors.toList());
        return new PageResult<>(noticeFrontDtoIPage.getRecords(), (int) noticeFrontDtoIPage.getTotal());
    }

    /**
     * @param isRead
     * @param isSystemNotice
     * @param nickname
     * @return {@link PageResult<NoticeBackDto>}
     * @description: 获取后台通知列表
     * @auther apecode
     * @date 2022/7/16 1:19
     */
    @Override
    public PageResult<NoticeBackDto> listNoticeBack(Boolean isRead, Boolean isSystemNotice, String nickname) {
        Integer count = noticeMapper.noticeCount(isRead, isSystemNotice, nickname);
        if (count == 0) return new PageResult<>();
        List<NoticeBackDto> noticeBackDtoList = noticeMapper.listNoticeBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), isRead, isSystemNotice, nickname);
        noticeBackDtoList.stream().peek(notice -> notice.setId(SecurityUtils.encrypt(notice.getId()))).collect(Collectors.toList());
        return new PageResult<>(noticeBackDtoList, count);
    }

    /**
     * @description: 获取用户未阅通知数量
     * @return {@link Integer}
     * @auther apecode
     * @date 2022/7/17 1:25
    */
    @Override
    public Integer getUserNoReadNoticeCount() {
        Long count = noticeMapper.selectCount(Wrappers.<Notice>lambdaQuery().eq(Notice::getUserId, UserUtils.getLoginUser().getUserInfoId()).eq(Notice::getStatus, false));
        return count.intValue();
    }
}
