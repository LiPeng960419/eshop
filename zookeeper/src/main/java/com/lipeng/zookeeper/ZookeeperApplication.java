package com.lipeng.zookeeper;

import com.lipeng.zookeeper.config.DistributedLockByZookeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableScheduling
public class ZookeeperApplication {

    @Value("${zk.lockPath}")
    private String lockPath;

    public static void main(String[] args) {
        SpringApplication.run(ZookeeperApplication.class, args);
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void test() {
        try {
            distributedLockByZookeeper.acquireDistributedLock(lockPath);
            System.out.println(Thread.currentThread().getName() + "acquireDistributedLock lock");
            Thread.sleep(8000);
        } catch (Exception e) {
            //
        } finally {
            distributedLockByZookeeper.releaseDistributedLock(lockPath);
            System.out.println(Thread.currentThread().getName() + "releaseDistributedLock lock");
        }
    }

    @Autowired
    private DistributedLockByZookeeper distributedLockByZookeeper;

    @GetMapping("/lock")
    public void getLock() throws Exception {
        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        distributedLockByZookeeper.acquireDistributedLock(lockPath);
                        System.out.println(Thread.currentThread().getName() + "get lock");
                        Thread.sleep(500);
                    } catch (Exception e) {
                        //
                    } finally {
                        distributedLockByZookeeper.releaseDistributedLock(lockPath);
                    }
                }
            }).start();
        }
    }

}
