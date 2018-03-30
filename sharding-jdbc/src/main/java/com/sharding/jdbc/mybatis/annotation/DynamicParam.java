package com.sharding.jdbc.mybatis.annotation;

import com.sharding.jdbc.mybatis.support.DefaultDynamicValueStrategy;
import com.sharding.jdbc.mybatis.support.DynamicValueStrategy;

/**
 * 动态参数注解
 *
 */
public @interface DynamicParam {

	// 参数的key
	String key();
	// 参数的value值
	Class<? extends DynamicValueStrategy> value() default DefaultDynamicValueStrategy.class;
}
