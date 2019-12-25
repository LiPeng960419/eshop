package com.lipeng.storm;

import com.lipeng.storm.bolt.TestBolt;
import com.lipeng.storm.spout.AccessLogKafkaSpout;
import com.lipeng.storm.utils.GetSpringBean;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class StormApplication {

    /*
    本地运行
     */
    public static void main(String[] args) {
        StormApplication.runSpring();
        StormApplication.runStorm();
    }

    public synchronized static void runSpring() {
        runSpring(new String[0]);
    }

    public synchronized static void runSpring(String... args) {
        if (Objects.nonNull(GetSpringBean.getApplicationContext())) {
            return;
        }
        run(args);
    }

    public synchronized static void run(String[] args) {
        SpringApplication.run(StormApplication.class, args);
    }

    public synchronized static void runStorm() {
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("AccessLogKafkaSpout", new AccessLogKafkaSpout(), 1);
        builder.setBolt("TestBolt", new TestBolt(), 2)
                .setNumTasks(2)
                .shuffleGrouping("AccessLogKafkaSpout");
//        builder.setBolt("LogParseBolt", new LogParseBolt(), 2)
//                .setNumTasks(2)
//                .shuffleGrouping("AccessLogKafkaSpout");
//        builder.setBolt("ProductCountBolt", new ProductCountBolt(), 2)
//                .setNumTasks(2)
//                .fieldsGrouping("LogParseBolt", new Fields("productId"));

        Config config = new Config();
        try {
            String osName = System.getProperty("os.name");
            if (osName.toLowerCase().contains("windows")
                    || osName.toLowerCase().contains("win")) {
                LocalCluster cluster = new LocalCluster();
                cluster.submitTopology("StormTopology", config, builder.createTopology());
                log.info("运行本地模式");
            } else {
                config.setNumWorkers(3);
                try {
                    StormSubmitter.submitTopology("StormTopology", config, builder.createTopology());
                    log.info("运行远程模式");
                } catch (Exception e) {
                    log.error("storm运行远程模式启动失败!", e);
                }
            }
        } catch (Exception e) {
            log.error("storm启动失败!", e);
        }
    }

}