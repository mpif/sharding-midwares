
package com.sharding.jdbc.mybatis;

/**
 * 表示读/写的枚举。
 */
public enum RW {

	/**
	 * 读
	 */
	READ,
	
	/**
	 * 写
	 */
	WRITE,
	
	/**
	 * 未指定
	 */
	NS;
	
}
