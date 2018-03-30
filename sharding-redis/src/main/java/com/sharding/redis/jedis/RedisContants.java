
package com.sharding.redis.jedis;

import java.nio.charset.Charset;

/**
 * Redis常量。
 */
public class RedisContants {
	
	/**
	 * Redis返回状态-OK。
	 */
	public static final String REDIS_REPLY_STATUS_OK = "OK";
	
	/**
	 * Redis错误信息模板。
	 */
	public static final String ERR_MSG_TEMPLATE = "Redis Error Occur! Message:[{0}],Server Info:[role={1},host={2},port={3}]";
	
	/**
	 * 默认字符集。
	 */
	public static final Charset CS_UTF8 = Charset.forName("UTF-8");
	
	private RedisContants(){}

}
