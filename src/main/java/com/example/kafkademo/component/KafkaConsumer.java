package com.example.kafkademo.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息接收
 * @author shawn yang
 * @version [v1.0]
 * @Description
 * @CreateDate 2020/1/21
 */
@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(containerFactory = "kafkaBatchListener",topics = {"hello"})
    public void batchListener(List<ConsumerRecord<?,?>> records, Acknowledgment ack){

        try {
            records.forEach(record -> {

                //处理消息
                log.info("receive msg:{}",record.value().toString());

            });
        } catch (Exception e) {
            log.error("kafka listen error:{}",e.getMessage());
        } finally {
            ack.acknowledge();//手动提交偏移量
        }

    }


}
