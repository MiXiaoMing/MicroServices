package com.microservices;

import org.coffee.falsework.core.generator.SnowflakeIdService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
public class SupportApplication {

    // TODO: 2020/3/18 需要调整为 maven库
    public static void main(String[] args) {
        SpringApplication.run(SupportApplication.class, args);
    }

    // 生成id
    @Bean
    public SnowflakeIdService snowflakeIdService() {
        return new SnowflakeIdService();
    }
}
