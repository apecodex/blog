package cn.apecode.blog.config;

import cn.apecode.blog.filter.JwtAuthenticationTokenFilter;
import cn.apecode.blog.handler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
@Configuration
public class SecurityConfig {

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;
    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;
    @Autowired
    private AuthenticationFailHandlerImpl authenticationFailHandler;
    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;
    // 接口拦截规则
    @Autowired
    private FilterInvocationSecurityMetadataSourceImpl filterInvocationSecurityMetadataSource;
    // 访问决策管理器
    @Autowired
    private AccessDecisionManagerImpl accessDecisionManager;
    // Jwt登录授权过滤器
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

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
        // 不需要csrf
        http.csrf().disable().exceptionHandling();
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
