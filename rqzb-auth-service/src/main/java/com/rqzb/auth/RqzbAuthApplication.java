package com.rqzb.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.rqzb.auth.mapper")
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.rqzb")
public class RqzbAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(RqzbAuthApplication.class, args);
    }
}
