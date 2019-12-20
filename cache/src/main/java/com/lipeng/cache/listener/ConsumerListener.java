package com.lipeng.cache.listener;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.cache.service.BaseCacheService;
import com.lipeng.cache.utils.SpringContextUtil;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

/**
 * @Author: lipeng 910138
 * @Date: 2019/9/23 21:16
 */
@Slf4j
@Component
public class ConsumerListener {

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.consumer.topic}")
    public void listener(ConsumerRecord<?, ?> record, @Headers MessageHeaders headers) {
        log.info("ConsumerListener接收到消息:" + record.toString());
        //headers.keySet().forEach(key -> log.info("MessageHeaders:{}:{}", key, headers.get(key)));
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            String message = (String) kafkaMessage.get();
            // 首先将message转换成json对象
            JSONObject messageJSONObject = JSONObject.parseObject(message);
            // 从这里提取出消息对应的服务的标识
            String serviceId = messageJSONObject.getString("serviceId");

            BaseCacheService service = (BaseCacheService) SpringContextUtil.getBean(serviceId);
            if (Objects.nonNull(service)) {
                service.processMessage(messageJSONObject);
            }
        }
    }

}