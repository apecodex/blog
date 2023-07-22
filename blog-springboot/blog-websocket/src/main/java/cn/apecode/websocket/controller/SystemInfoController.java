package cn.apecode.websocket.controller;

import cn.apecode.common.utils.ResponseCode;
import cn.apecode.websocket.service.SystemInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 系统信息控制类
 * @author: apecode
 * @date: 2023-07-21 14:49
 **/
@Api(tags = "SystemInfo")
@RequiredArgsConstructor
@RestController
public class SystemInfoController {

    private final SystemInfoService systemInfoService;

    /**
     * @description: 获取在线人数
     * @return {@link ResponseCode<Integer>}
     * @auther apecode
     * @date 2023/7/21 14:51
    */
    @GetMapping("/getOnlineCount")
    @ApiOperation(value = "获取在线人数", httpMethod = "GET")
    public ResponseCode<Integer> getOnlineCount() {
        return ResponseCode.ok(systemInfoService.getOnlineCount());
    }
}
