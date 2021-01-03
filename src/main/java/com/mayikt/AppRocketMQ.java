package com.mayikt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 蚂蚁课堂创始人-余胜军QQ644064779
 * @title: AppRocketMQ
 * @description: 每特教育独创第五期互联网架构课程
 * @date 2019/12/2415:52
 */
@SpringBootApplication
@MapperScan("com.mayikt.mapper")
public class AppRocketMQ {
    public static void main(String[] args) {
        SpringApplication.run(AppRocketMQ.class);
    }
}
