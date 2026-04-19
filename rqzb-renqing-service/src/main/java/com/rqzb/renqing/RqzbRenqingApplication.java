package com.rqzb.renqing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.rqzb.renqing.mapper")
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.rqzb")
public class RqzbRenqingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RqzbRenqingApplication.class, args);
    }
}
