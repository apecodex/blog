package cn.apecode.controller;

import cn.apecode.common.annotation.AccessLimit;
import cn.apecode.common.annotation.OptLog;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.UploadFileInfoDto;
import cn.apecode.dto.UserInfoFrontDto;
import cn.apecode.service.UserInfoService;
import cn.apecode.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static cn.apecode.common.constant.OptTypeConst.*;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "用户信息模块")
@RequiredArgsConstructor
@RestController
public class UserInfoController {

    private final UserInfoService userInfoService;

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户角色", httpMethod = "PUT")
    @PutMapping("/admin/user/role")
    @Decrypt
    public ResponseCode<?> updateUserRole(@Validated @RequestBody UserRoleVo userRole) {
        userInfoService.updateUserRole(userRole);
        return ResponseCode.ok("修改成功");
    }

    @AccessLimit(seconds = 30, maxCount = 5)
    @ApiOperation(value = "更新用户头像")
    @PostMapping(value = "/user/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseCode<UploadFileInfoDto> updateUserAvatar(@ApiParam(value = "上传用户头像", required = true) @RequestPart("file") MultipartFile file) {
        return ResponseCode.ok(userInfoService.updateUserAvatar(file), "上传成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "绑定用户邮箱", httpMethod = "POST")
    @PostMapping("/user/email/bind")
    public ResponseCode<String> saveUserEmail(@Validated @RequestBody SaveOrUnbindEmailVo saveEmail) {
        return ResponseCode.ok(userInfoService.saveUserEmail(saveEmail), "绑定成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "更新用户信息", httpMethod = "PUT")
    @PutMapping("/user/info")
    @Decrypt
    public ResponseCode<?> updateUserInfo(@Validated @RequestBody UserInfoVo userInfo) {
        userInfoService.updateUserInfo(userInfo);
        return ResponseCode.ok("更新成功");
    }

    @ApiOperation(value = "获取用户信息", httpMethod = "GET")
    @GetMapping("/user/info")
    @Encrypt
    public ResponseCode<UserInfoFrontDto> getUserInfo() {
        return ResponseCode.ok(userInfoService.getUserInfo());
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "解绑用户邮箱", httpMethod = "PUT")
    @PutMapping("/user/email/unbind")
    @Decrypt
    public ResponseCode<String> unbindEmail(@Validated @RequestBody SaveOrUnbindEmailVo unbindEmailVo) {
        return ResponseCode.ok(userInfoService.unbindEmail(unbindEmailVo), "解绑成功");
    }

    @OptLog(optType = SAVE)
    @ApiOperation(value = "绑定QQ", httpMethod = "POST")
    @PostMapping("/user/qq/bind")
    public ResponseCode<?> bindQQ(@Validated @RequestBody QQLoginVo qqLoginVo) {
        userInfoService.bindQQ(qqLoginVo);
        return ResponseCode.ok("绑定成功");
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "解绑QQ", httpMethod = "PUT")
    @PutMapping("/user/qq/unbind")
    public ResponseCode<?> unbindQQ() {
        userInfoService.unbindQQ();
        return ResponseCode.ok("解绑成功");
    }
}
