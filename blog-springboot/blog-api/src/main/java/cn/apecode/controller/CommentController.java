package cn.apecode.controller;

import cn.apecode.common.annotation.AccessLimit;
import cn.apecode.common.annotation.OptLog;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.CommentBackDto;
import cn.apecode.dto.CommentFrontDto;
import cn.apecode.dto.ReplyDto;
import cn.apecode.service.CommentService;
import cn.apecode.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.apecode.common.constant.OptTypeConst.*;

/**
 * <p>
 * 文章评论 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-27
 */
@Api(tags = "评论模块")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @AccessLimit(seconds = 30, maxCount = 10)
    @OptLog(optType = COMMENT)
    @ApiOperation(value = "添加评论", httpMethod = "POST")
    @PostMapping("/comment")
    @Decrypt
    public ResponseCode<?> saveComment(@Validated @RequestBody CommentVo comment) {
        commentService.saveComment(comment);
        return ResponseCode.ok("添加成功");
    }

    @ApiOperation(value = "查询评论", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(value = "主题id", name = "topicId", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(value = "评论类型", name = "type", required = true, dataTypeClass = Integer.class)})
    @GetMapping("/comments")
    @Encrypt
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
    @Encrypt
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
    @Encrypt
    public ResponseCode<PageResult<CommentBackDto>> listCommentBack(ConditionVo condition) {
        return ResponseCode.ok(commentService.listCommentBack(condition));
    }

}
