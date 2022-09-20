package com.mall.ware2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan(basePackages = "com.mall.ware2.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class MallWare2Application {

    public static void main(String[] args) {
        SpringApplication.run(MallWare2Application.class, args);
    }

}
