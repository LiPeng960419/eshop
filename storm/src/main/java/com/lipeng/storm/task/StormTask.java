package com.lipeng.storm.task;

import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/25 10:45
 */
@Component
@Slf4j
public class StormTask {

    private static final String[] SENTENCES = "lipeng,huangshaowen,wangyifen,xietian".split(",");

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    @Scheduled(cron = "0/5 * * * * ?")
    public void showNowTime() {
        String msg = SENTENCES[new Random().nextInt(SENTENCES.length)];
        kafkaTemplate.send(topic, msg);
    }

}