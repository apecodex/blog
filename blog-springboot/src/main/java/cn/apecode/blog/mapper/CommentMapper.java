package cn.apecode.blog.mapper;

import cn.apecode.blog.dto.*;
import cn.apecode.blog.entity.Comment;
import cn.apecode.blog.vo.ConditionVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文章评论 Mapper 接口
 * </p>
 *
 * @author apecode
 * @since 2022-05-27
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    /**
     * @param limitCurrent
     * @param size
     * @param topicId
     * @param type
     * @return {@link List<CommentFrontDto>}
     * @description: 根据主题id和评论类型查找所有父评论
     * @auther apecode
     * @date 2022/7/8 18:21
     */
    List<CommentFrontDto> listParentComment(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("topicId") Integer topicId, @Param("type") Integer type);

    /**
     * @param limitCurrent
     * @param size
     * @return {@link List<ReplyDto>}
     * @description: 通过父评论查询子评论
     * @auther apecode
     * @date 2022/7/8 20:48
     */
    List<ReplyDto> listReplies(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("parentIds") List<String> parentIds);

    /**
     * @param parentIds
     * @param type
     * @return {@link List<ReplyCountDto>}
     * @description: 根据评论id查询回复量
     * @auther apecode
     * @date 2022/7/9 0:41
     */
    List<ReplyCountDto> replyCountByCommentIds(@Param("parentIds") List<String> parentIds);

    /**
     * @param limitCurrent
     * @param size
     * @param commentId
     * @return {@link List<ReplyDto>}
     * @description: 通过页码查询评论下的回复
     * @auther apecode
     * @date 2022/7/9 13:22
     */
    List<ReplyDto> listRepliesByCommentId(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("commentId") Integer commentId);

    /**
     * @param talkIds
     * @return {@link List<ReplyCountDto>}
     * @description: 根据说说id获取评论数量
     * @auther apecode
     * @date 2022/7/9 17:34
     */
    List<ReplyCountDto> listTalkCommentCountByTalkId(@Param("talkIds") List<String> talkIds);

    /**
     * @description: 查询后台评论
     * @param limitCurrent
     * @param size
     * @param condition
     * @return {@link List<CommentBackDto>}
     * @auther apecode
     * @date 2022/7/9 22:06
    */
    List<CommentBackDto> listCommentBack(@Param("current") Long limitCurrent, @Param("size") Long size, @Param("condition") ConditionVo condition);

    /**
     * @description: 查询后台评论量
     * @param condition
     * @return {@link Integer}
     * @auther apecode
     * @date 2022/7/9 22:38
    */
    Integer countComment(@Param("condition") ConditionVo condition);

    /**
     * @description: 获取最新评论
     * @param
     * @return {@link List<NewCommentDto>}
     * @auther apecode
     * @date 28/10/2022 PM2:35
    */
    List<NewCommentDto> getNewsComment();
}
