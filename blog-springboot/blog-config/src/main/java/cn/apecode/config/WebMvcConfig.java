package cn.apecode.config;

import cn.apecode.handler.PageableHandlerInterceptor;
import cn.apecode.handler.RequestAccessLimit;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: web mvc 配置
 * @author: apecode
 * @date: 2022-06-09 10:10
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${upload.local.staticAccessPath}")
    private String staticAccessPath;
    @Value("${upload.local.uploadFolder}")
    private String uploadFolder;

    @Bean
    public RequestAccessLimit getRequestAccessLimit() {
        return new RequestAccessLimit();
    }

    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        // 本地上传文件虚拟路径
        registry.addResourceHandler(staticAccessPath+"**").addResourceLocations("file:" + uploadFolder);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    /**
     * @description: 解决跨域问题，无需给其他请求加@CrossOrigin
     * @param registry
     * @auther apecode
     * @date 2022/6/9 10:11
    */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOriginPatterns("*")
                .allowedMethods("*");
        WebMvcConfigurer.super.addCorsMappings(registry);
    }

    /**
     * @description: 自定义拦截器
     * @param registry
     * @auther apecode
     * @date 2022/6/9 10:11
    */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 分页拦截器
        registry.addInterceptor(new PageableHandlerInterceptor());
        // 接口限流
        registry.addInterceptor(getRequestAccessLimit());
    }
}
