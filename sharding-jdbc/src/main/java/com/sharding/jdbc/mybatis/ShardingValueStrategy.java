
package com.sharding.jdbc.mybatis;

/**
 * Sharding值的计算策略。
 * 
 * <p>这个类的作用是通过属性值(propertyValue)来计算出Sharding值。
 * 
 * <p>例如：
 *    通过userId=1234567890 的后6位来做sharding值，那么propertyValue就是1234567890，
 *    sharding值就是567890。
 */
public interface ShardingValueStrategy {

	/**
	 * 通过属性值算出sharding值。
	 * 
	 * @param propertyValue 属性值。
	 * @return
	 *      sharding值。
	 */
	Object get(Object propertyValue);
	
}
