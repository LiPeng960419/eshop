package com.lipeng.cache.sender;

import com.alibaba.fastjson.JSON;
import com.lipeng.cache.model.ProductInfoChangeMsg;
import com.lipeng.cache.model.ShopInfoChangeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @Author: lipeng 910138
 * @Date: 2019/9/23 20:51
 */
@Slf4j
@Component
public class CacheMessageSender<T> {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    @Async
    public void sendCacheChangeMsg(T data) {
        String key = getKeyByData(data);
        String msg = JSON.toJSONString(data);
        log.info("CacheMessageSender key:{},msg:{}", key, msg);

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, msg);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("CacheMessageSender send msg:{} error", msg, ex);
            }

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info("CacheMessageSender send msg:{} success,result:{}", msg, result);
            }
        });
    }

    private String getKeyByData(T data) {
        if (data instanceof ProductInfoChangeMsg) {
            return String.valueOf(((ProductInfoChangeMsg) data).getProductId());
        } else if (data instanceof ShopInfoChangeMsg) {
            return String.valueOf(((ShopInfoChangeMsg) data).getShopId());
        } else {
            throw new NullPointerException("key is empty");
        }
    }

}