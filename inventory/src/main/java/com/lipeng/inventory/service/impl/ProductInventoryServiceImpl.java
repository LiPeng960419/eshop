package com.lipeng.inventory.service.impl;

import com.lipeng.inventory.mapper.ProductInventoryMapper;
import com.lipeng.inventory.model.ProductInventory;
import com.lipeng.inventory.service.ProductInventoryService;
import com.lipeng.inventory.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品库存Service实现类
 */
@Service
@Slf4j
public class ProductInventoryServiceImpl implements ProductInventoryService {

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventoryMapper.updateProductInventory(productInventory);
        log.info("已修改数据库中的库存，商品id=" + productInventory.getProductId()
                + ", 商品库存数量=" + productInventory.getInventoryCnt());
    }

    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisUtil.delKey(key);
        log.info("已删除redis中的缓存，key=" + key);
    }

    /**
     * 根据商品id查询商品库存
     *
     * @param productId 商品id
     * @return 商品库存
     */
    public ProductInventory findProductInventory(Integer productId) {
        return productInventoryMapper.findProductInventory(productId);
    }

    /**
     * 设置商品库存的缓存
     *
     * @param productInventory 商品库存
     */
    public void setProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:" + productInventory.getProductId();
        redisUtil.setString(key, String.valueOf(productInventory.getInventoryCnt()));
        log.info("已更新商品库存的缓存，商品id=" + productInventory.getProductId()
                + ", 商品库存数量=" + productInventory.getInventoryCnt() + ", key=" + key);
    }

    /**
     * 获取商品库存的缓存
     */
    public ProductInventory getProductInventoryCache(Integer productId) {
        Long inventoryCnt = 0L;
        String key = "product:inventory:" + productId;
        String result = redisUtil.getString(key);
        if (StringUtils.isNotEmpty(result)) {
            try {
                inventoryCnt = Long.valueOf(result);
                return new ProductInventory(productId, inventoryCnt);
            } catch (Exception e) {
                log.error("getProductInventoryCache error", e);
            }
        }
        return null;
    }

}