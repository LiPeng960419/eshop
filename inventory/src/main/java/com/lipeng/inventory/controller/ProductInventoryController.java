package com.lipeng.inventory.controller;

import com.lipeng.inventory.model.ProductInventory;
import com.lipeng.inventory.request.ProductInventoryCacheRefreshRequest;
import com.lipeng.inventory.request.ProductInventoryDBUpdateRequest;
import com.lipeng.inventory.request.Request;
import com.lipeng.inventory.service.ProductInventoryService;
import com.lipeng.inventory.service.RequestAsyncProcessService;
import com.lipeng.inventory.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ProductInventoryController {

    @Autowired
    private RequestAsyncProcessService requestAsyncProcessService;

    @Autowired
    private ProductInventoryService productInventoryService;

    /**
     * 更新商品库存
     */
    @PostMapping("/updateProductInventory")
    public Response updateProductInventory(ProductInventory productInventory) {
        log.info("接收到更新商品库存的请求，商品id=" + productInventory.getProductId()
                + ", 商品库存数量=" + productInventory.getInventoryCnt());
        Response response = null;
        try {
            Request request = new ProductInventoryDBUpdateRequest(
                    productInventory, productInventoryService);
            requestAsyncProcessService.process(request);
            response = new Response(Response.SUCCESS);
        } catch (Exception e) {
            log.error("updateProductInventory error", e);
            response = new Response(Response.FAILURE);
        }
        return response;
    }

    /**
     * 获取商品库存
     */
    @GetMapping("/getProductInventory/{productId}")
    public ProductInventory getProductInventory(@PathVariable Integer productId) {
        log.info("接收到一个商品库存的读请求，商品id=" + productId);

        ProductInventory productInventory = null;

        try {
            Request request = new ProductInventoryCacheRefreshRequest(
                    productId, productInventoryService);
            requestAsyncProcessService.process(request);

            // 将请求扔给service异步去处理以后，就需要while(true)一会儿
            // 去尝试等待前面有商品库存更新的操作，同时缓存刷新的操作，将最新的数据刷新到缓存中
            long startTime = System.currentTimeMillis();
            long endTime = 0L;
            long waitTime = 0L;

            // 等待超过200ms没有从缓存中获取到结果
            while (true) {
//				if(waitTime > 25000) {
//					break;
//				}

                // 一般公司里面，面向用户的读请求控制在200ms就可以了
                if (waitTime > 200) {
                    break;
                }

                // 尝试去redis中读取一次商品库存的缓存数据
                productInventory = productInventoryService.getProductInventoryCache(productId);

                // 如果读取到了结果，那么就返回
                if (productInventory != null) {
                    log.info(waitTime + "ms读取到了redis中的库存缓存，商品id="
                            + productInventory.getProductId() + ", 商品库存数量=" + productInventory
                            .getInventoryCnt());
                    return productInventory;
                }

                // 如果没有读取到结果，那么等待一段时间
                else {
                    Thread.sleep(20);
                    endTime = System.currentTimeMillis();
                    waitTime = endTime - startTime;
                }
            }

            // 直接尝试从数据库中读取数据
            log.info("200ms超时,正在从数据库获取数据");
            productInventory = productInventoryService.findProductInventory(productId);
            if (productInventory != null) {
                // 将缓存刷新一下
                log.info("从数据库获取数据成功:{}", productInventory);
                productInventoryService.setProductInventoryCache(productInventory);
                return productInventory;
            }
        } catch (Exception e) {
            log.error("getProductInventory error", e);
        }

        return new ProductInventory(productId, -1L);
    }

}