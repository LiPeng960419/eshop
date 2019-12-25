package com.lipeng.storm;

import com.lipeng.storm.bolt.TestBolt;
import com.lipeng.storm.spout.AccessLogKafkaSpout;
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

    public static void main(String[] args) {
        SpringApplication.run(StormApplication.class, args);
        runStorm(args);
    }

    public static void runStorm(String[] args) {
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
            if (args != null && args.length > 0) {
                config.setNumWorkers(3);
                try {
                    StormSubmitter.submitTopology(args[0], config, builder.createTopology());
                    log.info("运行远程模式");
                } catch (Exception e) {
                    log.error("storm运行远程模式启动失败!", e);
                }
            } else {
                LocalCluster cluster = new LocalCluster();
                cluster.submitTopology("HotProductTopology", config, builder.createTopology());
                log.info("运行本地模式");
            }
            log.info("storm启动成功...");
        } catch (Exception e) {
            log.error("storm启动失败!", e);
        }
    }

}