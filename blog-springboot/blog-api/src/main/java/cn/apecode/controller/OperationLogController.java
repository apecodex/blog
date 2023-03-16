package cn.apecode.controller;

import cn.apecode.common.annotation.OptLog;
import cn.apecode.dto.LoginLogBackDto;
import cn.apecode.dto.MailLogBackDto;
import cn.apecode.dto.OperationLogBackDto;
import cn.apecode.service.LoginLogService;
import cn.apecode.service.MailLogService;
import cn.apecode.service.OperationLogService;
import cn.apecode.vo.ConditionVo;
import cn.apecode.vo.DeleteVo;
import cn.apecode.vo.PageResult;
import cn.apecode.common.utils.ResponseCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static cn.apecode.common.constant.OptTypeConst.REMOVE;

/**
 * <p>
 * 操作记录 前端控制器
 * </p>
 *
 * @author apecode
 * @since 2022-05-26
 */
@Api(tags = "日志模块")
@RequiredArgsConstructor
@RestController
public class OperationLogController {

    private final OperationLogService operationLogService;
    private final LoginLogService loginLogService;
    private final MailLogService mailLogService;

    @ApiOperation(value = "获取操作日志", httpMethod = "GET")
    @GetMapping("/admin/operation/logs")
    public ResponseCode<PageResult<OperationLogBackDto>> listOperationLog() {
        return ResponseCode.ok(operationLogService.listOperationLog());
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除操作日志", httpMethod = "DELETE")
    @DeleteMapping("/admin/operation/logs")
    public ResponseCode<?> deleteOperationLog(@Validated @RequestBody DeleteVo delete) {
        operationLogService.deleteOperationLog(delete);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "获取登录日志", httpMethod = "GET")
    @GetMapping("/admin/login/logs")
    public ResponseCode<PageResult<LoginLogBackDto>> listLoginLog() {
        return ResponseCode.ok(loginLogService.listLoginLog());
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除登录日志", httpMethod = "DELETE")
    @DeleteMapping("/admin/login/logs")
    public ResponseCode<?> deleteLoginLog(@Validated @RequestBody DeleteVo delete) {
        loginLogService.deleteLoginLog(delete);
        return ResponseCode.ok("删除成功");
    }

    @ApiOperation(value = "获取邮件日志", httpMethod = "GET")
    @GetMapping("/admin/email/logs")
    public ResponseCode<PageResult<MailLogBackDto>> listMailLog(ConditionVo condition) {
        return ResponseCode.ok(mailLogService.listMailLog(condition));
    }

    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除邮件日志", httpMethod = "DELETE")
    @DeleteMapping("/admin/email/logs")
    public ResponseCode<?> deleteMailLog(@Validated @RequestBody DeleteVo delete) {
        mailLogService.deleteMailLog(delete);
        return ResponseCode.ok("删除成功");
    }

}
