package cn.apecode.config;

import cn.apecode.auth.filter.JwtAuthenticationTokenFilter;
import cn.apecode.auth.handler.*;
import cn.apecode.handler.FilterInvocationSecurityMetadataSourceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

/**
 * @description: 安全配置类
 * @author: apecode
 * @date: 2022-05-26 23:07
 **/
// 激活WebSecurityConfiguration配置类
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final AccessDeniedHandlerImpl accessDeniedHandler;
    private final AuthenticationFailHandlerImpl authenticationFailHandler;
    private final AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    private final LogoutSuccessHandlerImpl logoutSuccessHandler;
    // 接口拦截规则
    private final FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;
    // 访问决策管理器
    private final AccessDecisionManagerImpl accessDecisionManager;
    // Jwt登录授权过滤器
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    /**
     *
     * @description: 用户身份验证
     *
     * @return {@link SecurityFilterChain}
     * @auther apecode
     * @date 2022/5/26 23:37
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 配置security请求接口允许跨域，不然登录会有跨域问题
        http.cors(Customizer.withDefaults()).csrf().disable();
        // 禁用缓存
        http.headers().cacheControl().disable();
        // 基于Token,不需要Session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 配置登录注销路径
        http.formLogin()
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailHandler)
                .and().logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 配置路由权限信息
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(filterInvocationSecurityMetadataSource);  // 接口拦截规则
                        object.setAccessDecisionManager(accessDecisionManager);  // 访问决策管理器
                        return object;
                    }
                })
                .anyRequest().permitAll();
        // Jwt登录授权过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 未授权和未登录
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     *
     * @description: 加密方式
     *
     * @return {@link PasswordEncoder}
     * @auther apecode
     * @date 2022/5/26 23:40
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * @description: 发送客户端请求
     * @param builder
     * @return {@link RestTemplate}
     * @auther apecode
     * @date 2023/1/5 14:05
    */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}
