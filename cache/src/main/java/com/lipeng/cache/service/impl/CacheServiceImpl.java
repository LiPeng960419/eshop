package com.lipeng.cache.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lipeng.cache.model.ProductInfo;
import com.lipeng.cache.model.ShopInfo;
import com.lipeng.cache.service.CacheService;
import com.lipeng.cache.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 缓存Service实现类
 *
 * @author Administrator
 */
@Service
public class CacheServiceImpl implements CacheService {

    public static final String CACHE_NAME = "local";

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 将商品信息保存到本地的ehcache缓存中
     */
    @CachePut(value = CACHE_NAME, key = "'product_info_'+#productInfo.getId()")
    public ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    /**
     * 从本地ehcache缓存中获取商品信息
     */
    @Cacheable(value = CACHE_NAME, key = "'product_info_'+#productId")
    public ProductInfo getProductInfoFromLocalCache(Long productId) {
        return null;
    }

    /**
     * 将店铺信息保存到本地的ehcache缓存中
     */
    @CachePut(value = CACHE_NAME, key = "'shop_info_'+#shopInfo.getId()")
    public ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo) {
        return shopInfo;
    }

    /**
     * 从本地ehcache缓存中获取店铺信息
     */
    @Cacheable(value = CACHE_NAME, key = "'shop_info_'+#shopId")
    public ShopInfo getShopInfoFromLocalCache(Long shopId) {
        return null;
    }

    /**
     * 将商品信息保存到redis中
     */
    public void saveProductInfo2ReidsCache(ProductInfo productInfo) {
        String key = "product_info_" + productInfo.getId();
        redisUtil.setString(key, JSONObject.toJSONString(productInfo));
    }

    /**
     * 将店铺信息保存到redis中
     */
    public void saveShopInfo2ReidsCache(ShopInfo shopInfo) {
        String key = "shop_info_" + shopInfo.getId();
        redisUtil.setString(key, JSONObject.toJSONString(shopInfo));
    }

}