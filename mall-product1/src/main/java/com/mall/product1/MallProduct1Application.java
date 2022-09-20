package com.mall.product1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan("com.mall.product1.dao")//让mybatis能扫描到所有mapper接口
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.mall.product1.feign")
public class MallProduct1Application {

    public static void main(String[] args) {
        SpringApplication.run(MallProduct1Application.class, args);
    }

}
