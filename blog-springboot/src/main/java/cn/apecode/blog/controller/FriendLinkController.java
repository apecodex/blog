package cn.apecode.blog.controller;

import cn.apecode.blog.annotation.AccessLimit;
import cn.apecode.blog.annotation.OptLog;
import cn.apecode.blog.dto.FriendLinkBackDto;
import cn.apecode.blog.dto.FriendLinkFrontDto;
import cn.apecode.blog.service.FriendLinkService;
import cn.apecode.blog.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import static cn.apecode.blog.constant.OptTypeConst.*;

/**
 * <p>
 * 友情链接 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "友链模块")
@RestController
public class FriendLinkController {

    @Autowired
    private FriendLinkService friendLinkService;

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或修改友链", httpMethod = "POST")
    @PostMapping("/admin/link")
    public ResponseCode<?> saveOrUpdateFriendLike(@Validated FriendLinkVo friendLink) {
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
    public ResponseCode<?> saveOrUpdateFriendLinkByUser(@Validated FriendLinkUserVo friendLink) {
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
