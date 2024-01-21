package cn.apecode.controller;

import cn.apecode.common.annotation.OptLog;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.TalkBackDto;
import cn.apecode.dto.TalkBackOnlyDto;
import cn.apecode.dto.TalkFrontDto;
import cn.apecode.dto.UploadFileInfoDto;
import cn.apecode.service.TalkService;
import cn.apecode.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.apecode.common.constant.OptTypeConst.*;

/**
 * <p>
 * 说说 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-27
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "说说模块")
public class TalkController {

    private final TalkService talkService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或修改说说", httpMethod = "POST")
    @PostMapping(value = "/admin/talk")
    @Decrypt
    public ResponseCode<?> saveOrUpdateTalk(@RequestBody @Validated TalkVo talk) {
        talkService.saveOrUpdateTalk(talk);
        return ResponseCode.ok("保存成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除说说", httpMethod = "DELETE")
    @DeleteMapping("/admin/talk")
    public ResponseCode<?> deleteTalk(@RequestBody String talkId) {
        talkService.deleteTalk(talkId);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "查看后台说说", httpMethod = "GET")
    @GetMapping("/admin/talks")
    @Encrypt
    public ResponseCode<PageResult<TalkBackDto>> listTalkBack(ConditionVo condition) {
        return ResponseCode.ok(talkService.listTalkBack(condition));
    }

    @ApiOperation(value = "点赞说说", httpMethod = "POST")
    @ApiImplicitParam(value = "说说id", name = "talkId", dataTypeClass = String.class)
    @PostMapping("/user/talk/{talkId}/like")
    public ResponseCode<?> talkLike(@PathVariable String talkId) {
        talkService.talkLike(talkId);
        return ResponseCode.ok("成功");
    }

    @ApiOperation(value = "查看说说列表", httpMethod = "GET")
    @GetMapping("/talks")
    @Encrypt
    public ResponseCode<PageResult<TalkFrontDto>> listTalkFront() {
        return ResponseCode.ok(talkService.listTalkFront());
    }

    @ApiOperation(value = "根据id查看说说", httpMethod = "GET")
    @ApiImplicitParam(value = "说说id", name = "talkId", required = true, dataTypeClass = String.class)
    @GetMapping("/talk/{talkId}")
    @Encrypt
    public ResponseCode<TalkFrontDto> getTalkById(@PathVariable String talkId) {
        return ResponseCode.ok(talkService.getTalkById(talkId));
    }

    @ApiOperation(value = "上传说说配图", httpMethod = "POST")
    @ApiImplicitParam(value = "上传说说配图", name = "file", required = true, dataTypeClass = MultipartFile.class)
    @PostMapping(value = "/admin/talk/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseCode<UploadFileInfoDto> uploadTalkWithPicture(@RequestPart(value = "file", required = true) MultipartFile file) {
        return ResponseCode.ok(talkService.uploadTalkWithPicture(file), "上传成功");
    }

    @ApiOperation(value = "根据id获取后台说说", httpMethod = "GET")
    @ApiImplicitParam(value = "说说id", name = "talkId", required = true, dataTypeClass = String.class)
    @GetMapping("/admin/talk/{talkId}")
    @Encrypt
    public ResponseCode<TalkBackOnlyDto> getTalkBackOnlyById(@PathVariable String talkId) {
        return ResponseCode.ok(talkService.getTalkBackOnlyById(talkId));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改说说顶置", httpMethod = "PUT")
    @PutMapping("/admin/talk/top")
    public ResponseCode<?> updateTalkTop(@Validated @RequestBody TopVo top) {
        talkService.updateTalkTop(top);
        return ResponseCode.ok("修改成功");
    }
}
