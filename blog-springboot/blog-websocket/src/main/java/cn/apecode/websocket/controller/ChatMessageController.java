package cn.apecode.websocket.controller;

import cn.apecode.common.utils.HTMLUtils;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.websocket.pojo.OnlineUser;
import cn.apecode.websocket.pojo.VisitorUser;
import cn.apecode.websocket.service.ChatMessageService;
import cn.apecode.websocket.utils.VisitorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

/**
 * @author apecode
 * @description WebSocket控制类
 * @date 20/6/2023 PM1:56
 */
@Api(tags = "聊天室")
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    /**
     * @description: 获取聊天室游客信息
     * @param request
     * @return {@link ResponseCode<VisitorUser>}
     * @auther apecode
     * @date 2023/7/21 11:55
    */
    @GetMapping(value = "/getChatVisitorInfo")
    @ApiOperation(value = "获取聊天室游客信息", httpMethod = "GET")
    public ResponseCode<VisitorUser> getVisitorInfo(HttpServletRequest request) {
        VisitorUser visitorUser = VisitorUtils.getVisitorUserInfo(request);
        return ResponseCode.ok(visitorUser);
    }

    /**
     * @param accessor
     * @description: 加入或离开聊天室
     * @auther apecode
     * @date 2023/7/20 15:09
     */
    @MessageMapping("/joinOrLeaveChatRoom")
    public void joinOrLeaveChatRoom(StompHeaderAccessor accessor) {
        // 获取类型，加入还是离开
        Integer type = Integer.valueOf(Objects.requireNonNull(accessor.getFirstNativeHeader("type")));
        Principal user = accessor.getUser();
        chatMessageService.joinOrLeaveChatRoom(type, user);
        // 返回在线用户列表
        chatMessageService.sendOnlineUserList();
    }

    /**
     * @param messageVo
     * @param accessor
     * @description: 发送聊天消息
     * @auther apecode
     * @date 2/7/2023 PM5:18
     */
    @MessageMapping("/sendChatMessage")
    public void sendChatMessage(@Payload String message, StompHeaderAccessor accessor) {
        Principal user = accessor.getUser();
        // 过滤敏感词和html标签
        String m = HTMLUtils.filter(message);
        // 发送消息
        chatMessageService.sendChatMessage(m, user);
    }

    /**
     * @description: 获取在线用户列表
     * @auther apecode
     * @date 2023/7/21 0:35
    */
    @GetMapping(value = "/getOnlineUserList")
    @ApiOperation(value = "获取在线用户列表", httpMethod = "GET")
    public ResponseCode<List<OnlineUser>> getOnlineUserList() {
        List<OnlineUser> onlineUserList = chatMessageService.getOnlineUserList();
        return ResponseCode.ok(onlineUserList);
    }
}
