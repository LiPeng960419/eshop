package com.lipeng.storm.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/24 20:57
 */
@Component
@Slf4j
public class ZKUtils {

    @Autowired
    private CuratorFramework zkClient;

    public String getNodeData(String path) {
        try {
            byte[] bytes = zkClient.getData().forPath(path);
            return new String(bytes);
        } catch (Exception e) {
            log.error("getNodeData error,path:{}", path, e);
        }
        return null;
    }

    public void setNodeData(String path, String data) {
        try {
            zkClient.setData().forPath(path, data.getBytes());
        } catch (Exception e) {
            log.error("setNodeData error,path:{},data:{}", path, data, e);
        }
    }

    public boolean crateNode(String path, String data, CreateMode createMode) {
        try {
            zkClient.create().withMode(createMode).forPath(path, data.getBytes());
        } catch (Exception e) {
            log.error("crateNode error,path:{},data:{}", path, data, e);
            return false;
        }
        return true;
    }

}