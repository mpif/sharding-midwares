package com.sharding.jdbc.mybatis.support;


import com.sharding.jdbc.mybatis.datasource.DataSourceHolder;

/**
 * 默认的动态取值策略
 *
 */
public class DefaultDynamicValueStrategy implements DynamicValueStrategy {

	@Override
	public Object get(String key) {
		return DataSourceHolder.getRealTableIndex();
	}
}
