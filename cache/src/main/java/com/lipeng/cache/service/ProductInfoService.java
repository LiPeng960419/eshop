package com.lipeng.cache.service;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.cache.model.ProductInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/20 14:47
 */
@Service
@Slf4j
public class ProductInfoService implements BaseCacheService {

    @Autowired
    private CacheService cacheService;

    /*
    这里通过fegin调用获取商品信息
     */
    @Override
    public void processMessage(JSONObject obj) {
        // 提取出商品id
        Long productId = obj.getLong("productId");

        // 调用商品信息服务的接口 然后获取返回数据
        String productInfoJSON = "{\"id\": 1, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1}";
        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
        cacheService.saveProductInfo2LocalCache(productInfo);
        log.info("获取刚保存到本地缓存的商品信息：" + cacheService.getProductInfoFromLocalCache(productId));
        cacheService.saveProductInfo2ReidsCache(productInfo);
    }

}