package cn.apecode.blog.filter;

import cn.apecode.blog.dto.UserDetailsDto;
import cn.apecode.blog.service.RedisService;
import cn.apecode.blog.service.impl.UserDetailsServicesImpl;
import cn.apecode.blog.utils.IpUtils;
import cn.apecode.blog.utils.JwtUtils;
import cn.apecode.blog.vo.ResponseCode;
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

import static cn.apecode.blog.constant.CommonConst.APPLICATION_JSON;
import static cn.apecode.blog.constant.RedisPrefixConst.DAY_VISITOR;
import static cn.apecode.blog.constant.RedisPrefixConst.UNIQUE_VISITOR;
import static cn.apecode.blog.constant.RedisPrefixConst.VISITOR_AREA;
import static cn.apecode.blog.constant.RedisPrefixConst.BLOG_VIEWS_COUNT;
import static cn.apecode.blog.constant.CommonConst.UNKNOWN;
import static cn.apecode.blog.enums.StatusCodeEnum.EXPIRATION;

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
    private final UserDetailsServicesImpl userDetailsServices;
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
                    response.setContentType(APPLICATION_JSON);
                    PrintWriter writer = response.getWriter();
                    writer.write(JSON.toJSONString(ResponseCode.customize(false, EXPIRATION)));
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
        if (!redisService.sIsMember(DAY_VISITOR, ipAddress)) {
            redisService.incr(BLOG_VIEWS_COUNT, 1L);
            redisService.sAdd(DAY_VISITOR, ipAddress);
        }
        // 独立访问
        if (!redisService.sIsMember(UNIQUE_VISITOR, ipAddress)) {
            String ipSource = IpUtils.getIpSource(ipAddress);
            String province = IpUtils.cutProvince(ipSource);
            if (StringUtils.isNotBlank(province)) {
                redisService.zIncr(VISITOR_AREA, province, 1D);
            } else {
                redisService.zIncr(VISITOR_AREA, UNKNOWN, 1D);
            }
            redisService.sAdd(UNIQUE_VISITOR, ipAddress);
        }
    }

    private String getJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader(tokenHeader);
        return StringUtils.remove(authHeader, tokenHead);
    }
}
