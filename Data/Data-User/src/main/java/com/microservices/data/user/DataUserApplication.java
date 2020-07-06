package com.microservices.data.user;

import com.microservices.common.generator.SnowflakeIdService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableFeignClients(basePackages = "com.microservices")
public class DataUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataUserApplication.class, args);
    }

    @Bean
    public SnowflakeIdService snowflakeIdService() {
        return new SnowflakeIdService();
    }

}
