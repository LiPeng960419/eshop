package com.lipeng.inventory.thread;

import com.lipeng.inventory.request.Request;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import lombok.extern.slf4j.Slf4j;

/**
 * 执行请求的工作线程
 */
@Slf4j
public class RequestProcessorThread implements Callable<Boolean> {

    /**
     * 自己监控的内存队列
     */
    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue) {
        this.queue = queue;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            while (true) {
                Request request = queue.take();
                log.info("工作线程处理请求，商品id=" + request.getProductId());
                request.process();
            }
        } catch (Exception e) {
            log.error("error", e);
        }
        return true;
    }

}