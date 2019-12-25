package com.lipeng.storm;

import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
@Import(com.lipeng.storm.utils.GetSpringBean.class)
public class StormApplicationServer {

    private static AtomicBoolean flag = new AtomicBoolean(false);

    public synchronized static void run(String... args) {
        if (flag.compareAndSet(false, true)) {
            SpringApplication springApplication = new SpringApplication(StormApplicationServer.class);
            springApplication.run(args);
        }
    }

}