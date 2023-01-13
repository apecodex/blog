package cn.apecode.blog.service;

import cn.apecode.blog.dto.NoticeBackDto;
import cn.apecode.blog.dto.NoticeFrontDto;
import cn.apecode.blog.entity.Notice;
import cn.apecode.blog.vo.DeleteVo;
import cn.apecode.blog.vo.NoticeVo;
import cn.apecode.blog.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 消息通知 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
public interface NoticeService extends IService<Notice> {

    /**
     * @description: 发送通知
     * @param noticeVo
     * @auther apecode
     * @date 2022/7/15 16:59
    */
    void sendNotice(NoticeVo noticeVo);

    /**
     * @description: 更新通知已阅
     * @param noticeIds
     * @auther apecode
     * @date 2022/7/16 0:07
    */
    void updateNoticeRead(List<String> noticeIds);

    /**
     * @description: 删除通知
     * @param deleteVo
     * @auther apecode
     * @date 2022/7/16 0:18
    */
    void deleteNotice(DeleteVo deleteVo);

    /**
     * @description: 根据id获取通知
     * @param noticeId
     * @return {@link NoticeFrontDto}
     * @auther apecode
     * @date 2022/7/16 0:37
    */
    NoticeFrontDto getNoticeById(String noticeId);

    /**
     * @description: 获取用户通知列表
     * @param isRead
     * @return {@link PageResult<NoticeFrontDto>}
     * @auther apecode
     * @date 2022/7/16 0:49
    */
    PageResult<NoticeFrontDto> listNoticeFront(Boolean isRead);

    /**
     * @param isRead
     * @param isSystemNotice
     * @param nickname
     * @return {@link PageResult<NoticeBackDto>}
     * @description: 获取后台通知列表
     * @auther apecode
     * @date 2022/7/16 1:19
     */
    PageResult<NoticeBackDto> listNoticeBack(Boolean isRead, Boolean isSystemNotice, String nickname);

    /**
     * @description: 获取用户未阅通知数量
     * @return {@link Integer}
     * @auther apecode
     * @date 2022/7/17 1:25
    */
    Integer getUserNoReadNoticeCount();
}
