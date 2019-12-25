package com.lipeng.storm.spout;

import com.lipeng.storm.StormApplicationServer;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kafka消费数据的spout
 */
public class AccessLogKafkaSpout extends BaseRichSpout {

    private static final long serialVersionUID = 8698470299234327074L;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLogKafkaSpout.class);

    public static ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(1000);

    private SpoutOutputCollector collector;

    public void open(Map conf, TopologyContext context,
            SpoutOutputCollector collector) {
        this.collector = collector;
        String osName = System.getProperty("os.name");
        if (!(osName.toLowerCase().contains("windows")
                || osName.toLowerCase().contains("win"))) {
            StormApplicationServer.run();
        }
    }

    public void nextTuple() {
        if (queue.size() > 0) {
            try {
                String message = queue.take();
                collector.emit(new Values(message));
                LOGGER.info("【AccessLogKafkaSpout发射出去一条日志】message=" + message);
            } catch (Exception e) {
                LOGGER.error("nextTuple method error", e);
            }
        } else {
            Utils.sleep(100);
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("message"));
    }

}