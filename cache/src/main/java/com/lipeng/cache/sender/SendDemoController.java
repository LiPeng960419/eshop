package com.lipeng.cache.sender;

import com.lipeng.cache.common.Constants;
import com.lipeng.cache.model.BaseCacheMessage;
import com.lipeng.cache.model.ProductInfo;
import com.lipeng.cache.model.ProductInfoChangeMsg;
import com.lipeng.cache.model.ShopInfo;
import com.lipeng.cache.model.ShopInfoChangeMsg;
import com.lipeng.cache.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/20 15:29
 */
@Slf4j
@RestController
public class SendDemoController {

    @Autowired
    private CacheMessageSender<BaseCacheMessage> messageSender;

    @Autowired
    private CacheService cacheService;

    @GetMapping("/getProductInfoFromLocalCache")
    public ProductInfo getProductInfoFromLocalCache() {
        ProductInfo productInfo = cacheService.getProductInfoFromLocalCache(1L);
        log.info("productInfo:{}", productInfo);
        return productInfo;
    }

    @GetMapping("/getShopInfoFromLocalCache")
    public ShopInfo getShopInfoFromLocalCache() {
        ShopInfo shopInfo = cacheService.getShopInfoFromLocalCache(1L);
        log.info("shopInfo:{}", shopInfo);
        return shopInfo;
    }

    @GetMapping("/sendProductInfoChangeMsg")
    public void sendProductInfoChangeMsg() {
        ProductInfoChangeMsg productInfoChangeMsg = new ProductInfoChangeMsg();
        productInfoChangeMsg.setServiceId(Constants.PRODUCT_INFO_SERVICE);
        productInfoChangeMsg.setProductId(1L);
        messageSender.sendCacheChangeMsg(productInfoChangeMsg);
    }

    @GetMapping("/sendShopInfoChangeMsg")
    public void sendShopInfoChangeMsg() {
        ShopInfoChangeMsg shopInfoChangeMsg = new ShopInfoChangeMsg();
        shopInfoChangeMsg.setServiceId(Constants.SHOP_INFO_SERVICE);
        shopInfoChangeMsg.setProductId(1L);
        shopInfoChangeMsg.setShopId(1L);
        messageSender.sendCacheChangeMsg(shopInfoChangeMsg);
    }

}