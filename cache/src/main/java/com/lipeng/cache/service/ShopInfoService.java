package com.lipeng.cache.service;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.cache.model.ShopInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/20 14:47
 */
@Service
@Slf4j
public class ShopInfoService implements BaseCacheService {

    @Autowired
    private CacheService cacheService;

    @Override
    public void processMessage(JSONObject obj) {
        // 提取出商品id 店铺id信息 然后通过服务获取保存到缓存
        //Long productId = obj.getLong("productId");
        Long shopId = obj.getLong("shopId");

        // 调用商品信息服务的接口
        // 直接用注释模拟：getProductInfo?productId=1，传递过去
        // 商品信息服务，一般来说就会去查询数据库，去获取productId=1的商品信息，然后返回回来

        String shopInfoJSON = "{\"id\": 1, \"name\": \"小王的手机店\", \"level\": 5, \"goodCommentRate\":0.99}";
        ShopInfo shopInfo = JSONObject.parseObject(shopInfoJSON, ShopInfo.class);
        cacheService.saveShopInfo2LocalCache(shopInfo);
        log.info("获取刚保存到本地缓存的店铺信息：" + cacheService.getShopInfoFromLocalCache(shopId));
        cacheService.saveShopInfo2ReidsCache(shopInfo);
    }

}