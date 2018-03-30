
package com.sharding.redis.jedis;

/**
 * 用于set命令的超时时间单位。
 */
public enum TimeUnit {
	
	SECOND("EX"),
	MILLISECOND("PX");

	private String unit;
	
	private TimeUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}
	
}
