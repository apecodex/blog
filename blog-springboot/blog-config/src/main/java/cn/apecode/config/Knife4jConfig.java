package cn.apecode.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @description: Knife4j配置类
 * @author: apecode
 * @date: 2022-05-25 22:50
 **/
@Configuration
@EnableOpenApi
public class Knife4jConfig  {

    @Value("${swagger.version}")
    private String version;

    @Bean
    public Docket defaultApi(Environment environment) {
        boolean dev = environment.acceptsProfiles(Profiles.of("dev", "pro"));
        HashSet<String> protocols = new HashSet<>();
        protocols.add("https");
        protocols.add("http");
        return new Docket(DocumentationType.OAS_30)
                // 是否开启swagger
                .enable(dev)
                .apiInfo(apiInfo())
                // 选择哪些接口作为swagger的doc发布
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.apecode.controller"))
                .paths(PathSelectors.any())
                .build()
                // 支持的通讯协议集合
                .protocols(protocols)
                .securitySchemes(securitySchemes())
                // 授权信息全局应用
                .securityContexts(securityContexts());
    }

    /**
     *
     * @description: API 页面上半部分展示信息
     * @return {@link ApiInfo}
     * @auther apecode
     * @date 2022/5/26 0:23
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("apecode-blog接口文档")
                .description("基于swagger3的在线接口文档")
                .contact(new Contact("apecode", "https://apecode.co", "apecode@qq.com"))
                .version("Application Version: " + version + ", Spring Boot Version: " + SpringBootVersion.getVersion())
                .build();
    }

    /**
     *
     * @description: 认证的安全上下文
     * @return {@link List<SecurityContext>}
     * @auther apecode
     * @date 2022/5/26 0:22
     */
    private List<SecurityContext> securityContexts() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("Authorization", authorizationScopes)))
                // 声明作用域
                .operationSelector(operationContext -> operationContext.requestMappingPattern().matches("/.*"))
                .build());
    }

    /**
     *
     * @description:
     * @return {@link List<SecurityScheme>}
     * @auther apecode
     * @date 2022/5/26 0:22
     */
    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> securitySchemes = new ArrayList<>();
        HttpAuthenticationScheme httpAuthenticationScheme = HttpAuthenticationScheme.JWT_BEARER_BUILDER
                .name(HttpHeaders.AUTHORIZATION)
                .description("Bearer Token")
                .build();
        securitySchemes.add(httpAuthenticationScheme);
        return securitySchemes;
    }

}
