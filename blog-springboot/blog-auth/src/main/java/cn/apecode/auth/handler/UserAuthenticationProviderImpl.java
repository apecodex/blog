package cn.apecode.auth.handler;

import cn.apecode.common.dto.UserDetailsDto;
import cn.apecode.service.impl.UserDetailsServiceImpl;
import cn.apecode.utils.AnjiCaptchaUtils;
import cn.apecode.common.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @description: 自定义验证方式
 * @author: apecode
 * @date: 2022-05-28 00:22
 **/
@RequiredArgsConstructor
@Component
public class UserAuthenticationProviderImpl implements AuthenticationProvider {

    private final HttpServletRequest request;
    private final UserDetailsServiceImpl userDetailsServices;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    private final AnjiCaptchaUtils anjiCaptchaUtils;

    /**
     * @param authentication
     * @return {@link Authentication}
     * @description: 登录逻辑及生成Jwt
     * @auther apecode
     * @date 2022/5/28 18:52
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        String captchaVerification = request.getParameter("captchaVerification");
        // 验证滑动验证码
        anjiCaptchaUtils.checkCaptcha(captchaVerification);
        UserDetailsDto userDetailsDto = (UserDetailsDto) userDetailsServices.loadUserByUsername(username);
        if (Objects.isNull(userDetailsDto) || !passwordEncoder.matches(password, userDetailsDto.getPassword())) {
            throw new BadCredentialsException("用户名或密码错误");
        }
        if (!userDetailsDto.isEnabled()) {
            throw new DisabledException("账号被禁用");
        }
        // 生成Token
        String token = jwtUtils.generateToken(userDetailsDto);
        userDetailsDto.setToken(token);
        userDetailsDto.setTokenHead(tokenHead);
        userDetailsDto.setPassword(null);
        // 安全上下文
        return new UsernamePasswordAuthenticationToken(userDetailsDto, null, userDetailsDto.getAuthorities());
    }

    /**
     * @param authentication
     * @return {@link boolean}
     * @description: 如果该AuthenticationProvider支持传入的Authentication对象，则返回true
     * @auther apecode
     * @date 2022/5/28 18:52
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
