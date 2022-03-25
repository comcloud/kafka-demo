package com.example.kafkademo.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 消息接收
 *
 * @author rayss
 */
@Component
@Slf4j
public class KafkaConsumer {

    /**
     * containerFactory:定义批处理器，批处理消费的线程数由kafka.listener.concurrencys控制
     * topics：消费的消息队列的topic
     *
     * @param records
     * @param ack
     */
    @KafkaListener(containerFactory = "kafkaBatchListener", topics = {"first"})
    public void batchListener1(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {

        try {
            records.forEach(record -> {
                log.info("receive {} msg:{}", record.topic(), record.value().toString());

            });
        } catch (Exception e) {
            log.error("kafka listen error:{}", e.getMessage());

        } finally {
            //手动提交偏移量
            ack.acknowledge();
        }

    }

    /**
     * containerFactory:定义批处理器，批处理消费的线程数由kafka.listener.concurrencys控制
     * topics：消费的消息队列的topic
     * 订阅另一个topic
     *
     * @param records
     * @param ack
     */
    @KafkaListener(containerFactory = "kafkaBatchListener", topics = {"second"})
    public void batchListener2(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {

        try {
            records.forEach(record -> {
                //TODO - 处理消息
                log.info("receive {} msg:{}", record.topic(), record.value().toString());

            });
        } catch (Exception e) {
            //TODO - 消息处理异常操作
            log.error("kafka listen error:{}", e.getMessage());

        } finally {
            //手动提交偏移量
            ack.acknowledge();
        }

    }


    /**
     * 动态设置监控分区
     *
     * @KafkaListener(topicPartitions = @TopicPartition(topic = "three",
     * partitions = "#{@finder.partitions('three')}",
     * partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0"))
     * )
     */
    @KafkaListener(containerFactory = "kafkaBatchListener", topics = {"three"})
    public void batchListener3(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
//@表示取bean
        try {
            records.forEach(record -> {
                //TODO - 处理消息
                log.info("receive {} msg:{}", record.topic(), record.value().toString());

            });
        } catch (Exception e) {
            //TODO - 消息处理异常操作
            log.error("kafka listen error:{}", e.getMessage());

        } finally {
            //手动提交偏移量
            ack.acknowledge();
        }

    }

    @Bean
    public PartitionFinder finder(ConsumerFactory<String, String> consumerFactory) {
        return new PartitionFinder(consumerFactory);
    }

    public static class PartitionFinder {

        private final ConsumerFactory<String, String> consumerFactory;

        public PartitionFinder(ConsumerFactory<String, String> consumerFactory) {
            this.consumerFactory = consumerFactory;
        }

        public String[] partitions(String topic) {
            try (Consumer<String, String> consumer = consumerFactory.createConsumer()) {
                return consumer.partitionsFor(topic).stream()
                        .map(pi -> "" + pi.partition())
                        .toArray(String[]::new);
            }
        }

    }
}
