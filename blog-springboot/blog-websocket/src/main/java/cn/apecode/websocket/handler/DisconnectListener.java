package cn.apecode.websocket.handler;

import cn.apecode.common.dto.UserDetailsDto;
import cn.apecode.websocket.pojo.VisitorUser;
import cn.apecode.websocket.service.ChatMessageService;
import cn.apecode.websocket.service.SystemInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

/**
 * @description: 用户下线
 * @author: apecode
 * @date: 2023-07-19 17:02
 **/
@Component
@RequiredArgsConstructor
public class DisconnectListener implements ApplicationListener<SessionDisconnectEvent> {

    private final ChatMessageService chatMessageService;
    private final SystemInfoService systemInfoService;

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();
        if (user instanceof UsernamePasswordAuthenticationToken) {
            UserDetailsDto userDetailsDto = (UserDetailsDto) ((UsernamePasswordAuthenticationToken) user).getPrincipal();
            chatMessageService.remove(userDetailsDto.getUid());
        }
        if (user instanceof VisitorUser) {
            VisitorUser visitorUser = (VisitorUser) user;
            chatMessageService.remove(visitorUser.getUid());
        }
        // 发送在线人数
        systemInfoService.getOnlineCount();
    }
}
