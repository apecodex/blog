package cn.apecode.blog.controller;

import cn.apecode.blog.annotation.AccessLimit;
import cn.apecode.blog.annotation.OptLog;
import cn.apecode.blog.dto.CommentBackDto;
import cn.apecode.blog.dto.CommentFrontDto;
import cn.apecode.blog.dto.ReplyDto;
import cn.apecode.blog.service.CommentService;
import cn.apecode.blog.vo.ResponseCode;
import cn.apecode.blog.vo.CommentVo;
import cn.apecode.blog.vo.PageResult;
import cn.apecode.blog.vo.ReviewVo;
import cn.apecode.blog.vo.ConditionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.apecode.blog.constant.OptTypeConst.*;

/**
 * <p>
 * 文章评论 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-27
 */
@Api(tags = "评论模块")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @AccessLimit(seconds = 30, maxCount = 10)
    @OptLog(optType = COMMENT)
    @ApiOperation(value = "添加评论", httpMethod = "POST")
    @PostMapping("/comment")
    public ResponseCode<?> saveComment(@Validated CommentVo comment) {
        commentService.saveComment(comment);
        return ResponseCode.ok("添加成功");
    }

    @ApiOperation(value = "查询评论", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(value = "主题id", name = "topicId", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(value = "评论类型", name = "type", required = true, dataTypeClass = Integer.class)})
    @GetMapping("/comments")
    public ResponseCode<PageResult<CommentFrontDto>> listComment(String topicId, Integer type) {
        return ResponseCode.ok(commentService.listComment(topicId, type));
    }

    @ApiOperation(value = "点赞评论", httpMethod = "POST")
    @ApiImplicitParam(value = "评论id", name = "commentId", required = true, dataTypeClass = String.class)
    @PostMapping("/comment/{commentId}/like")
    public ResponseCode<?> likeComment(@PathVariable String commentId) {
        commentService.likeComment(commentId);
        return ResponseCode.ok("成功");
    }

    @ApiOperation(value = "查询评论下的回复", httpMethod = "GET")
    @ApiImplicitParam(value = "评论id", name = "commentId", required = true, dataTypeClass = String.class)
    @GetMapping("/comment/{commentId}/replies")
    public ResponseCode<List<ReplyDto>> listRepliesByCommentId(@PathVariable String commentId) {
        return ResponseCode.ok(commentService.listRepliesByCommentId(commentId));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "审核评论", httpMethod = "PUT")
    @PutMapping("/admin/comment")
    public ResponseCode<?> updateCommentReview(@Validated @RequestBody ReviewVo review) {
        commentService.updateCommentReview(review);
        return ResponseCode.ok("修改成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除评论", httpMethod = "DELETE")
    @DeleteMapping("/admin/comment")
    public ResponseCode<?> deleteComment(String commentId) {
        commentService.deleteComment(commentId);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "查询后台评论", httpMethod = "GET")
    @GetMapping("/admin/comments")
    public ResponseCode<PageResult<CommentBackDto>> listCommentBack(ConditionVo condition) {
        return ResponseCode.ok(commentService.listCommentBack(condition));
    }

}
