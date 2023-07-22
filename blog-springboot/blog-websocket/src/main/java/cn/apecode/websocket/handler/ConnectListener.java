package cn.apecode.websocket.handler;

import cn.apecode.common.dto.UserDetailsDto;
import cn.apecode.websocket.pojo.OnlineUser;
import cn.apecode.websocket.pojo.VisitorUser;
import cn.apecode.websocket.service.ChatMessageService;
import cn.apecode.websocket.service.SystemInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Principal;
import java.util.Objects;

import static cn.apecode.common.constant.CommonConst.DEFAULT_AVATAR;
import static cn.apecode.common.constant.CommonConst.DEFAULT_TOURIST;

/**
 * @description: 用户上线
 * @author: apecode
 * @date: 2023-07-20 17:09
 **/
@Component
@RequiredArgsConstructor
public class ConnectListener implements ApplicationListener<SessionConnectEvent> {

    private final ChatMessageService chatMessageService;
    private final SystemInfoService systemInfoService;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        // 设置当前访问器的认证用户
        Principal user = accessor.getUser();
        if (Objects.nonNull(user)) {
            OnlineUser onlineUser = new OnlineUser();
            // 登录用户
            if (user instanceof UsernamePasswordAuthenticationToken) {
                UserDetailsDto userDetailsDto = (UserDetailsDto) ((UsernamePasswordAuthenticationToken) user).getPrincipal();
                onlineUser.setUid(userDetailsDto.getUid());
                onlineUser.setNickname(userDetailsDto.getNickname());
                onlineUser.setAvatar(userDetailsDto.getAvatar());
                onlineUser.setIpAddress(userDetailsDto.getIpAddress());
                onlineUser.setIpSource(userDetailsDto.getIpSource());
            }
            // 游客
            if (user instanceof VisitorUser) {
                VisitorUser visitorUser = (VisitorUser) user;
                onlineUser.setUid(visitorUser.getUid());
                onlineUser.setNickname(DEFAULT_TOURIST + visitorUser.getUid());
                onlineUser.setAvatar(DEFAULT_AVATAR);
                onlineUser.setIpAddress(visitorUser.getIpAddress());
                onlineUser.setIpSource(visitorUser.getIpSource());
            }
            accessor.setUser(user);
            // 将在线用户缓存
            chatMessageService.addUser(onlineUser);
            // 发送在线人数
            systemInfoService.getOnlineCount();
        }
    }
}
