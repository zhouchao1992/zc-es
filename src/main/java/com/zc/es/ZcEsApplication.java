package com.zc.es;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@MapperScan("com.zc.es.mapper")
public class ZcEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZcEsApplication.class, args);
    }





}
