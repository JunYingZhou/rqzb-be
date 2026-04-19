package com.rqzb.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.rqzb.system.mapper")
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.rqzb")
public class RqzbSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RqzbSystemApplication.class, args);
    }
}
