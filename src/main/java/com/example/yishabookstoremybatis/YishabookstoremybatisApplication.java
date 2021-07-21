package com.example.yishabookstoremybatis;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@MapperScan(basePackages = "com.example.yishabookstoremybatis.mapper")
public class YishabookstoremybatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(YishabookstoremybatisApplication.class, args);
    }

}
