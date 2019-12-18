package com.lipeng.inventory.request;

/**
 * 请求接口
 */
public interface Request {
	
	void process();

	Integer getProductId();
	
}