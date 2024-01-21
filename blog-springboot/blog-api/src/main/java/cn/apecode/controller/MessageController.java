package cn.apecode.controller;

import cn.apecode.common.annotation.AccessLimit;
import cn.apecode.common.annotation.OptLog;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.MessageBackDto;
import cn.apecode.dto.MessageFrontDto;
import cn.apecode.service.MessageService;
import cn.apecode.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.apecode.common.constant.OptTypeConst.*;

/**
 * <p>
 * 留言 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "留言模块")
@RequiredArgsConstructor
@RestController
public class MessageController {

    private final MessageService messageService;

    @ApiOperation(value = "获取后台留言列表", httpMethod = "GET")
    @GetMapping("/admin/messages")
    @Encrypt
    public ResponseCode<PageResult<MessageBackDto>> listMessageBack(ConditionVo condition) {
        return ResponseCode.ok(messageService.listMessageBack(condition));
    }

    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "添加留言", httpMethod = "POST")
    @PostMapping("/message")
    @Decrypt
    public ResponseCode<?> saveMessage(@Validated @RequestBody MessageVo message) {
        messageService.saveMessage(message);
        return ResponseCode.ok("谢谢你的留言哦~");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除留言", httpMethod = "DELETE")
    @DeleteMapping("/admin/message")
    @Decrypt
    public ResponseCode<?> deleteMessage(@RequestBody List<String> ids) {
        messageService.deleteMessage(ids);
        return ResponseCode.ok("删除成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "审核留言", httpMethod = "PUT")
    @PutMapping("/admin/message")
    @Decrypt
    public ResponseCode<?> updateMessageReview(@Validated @RequestBody ReviewVo review) {
        messageService.updateMessageReview(review);
        return ResponseCode.ok("修改成功");
    }

    @ApiOperation(value = "获取留言列表", httpMethod = "GET")
    @GetMapping("/messages")
    @Encrypt
    public ResponseCode<PageResult<MessageFrontDto>> listMessageFront(ConditionVo condition) {
        return ResponseCode.ok(messageService.listMessageFront(condition));
    }
}
