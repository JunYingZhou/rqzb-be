package com.rqzb.userinfo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.rqzb.userinfo.mapper")
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.rqzb")
public class RqzbUserInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RqzbUserInfoApplication.class, args);
    }
}
