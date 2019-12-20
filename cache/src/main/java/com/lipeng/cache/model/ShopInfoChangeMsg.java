package com.lipeng.cache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/20 15:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShopInfoChangeMsg extends BaseCacheMessage {

    private Long productId;

    private Long shopId;

}