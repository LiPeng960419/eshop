package com.lipeng.storm.kafka;

import com.lipeng.storm.spout.AccessLogKafkaSpout;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Author: lipeng 910138
 * @Date: 2019/9/23 21:16
 */
@Slf4j
@Component
public class ConsumerListener {

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.consumer.topic}")
    public void listener(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String message = (String) kafkaMessage.get();
            log.info("【AccessLogKafkaSpout中的Kafka消费者接收到一条日志】message=" + message);
            try {
                AccessLogKafkaSpout.queue.put(message);
            } catch (InterruptedException e) {
                log.error("put message to queue error", e);
            }
        }
    }

}