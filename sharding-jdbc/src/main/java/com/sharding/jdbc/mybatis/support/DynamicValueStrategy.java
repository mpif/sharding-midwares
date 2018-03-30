package com.sharding.jdbc.mybatis.support;

/**
 * 动态取值策略
 *
 */
public interface DynamicValueStrategy {

	public Object get(String key);
}
