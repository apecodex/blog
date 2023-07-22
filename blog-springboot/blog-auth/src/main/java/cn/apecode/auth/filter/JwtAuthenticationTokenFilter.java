package cn.apecode.auth.filter;

import cn.apecode.common.constant.CommonConst;
import cn.apecode.common.constant.RedisPrefixConst;
import cn.apecode.common.enums.StatusCodeEnum;
import cn.apecode.common.utils.ResponseCode;
import cn.apecode.common.dto.UserDetailsDto;
import cn.apecode.service.RedisService;
import cn.apecode.service.impl.UserDetailsServiceImpl;
import cn.apecode.common.utils.IpUtils;
import cn.apecode.common.utils.JwtUtils;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @description: Jwt登录授权过滤器
 * @author: apecode
 * @date: 2022-05-28 21:32
 **/
@RequiredArgsConstructor
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsServices;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        String authToken = getJwtToken(request);
        if (StringUtils.isNotBlank(authToken)) {
            authToken = authToken.trim();
            String username = jwtUtils.getUserNameFromToken(authToken);
            if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                UserDetailsDto userDetailsDto = (UserDetailsDto) userDetailsServices.loadUserByUsername(username);
                if (jwtUtils.validateToken(authToken, userDetailsDto)) {
                    userDetailsDto.setToken(authToken);
                    userDetailsDto.setTokenHead(tokenHead);
                    userDetailsDto.setPassword(null);
                    // 安全上下文
                    SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetailsDto, null, userDetailsDto.getAuthorities());
                    emptyContext.setAuthentication(usernamePasswordAuthenticationToken);
                    SecurityContextHolder.setContext(emptyContext);
                } else {
                    // 登录状态失效
                    response.setContentType(CommonConst.APPLICATION_JSON);
                    PrintWriter writer = response.getWriter();
                    writer.write(JSON.toJSONString(ResponseCode.customize(false, StatusCodeEnum.EXPIRATION)));
                    writer.flush();
                    writer.close();
                    return;
                }
            }
        }
        report(request);
        filterChain.doFilter(request, response);
    }

    /**
     * @param request
     * @description: 更新访客
     * @auther apecode
     * @date 2022/7/13 23:07
     */
    @Async
    public void report(HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        // 今日访问
        if (!redisService.sIsMember(RedisPrefixConst.DAY_VISITOR, ipAddress)) {
            redisService.incr(RedisPrefixConst.BLOG_VIEWS_COUNT, 1L);
            redisService.sAdd(RedisPrefixConst.DAY_VISITOR, ipAddress);
        }
        // 独立访问
        if (!redisService.sIsMember(RedisPrefixConst.UNIQUE_VISITOR, ipAddress)) {
            String ipSource = IpUtils.getIpSource(ipAddress);
            String province = IpUtils.cutProvince(ipSource);
            if (StringUtils.isNotBlank(province)) {
                redisService.zIncr(RedisPrefixConst.VISITOR_AREA, province, 1D);
            } else {
                redisService.zIncr(RedisPrefixConst.VISITOR_AREA, CommonConst.UNKNOWN, 1D);
            }
            redisService.sAdd(RedisPrefixConst.UNIQUE_VISITOR, ipAddress);
        }
    }

    private String getJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader(tokenHeader);
        return StringUtils.remove(authHeader, tokenHead);
    }
}
