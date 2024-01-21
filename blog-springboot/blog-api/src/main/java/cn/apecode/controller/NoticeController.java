package cn.apecode.controller;

import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.NoticeBackDto;
import cn.apecode.dto.NoticeFrontDto;
import cn.apecode.service.NoticeService;
import cn.apecode.vo.DeleteVo;
import cn.apecode.vo.NoticeVo;
import cn.apecode.vo.PageResult;
import cn.apecode.common.utils.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 消息通知 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "消息通知模块")
@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    @ApiOperation(value = "发送通知", httpMethod = "POST")
    @PostMapping("/admin/notice")
    @Decrypt
    public ResponseCode<?> sendNotice(@Validated @RequestBody NoticeVo noticeVo) {
        noticeService.sendNotice(noticeVo);
        return ResponseCode.ok("发送成功");
    }

    @ApiOperation(value = "更新通知已阅", httpMethod = "PUT")
    @PutMapping("/user/notice")
    public ResponseCode<?> updateNoticeRead(@RequestBody List<String> noticeIds) {
        noticeService.updateNoticeRead(noticeIds);
        return ResponseCode.ok("修改成功");
    }

    @ApiOperation(value = "删除通知", httpMethod = "DELETE")
    @DeleteMapping("/admin/notice")
    @Decrypt
    public ResponseCode<?> deleteNotice(@Validated @RequestBody DeleteVo deleteVo) {
        noticeService.deleteNotice(deleteVo);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "根据id获取通知", httpMethod = "GET")
    @ApiImplicitParam(value = "通知id", name = "noticeId", required = true, dataTypeClass = String.class)
    @GetMapping("/user/notice/{noticeId}")
    public ResponseCode<NoticeFrontDto> getNoticeById(@PathVariable String noticeId) {
        return ResponseCode.ok(noticeService.getNoticeById(noticeId));
    }

    @ApiOperation(value = "获取用户通知列表", httpMethod = "GET")
    @ApiImplicitParam(value = "是否已阅", name = "isRead", required = true, dataTypeClass = Boolean.class)
    @GetMapping("/user/notice")
    @Encrypt
    public ResponseCode<PageResult<NoticeFrontDto>> listNoticeFront(Boolean isRead) {
        return ResponseCode.ok(noticeService.listNoticeFront(isRead));
    }

    @ApiOperation(value = "获取后台通知列表", httpMethod = "GET")
    @ApiImplicitParams({@ApiImplicitParam(value = "是否已阅", name = "isRead", required = true, dataTypeClass = Boolean.class),
            @ApiImplicitParam(value = "是否系统通知", name = "isSystemNotice", dataTypeClass = Boolean.class),
            @ApiImplicitParam(value = "用户昵称", name = "nickname", dataTypeClass = String.class)})
    @GetMapping("/admin/notice")
    @Encrypt
    public ResponseCode<PageResult<NoticeBackDto>> listNoticeBack(Boolean isRead, Boolean isSystemNotice, String nickname) {
        return ResponseCode.ok(noticeService.listNoticeBack(isRead, isSystemNotice, nickname));
    }

    @ApiOperation(value = "获取用户未阅通知数量", httpMethod = "GET")
    @GetMapping("/user/notice/noread")
    public ResponseCode<Integer> getUserNoReadNoticeCount() {
        return ResponseCode.ok(noticeService.getUserNoReadNoticeCount());
    }

}
