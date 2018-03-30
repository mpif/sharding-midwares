
package com.sharding.jdbc.mybatis.support;

import com.sharding.jdbc.mybatis.ShardingValueStrategy;

/**
 * ShardingValueStrategy的简单实现。
 */
public class SimpleShardingValueStrategy implements ShardingValueStrategy {

	public Object get(Object propertyValue) {
		return propertyValue;
	}

}
