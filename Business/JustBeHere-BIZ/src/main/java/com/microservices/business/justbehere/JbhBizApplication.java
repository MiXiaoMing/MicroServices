package com.microservices.business.justbehere;

import com.microservices.common.generator.SnowflakeIdService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients(basePackages = "com.microservices")
public class JbhBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(JbhBizApplication.class, args);
    }

    @Bean
    public SnowflakeIdService snowflakeIdService() {
        return new SnowflakeIdService();
    }

}
