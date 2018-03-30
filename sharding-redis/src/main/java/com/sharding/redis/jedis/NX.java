
package com.sharding.redis.jedis;

/**
 * 用于redis set命令的NX选项。
 */
public enum NX {
	
	EXIST("XX"),
	NOTEXIST("NX");
	
	private String code;
	
	private NX(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
}
