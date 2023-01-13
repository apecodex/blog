package cn.apecode.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication(scanBasePackages = {"cn.apecode"})
public class BlogSpringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogSpringbootApplication.class, args);
    }

}
