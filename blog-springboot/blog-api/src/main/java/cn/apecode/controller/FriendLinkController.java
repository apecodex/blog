package cn.apecode.controller;

import cn.apecode.common.annotation.AccessLimit;
import cn.apecode.common.annotation.OptLog;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.FriendLinkBackDto;
import cn.apecode.dto.FriendLinkFrontDto;
import cn.apecode.service.FriendLinkService;
import cn.apecode.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.apecode.common.constant.OptTypeConst.*;

/**
 * <p>
 * 友情链接 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "友链模块")
@RequiredArgsConstructor
@RestController
public class FriendLinkController {

    private final FriendLinkService friendLinkService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或修改友链", httpMethod = "POST")
    @PostMapping("/admin/link")
    @Decrypt
    public ResponseCode<?> saveOrUpdateFriendLike(@Validated @RequestBody FriendLinkVo friendLink) {
        friendLinkService.saveOrUpdateFriendLink(friendLink);
        return ResponseCode.ok("保存成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除友链", httpMethod = "DELETE")
    @ApiImplicitParam(value = "删除友链", name = "friendLinkId", dataTypeClass = String.class, required = true)
    @DeleteMapping("/admin/link/{friendLinkId}")
    public ResponseCode<?> deleteFriendLink(@PathVariable String friendLinkId) {
        friendLinkService.deleteFriendLink(friendLinkId);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "获取后台友链列表", httpMethod = "GET")
    @GetMapping("/admin/links")
    @Encrypt
    public ResponseCode<PageResult<FriendLinkBackDto>> listFriendLinkBack(ConditionVo condition) {
        return ResponseCode.ok(friendLinkService.listFriendLinkBack(condition));
    }

    @ApiOperation(value = "获取友链列表", httpMethod = "GET")
    @GetMapping("/links")
    public ResponseCode<PageResult<FriendLinkFrontDto>> listFriendLinkFront() {
        return ResponseCode.ok(friendLinkService.listFriendLinkFront());
    }

    @AccessLimit(seconds = 30, maxCount = 5)
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "用户保存或修改友链", httpMethod = "POST")
    @PostMapping("/user/link")
    public ResponseCode<?> saveOrUpdateFriendLinkByUser(@Validated @RequestBody FriendLinkUserVo friendLink) {
        friendLinkService.saveOrUpdateFriendLinkByUser(friendLink);
        return ResponseCode.ok("保存成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "用户删除友链", httpMethod = "DELETE")
    @ApiImplicitParam(value = "用户删除友链", name = "friendLinkId", dataTypeClass = String.class, required = true)
    @DeleteMapping("/user/link/{friendLinkId}")
    public ResponseCode<?> deleteFriendLinkById(@PathVariable String friendLinkId) {
        friendLinkService.deleteFriendLinkById(friendLinkId);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "根据用户id获取用户友链", httpMethod = "GET")
    @GetMapping("/user/link")
    public ResponseCode<List<FriendLinkBackDto>> getFriendLinkByUserInfoId() {
        return ResponseCode.ok(friendLinkService.getFriendLinkByUserInfoId());
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "审核友链", httpMethod = "PUT")
    @PutMapping("/admin/links")
    public ResponseCode<?> reviewFriendLinkByIds(@Validated @RequestBody ReviewVo review) {
        friendLinkService.reviewFriendLinkByIds(review);
        return ResponseCode.ok("修改成功");
    }
}
