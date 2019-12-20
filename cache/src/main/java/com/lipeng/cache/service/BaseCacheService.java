package com.lipeng.cache.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/20 14:46
 */
public interface BaseCacheService {

    void processMessage(JSONObject obj);

}