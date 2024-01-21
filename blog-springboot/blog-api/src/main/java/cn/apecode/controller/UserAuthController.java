package cn.apecode.controller;

import cn.apecode.common.annotation.AccessLimit;
import cn.apecode.common.annotation.OptLog;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.crypto.annotation.Decrypt;
import cn.apecode.crypto.annotation.Encrypt;
import cn.apecode.dto.UserInfoBackDto;
import cn.apecode.dto.UserInfoDto;
import cn.apecode.service.UserAuthService;
import cn.apecode.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.apecode.common.constant.OptTypeConst.UPDATE;

/**
 * <p>
 * 用户账号 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "用户账号模块")
@RequiredArgsConstructor
@RestController
public class UserAuthController {

    private final UserAuthService userAuthService;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public void Login(@Validated LoginParamVo paramVo) {}

    @ApiOperation(value = "退出")
    @PostMapping("/logout")
    public void Logout() {}

    @ApiOperation(value = "获取后台用户列表", httpMethod = "GET")
    @GetMapping("/admin/users")
    @Encrypt
    public ResponseCode<PageResult<UserInfoBackDto>> listUsers(ConditionVo conditionVo) {
        return ResponseCode.ok(userAuthService.listUserInfoBack(conditionVo));
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改密码", httpMethod = "PUT")
    @PutMapping("/user/password")
    @Decrypt
    public ResponseCode<?> updatePassword(@Validated @RequestBody PasswordVo password) {
        userAuthService.updatePassword(password);
        return ResponseCode.ok("新密码修改成功");
    }

    @ApiOperation(value = "找回密码", httpMethod = "PUT")
    @PutMapping("/findPassword")
    @Decrypt
    public ResponseCode<?> findPassword(@Validated @RequestBody FindPasswordVo findPassword) {
        userAuthService.findPassword(findPassword);
        return ResponseCode.ok("新密码修改成功");
    }

    @ApiOperation(value = "用户注册", httpMethod = "POST")
    @PostMapping("/register")
    @Decrypt
    public ResponseCode<?> register(@Validated @RequestBody RegisterVo register) {
        userAuthService.register(register);
        return ResponseCode.ok("注册成功");
    }

    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "发送邮箱验证码", httpMethod = "GET")
    @ApiModelProperty(name = "email", value = "邮箱地址", required = true, dataType = "String")
    @GetMapping("/user/code")
    public ResponseCode<?> sendCode(String email) {
        userAuthService.sendCode(email);
        return ResponseCode.ok("验证码发送成功");
    }

    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改用户禁用状态", httpMethod = "PUT")
    @PutMapping("/admin/user/enable")
    @Decrypt
    public ResponseCode<?> updateUserEnable(@Validated @RequestBody UserEnableVo userEnable) {
        userAuthService.updateUserEnable(userEnable);
        return ResponseCode.ok("修改成功");
    }

    @ApiOperation(value = "第三方QQ登录", httpMethod = "POST")
    @PostMapping("/oauth/qq/callback")
    public ResponseCode<UserInfoDto> qqLogin(@Validated @RequestBody QQLoginVo qqLoginVo) {
        return ResponseCode.ok(userAuthService.qqLogin(qqLoginVo));
    }
}