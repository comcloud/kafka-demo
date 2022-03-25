package com.example.kafkademo.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 计算key的hashcode%size
 * 保证同一topic下，同一key可以在一个partition中
 * 如果没有指定key，则使用0
 * @version v1.0
 * @ClassName CustomPartitioner
 * @Author rayss
 * @Datetime 2022/3/22 4:57 下午
 */
@Slf4j
public class CustomPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, byte[] keyBytes,
                         Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.availablePartitionsForTopic(topic);
        return Objects.isNull(key) ? 0 : key.hashCode() % partitionInfos.size() - 1;

    }

    @Override
    public void close() {
        log.info(this.getClass().getName() + " :: close");
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
