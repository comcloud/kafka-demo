package com.example.kafkademo.controller;

import com.example.kafkademo.component.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shawn yang
 * @version [v1.0]
 * @Description
 * @CreateDate 2020/1/21
 */
@RestController
public class KafkaController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @PostMapping("send")
    public String send(String msg,String topic){

        Assert.notNull(msg,"消息内容不能为空");

        kafkaProducer.sendMessage(topic,msg);

        return "success";
    }
}
