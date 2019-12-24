//package com.lipeng.storm.zk;
//
//import com.alibaba.fastjson.JSON;
//import java.util.concurrent.CountDownLatch;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.zookeeper.CreateMode;
//import org.apache.zookeeper.WatchedEvent;
//import org.apache.zookeeper.Watcher;
//import org.apache.zookeeper.Watcher.Event.KeeperState;
//import org.apache.zookeeper.ZooDefs.Ids;
//import org.apache.zookeeper.ZooKeeper;
//import org.apache.zookeeper.data.Stat;
//import org.springframework.stereotype.Component;
//
///**
// * ZooKeeperSession
// */
//@Slf4j
//@Component
//public class ZooKeeperSession {
//
//    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
//
//    private ZooKeeper zookeeper;
//
//    public ZooKeeperSession() {
//        // 去连接zookeeper server，创建会话的时候，是异步去进行的
//        // 所以要给一个监听器，说告诉我们什么时候才是真正完成了跟zk server的连接
//        try {
//            this.zookeeper = new ZooKeeper(
//                    "192.168.31.187:2181,192.168.31.19:2181,192.168.31.227:2181",
//                    50000,
//                    new ZooKeeperWatcher());
//            // 给一个状态CONNECTING，连接中
//            log.info(JSON.toJSONString(zookeeper.getState()));
//            connectedSemaphore.await();
//            log.info("ZooKeeper session established......");
//        } catch (Exception e) {
//            log.error("ZooKeeperSession error", e);
//        }
//    }
//
//    /**
//     * 获取单例
//     */
//    public static ZooKeeperSession getInstance() {
//        return Singleton.getInstance();
//    }
//
//    /**
//     * 初始化单例的便捷方法
//     */
//    public static void init() {
//        getInstance();
//    }
//
//    /**
//     * 获取分布式锁
//     */
//    public void acquireDistributedLock() {
//        String path = "/taskid-list-lock";
//
//        try {
//            zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//            log.info("success to acquire lock for taskid-list-lock");
//        } catch (Exception e) {
//            // 如果那个商品对应的锁的node，已经存在了，就是已经被别人加锁了，那么就这里就会报错
//            // NodeExistsException
//            int count = 0;
//            while (true) {
//                try {
//                    Thread.sleep(20);
//                    zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//                } catch (Exception e2) {
//                    count++;
//                    log.info("the " + count + " times try to acquire lock for taskid-list-lock......");
//                    continue;
//                }
//                log.info("success to acquire lock for taskid-list-lock after " + count + " times try......");
//                break;
//            }
//        }
//    }
//
//    /**
//     * 释放掉一个分布式锁
//     */
//    public void releaseDistributedLock() {
//        String path = "/taskid-list-lock";
//        try {
//            zookeeper.delete(path, -1);
//            log.info("release the lock for taskid-list-lock......");
//        } catch (Exception e) {
//            log.error("releaseDistributedLock error", e);
//        }
//    }
//
//    public String getNodeData() {
//        try {
//            return new String(zookeeper.getData("/taskid-list", false, new Stat()));
//        } catch (Exception e) {
//            log.error("getNodeData error", e);
//        }
//        return "";
//    }
//
//    public void setNodeData(String path, String data) {
//        try {
//            zookeeper.setData(path, data.getBytes(), -1);
//        } catch (Exception e) {
//            log.error("setNodeData error", e);
//        }
//    }
//
//    public void createNode(String path) {
//        try {
//            zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//        } catch (Exception e) {
//            log.error("createNode error", e);
//        }
//    }
//
//    /**
//     * 封装单例的静态内部类
//     */
//    private static class Singleton {
//
//        private static ZooKeeperSession instance;
//
//        static {
//            instance = new ZooKeeperSession();
//        }
//
//        public static ZooKeeperSession getInstance() {
//            return instance;
//        }
//
//    }
//
//    /**
//     * 建立zk session的watcher
//     */
//    private class ZooKeeperWatcher implements Watcher {
//
//        public void process(WatchedEvent event) {
//            log.info("Receive watched event: " + event.getState());
//            if (KeeperState.SyncConnected == event.getState()) {
//                connectedSemaphore.countDown();
//            }
//        }
//
//    }
//
//}