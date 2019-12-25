package com.lipeng.storm;

import com.lipeng.storm.bolt.LogParseBolt;
import com.lipeng.storm.bolt.ProductCountBolt;
import com.lipeng.storm.spout.AccessLogKafkaSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StormApplication {

//    public synchronized static void run(String ...args) {
//        SpringApplication app = new SpringApplication(StormApplication.class);
//        //我们并不需要web servlet功能，所以设置为WebApplicationType.NONE
//        app.setWebApplicationType(WebApplicationType.NONE);
//        //忽略掉banner输出
//        app.setBannerMode(Banner.Mode.OFF);
//        //忽略Spring启动信息日志
//        app.setLogStartupInfo(false);
//        app.run(args);
//    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StormApplication.class);
        //我们并不需要web servlet功能，所以设置为WebApplicationType.NONE
        app.setWebApplicationType(WebApplicationType.NONE);
        //忽略掉banner输出
        app.setBannerMode(Banner.Mode.OFF);
        //忽略Spring启动信息日志
        app.setLogStartupInfo(false);
        app.run(args);
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("AccessLogKafkaSpout", new AccessLogKafkaSpout(), 1);
        builder.setBolt("LogParseBolt", new LogParseBolt(), 2)
                .setNumTasks(2)
                .shuffleGrouping("AccessLogKafkaSpout");
        builder.setBolt("ProductCountBolt", new ProductCountBolt(), 2)
                .setNumTasks(2)
                .fieldsGrouping("LogParseBolt", new Fields("productId"));

        Config config = new Config();

        if (args != null && args.length > 0) {
            config.setNumWorkers(3);
            try {
                StormSubmitter.submitTopology(args[0], config, builder.createTopology());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("HotProductTopology", config, builder.createTopology());
//            Utils.sleep(30000);
//            cluster.shutdown();
        }
    }

}