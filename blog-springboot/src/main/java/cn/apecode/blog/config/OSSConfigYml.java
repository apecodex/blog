package cn.apecode.blog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: oss配置属性
 * @author: apecode
 * @date: 2022-06-23 16:13
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "upload.oss")
public class OSSConfigYml {

    /**
     * oss域名
     */
    private String url;

    /**
     * 地域节点
     */
    private String endpoint;

    /**
     * 访问密钥id
     */
    private String accessKeyId;

    /**
     * 访问密钥密码
     */
    private String accessKeySecret;

    /**
     * bucket名称
     */
    private String bucketName;
}
