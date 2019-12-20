package com.lipeng.cache.service;


import com.lipeng.cache.model.ProductInfo;
import com.lipeng.cache.model.ShopInfo;

/**
 * 缓存service接口
 */
public interface CacheService {

    /**
     * 将商品信息保存到本地的ehcache缓存中
     */
    public ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo);

    /**
     * 从本地ehcache缓存中获取商品信息
     */
    public ProductInfo getProductInfoFromLocalCache(Long productId);

    /**
     * 将店铺信息保存到本地的ehcache缓存中
     */
    public ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo);

    /**
     * 从本地ehcache缓存中获取店铺信息
     */
    public ShopInfo getShopInfoFromLocalCache(Long shopId);

    /**
     * 将商品信息保存到redis中
     */
    public void saveProductInfo2ReidsCache(ProductInfo productInfo);

    /**
     * 将店铺信息保存到redis中
     */
    public void saveShopInfo2ReidsCache(ShopInfo shopInfo);

}