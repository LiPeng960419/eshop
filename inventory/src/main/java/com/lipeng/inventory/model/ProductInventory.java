package com.lipeng.inventory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 库存数量model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductInventory {

    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 库存数量
     */
    private Long inventoryCnt;

}