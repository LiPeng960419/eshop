package com.lipeng.cache.model;

import java.io.Serializable;
import lombok.Data;

/**
 * 店铺信息
 */
@Data
public class ShopInfo implements Serializable {

    private static final long serialVersionUID = -1466305778157288252L;
    private Long id;
    private String name;
    private Integer level;
    private Double goodCommentRate;

}