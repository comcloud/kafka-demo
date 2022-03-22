package com.example.kafkademo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * 自定义分区器，发送的message中如果存在demo，那么就发往0号分区，否则发往1号分区
 *
 * @version v1.0
 * @ClassName CustomPartitioner
 * @Author rayss
 * @Datetime 2022/3/22 4:57 下午
 */
@Slf4j
public class CustomPartitioner implements Partitioner {


    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        String msgValue = value.toString();
        return msgValue.contains("demo") ? 0 : 1;
    }

    @Override
    public void close() {
        log.info(this.getClass().getName() + " :: close");
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
