package cn.apecode.blog.handler;

import cn.apecode.blog.dto.UserDetailsDto;
import cn.apecode.blog.service.impl.UserDetailsServicesImpl;
import cn.apecode.blog.utils.AnjiCaptchaUtils;
import cn.apecode.blog.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
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
@Component
public class UserAuthenticationProviderImpl implements AuthenticationProvider {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserDetailsServicesImpl userDetailsServices;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private AnjiCaptchaUtils anjiCaptchaUtils;

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
