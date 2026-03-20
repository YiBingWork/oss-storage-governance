package com.yibing.oss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yibing.oss.mapper")
public class OssStorageGovernanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OssStorageGovernanceApplication.class, args);
    }
}
