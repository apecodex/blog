package cn.apecode.blog.mapper;

import cn.apecode.blog.dto.NoticeBackDto;
import cn.apecode.blog.dto.NoticeFrontDto;
import cn.apecode.blog.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 消息通知 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    /**
     * @param page
     * @param isRead
     * @param userInfoId
     * @return {@link IPage<NoticeFrontDto>}
     * @description: 获取用户通知列表
     * @auther apecode
     * @date 2022/7/16 0:59
     */
    IPage<NoticeFrontDto> listNotice(Page<Notice> page, @Param("isRead") Boolean isRead, @Param("userId") Integer userInfoId);

    /**
     * @param isRead
     * @param isSystemNotice
     * @param nickname
     * @return {@link Integer}
     * @description: 查询通知页数
     * @auther apecode
     * @date 2022/7/16 1:21
     */
    Integer noticeCount(@Param("isRead") Boolean isRead, @Param("isSystemNotice") Boolean isSystemNotice, @Param("nickname") String nickname);

    /**
     * @param limitCurrent
     * @param size
     * @param isRead
     * @param isSystemNotice
     * @param nickname
     * @return {@link List<NoticeBackDto>}
     * @description: 查询后台通知列表
     * @auther apecode
     * @date 2022/7/16 1:31
     */
    List<NoticeBackDto> listNoticeBack(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("isRead") Boolean isRead, @Param("isSystemNotice") Boolean isSystemNotice, @Param("nickname") String nickname);
}
