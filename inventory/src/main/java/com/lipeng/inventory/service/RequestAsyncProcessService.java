package com.lipeng.inventory.service;


import com.lipeng.inventory.request.Request;

/**
 * 请求异步执行的service
 */
public interface RequestAsyncProcessService {

    void process(Request request);

}