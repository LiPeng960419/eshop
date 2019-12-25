package com.lipeng.storm.bolt;

import com.lipeng.storm.StormApplication;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/25 10:51
 */
@Slf4j
public class TestBolt extends BaseRichBolt {

    private static final long serialVersionUID = -9133468517876671413L;

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        StormApplication.run();
    }

    @Override
    public void execute(Tuple tuple) {
        String message = tuple.getStringByField("message");
        log.info("【TestBolt接收到一条日志】message=" + message);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

}