package com.mayikt.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 蚂蚁课堂创始人-余胜军QQ644064779
 * @title: ProducerController
 * @description: 每特教育独创第五期互联网架构课程
 * @date 2020/1/222:04
 */
@RestController
public class ProducerController {
    @Autowired
    private ProducerService producerService;

    @RequestMapping("/sendOrder")
    public String sendOrder() {
        return producerService.saveOrder();
    }
}
