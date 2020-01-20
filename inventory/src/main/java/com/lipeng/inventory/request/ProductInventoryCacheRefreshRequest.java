package com.lipeng.inventory.request;

import com.lipeng.inventory.model.ProductInventory;
import com.lipeng.inventory.service.ProductInventoryService;
import lombok.extern.slf4j.Slf4j;

/**
 * 重新加载商品库存的缓存
 */
@Slf4j
public class ProductInventoryCacheRefreshRequest implements Request {

	/**
	 * 商品id
	 */
	private Integer productId;
	/**
	 * 商品库存Service
	 */
	private ProductInventoryService productInventoryService;
	
	public ProductInventoryCacheRefreshRequest(Integer productId,
			ProductInventoryService productInventoryService) {
		this.productId = productId;
		this.productInventoryService = productInventoryService;
	}
	
	@Override
	public void process() {
		// 从数据库中查询最新的商品库存数量
		ProductInventory productInventory = productInventoryService.findProductInventory(productId);
		log.info("已查询到商品最新的库存数量，商品id=" + productId + ", 商品库存数量=" + productInventory.getInventoryCnt());
		// 将最新的商品库存数量，刷新到redis缓存中去
		productInventoryService.setProductInventoryCache(productInventory); 
	}

	@Override
	public Integer getProductId() {
		return productId;
	}
	
}
