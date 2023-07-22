package cn.apecode.websocket.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

/**
 * @author apecode
 * @description WebSocket的握手之后拦截器
 * @date 1/7/2023 PM12:46
 */
public class WebSocketHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 设置认证用户
        return (Principal) attributes.get("user");
    }
}
