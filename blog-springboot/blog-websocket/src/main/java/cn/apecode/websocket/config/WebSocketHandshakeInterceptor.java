package cn.apecode.websocket.config;

import cn.apecode.common.dto.UserDetailsDto;
import cn.apecode.common.utils.CommonUtils;
import cn.apecode.common.utils.IpUtils;
import cn.apecode.common.utils.JwtUtils;
import cn.apecode.service.RedisService;
import cn.apecode.service.impl.UserDetailsServiceImpl;
import cn.apecode.websocket.pojo.OnlineUser;
import cn.apecode.websocket.pojo.VisitorUser;
import cn.apecode.websocket.utils.VisitorUtils;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

import static cn.apecode.common.constant.RedisPrefixConst.ONLINE_USER;

/**
 * @author apecode
 * @description WebSocket的http握手拦截器，用来验证获取当前登录用户信息
 * @date 1/7/2023 AM10:25
 */
@RequiredArgsConstructor
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;
    private final RedisService redisService;


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        // 拿到jwt令牌
        String authToken = req.getServletRequest().getParameter(tokenHeader);
        if (StringUtils.isNotBlank(authToken)) {
            String username = jwtUtils.getUserNameFromToken(authToken);
            if (StringUtils.isNotBlank(username)) {
                UserDetailsDto userDetailsDto = (UserDetailsDto) userDetailsService.loadUserByUsername(username);
                if (jwtUtils.validateToken(authToken, userDetailsDto)) {
                    userDetailsDto.setToken(authToken);
                    userDetailsDto.setTokenHead(tokenHead);
                    userDetailsDto.setPassword(null);
                    // 安全上下文，将信息交给spring security管理
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetailsDto, null, userDetailsDto.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    // 保存认证用户
                    attributes.put("user", authenticationToken);
                }
            }
        } else {
            // 游客
            String ipAddress = IpUtils.getIpAddress(req.getServletRequest());
            String uid = CommonUtils.getTouristMd5ByIpAddress(ipAddress);
            VisitorUser visitorUser = null;
            // 查看redis中是否有缓存
            if (redisService.hHasKey(ONLINE_USER, uid)) {
                Object obj = redisService.hGet(ONLINE_USER, uid);
                OnlineUser onlineUser = JSON.parseObject(obj.toString(), OnlineUser.class);
                visitorUser = VisitorUser.builder()
                        .uid(onlineUser.getUid())
                        .nickname(onlineUser.getNickname())
                        .avatar(onlineUser.getAvatar())
                        .ipAddress(onlineUser.getIpAddress())
                        .ipSource(onlineUser.getIpSource())
                        .build();
            } else {
                // 没有的话就重新拿
                visitorUser = VisitorUtils.getVisitorUserInfo(req.getServletRequest());
            }
            attributes.put("user", visitorUser);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
