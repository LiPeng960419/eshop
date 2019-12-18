package com.lipeng.inventory.thread;

import com.lipeng.inventory.request.Request;
import com.lipeng.inventory.request.RequestQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

/**
 * 请求处理线程池：单例
 */
public class RequestProcessorThreadPool {

    /**
     * 默认最大并发数<br>
     */
    private static final int THREAD_COUNT = 10;

    /**
     * 线程池名称格式
     */
    private static final String THREAD_POOL_NAME = "RequestProcessorThreadPool-%d";

    /**
     * 线程工厂名称
     */
    private static final ThreadFactory FACTORY = new BasicThreadFactory.Builder()
            .namingPattern(THREAD_POOL_NAME)
            .daemon(true).build();

    /**
     * 默认队列大小
     */
    private static final int DEFAULT_SIZE = 100;

    /**
     * 默认线程存活时间
     */
    private static final long DEFAULT_KEEP_ALIVE = 60L;

    /*
     * request队列大小
     */
    private static final int REQUEST_DEFAULT_SIZE = 100;

    /**
     * 执行队列
     */
    private static BlockingQueue<Runnable> executeQueue = new ArrayBlockingQueue<>(DEFAULT_SIZE);

    /**
     * 线程池
     */
    private static ExecutorService threadPool;

    static {
        threadPool = new ThreadPoolExecutor(THREAD_COUNT, THREAD_COUNT, DEFAULT_KEEP_ALIVE,
                TimeUnit.SECONDS, executeQueue, FACTORY);
    }

    public RequestProcessorThreadPool() {
        RequestQueue requestQueue = RequestQueue.getInstance();

        for (int i = 0; i < THREAD_COUNT; i++) {
            ArrayBlockingQueue<Request> queue = new ArrayBlockingQueue<Request>(REQUEST_DEFAULT_SIZE);
            requestQueue.addQueue(queue);
            threadPool.submit(new RequestProcessorThread(queue));
        }
    }

    public static RequestProcessorThreadPool getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 初始化的便捷方法
     */
    public static void init() {
        getInstance();
    }

    /**
     * 静态内部类的方式，去初始化单例
     */
    private static class Singleton {

        private static RequestProcessorThreadPool instance;

        static {
            instance = new RequestProcessorThreadPool();
        }

        public static RequestProcessorThreadPool getInstance() {
            return instance;
        }

    }

}