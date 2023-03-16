package cn.apecode.service;

import cn.apecode.dto.CommentBackDto;
import cn.apecode.dto.CommentFrontDto;
import cn.apecode.dto.ReplyDto;
import cn.apecode.entity.Comment;
import cn.apecode.vo.CommentVo;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.PageResult;
import cn.apecode.vo.ReviewVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文章评论 服务类
 * </p>
 *
 * @author apecode
 * @since 2022-05-27
 */
public interface CommentService extends IService<Comment> {

    /**
     * @param comment
     * @description: 添加评论
     * @auther apecode
     * @date 2022/7/7 19:51
     */
    void saveComment(CommentVo comment);

    /**
     * @param topicId
     * @param type
     * @return {@link PageResult<CommentFrontDto>}
     * @description: 查询评论
     * @auther apecode
     * @date 2022/7/8 1:10
     */
    PageResult<CommentFrontDto> listComment(String topicId, Integer type);

    /**
     * @param commentId
     * @description: 点赞评论
     * @auther apecode
     * @date 2022/7/9 1:00
     */
    void likeComment(String commentId);

    /**
     * @description: 查询评论下的回复
     * @param commentId
     * @return {@link List<ReplyDto>}
     * @auther apecode
     * @date 2022/7/9 13:11
    */
    List<ReplyDto> listRepliesByCommentId(String commentId);

    /**
     * @description: 审核评论
     * @param review
     * @auther apecode
     * @date 2022/7/9 18:06
    */
    void updateCommentReview(ReviewVo review);

    /**
     * @description: 删除评论
     * @param commentId
     * @auther apecode
     * @date 2022/7/9 18:33
    */
    void deleteComment(String commentId);

    /**
     * @description: 查询后台评论
     * @param condition
     * @return {@link PageResult<CommentBackDto>}
     * @auther apecode
     * @date 2022/7/9 21:21
    */
    PageResult<CommentBackDto> listCommentBack(ConditionVo condition);
}
