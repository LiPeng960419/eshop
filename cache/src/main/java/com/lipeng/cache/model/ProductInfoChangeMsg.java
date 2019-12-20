package com.lipeng.cache.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/20 15:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductInfoChangeMsg extends BaseCacheMessage {

    private Long productId;

}