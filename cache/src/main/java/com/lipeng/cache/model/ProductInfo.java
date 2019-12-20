package com.lipeng.cache.model;

import java.io.Serializable;
import lombok.Data;

/**
 * 商品信息
 */
@Data
public class ProductInfo implements Serializable {

    private static final long serialVersionUID = 5118350312464214382L;
    private Long id;
    private String name;
    private Double price;
    private String pictureList;
    private String specification;
    private String service;
    private String color;
    private String size;
    private Long shopId;

}