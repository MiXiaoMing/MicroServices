package com.microservices.testdata;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 跨域请求处理
 */
@Configuration
public class JSONPConfigurer extends WebMvcConfigurerAdapter{

    /**
     * mapping:限制请求路径
     * origins:限制接收的请求地址
     * methods:限制请求方式
     * credentials:限制是否发送cookie
     * maxAge:请求有效期
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST")
                .allowCredentials(false).maxAge(3600);
    }
}
