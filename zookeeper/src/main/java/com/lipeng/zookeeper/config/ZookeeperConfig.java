package com.lipeng.zookeeper.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: lipeng 910138
 * @Date: 2019/9/20 19:26
 */
@Configuration
public class ZookeeperConfig {

    @Value("${zk.url}")
    private String zkUrl;

    @Value("${zk.sessionTimeout}")
    private int sessionTimeout;

    @Value("${zk.connectTimeout}")
    private int connectTimeout;

    @Value("${zk.retryCount}")
    private int retryCount;

    @Value("${zk.baseSleepTimeMs}")
    private int baseSleepTimeMs;

    @Bean
    public CuratorFramework getCuratorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, retryCount);
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(zkUrl)
                .sessionTimeoutMs(sessionTimeout).connectionTimeoutMs(connectTimeout)
                .retryPolicy(retryPolicy).build();
        client.start();
        return client;
    }

}