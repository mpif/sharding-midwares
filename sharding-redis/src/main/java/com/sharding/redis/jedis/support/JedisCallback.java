
package com.sharding.redis.jedis.support;

import redis.clients.jedis.Jedis;

/**
 * Jedis回调接口。
 * 
 */
public interface JedisCallback<R> {

	/**
	 * 回调方法。
	 * 
	 * @param jedis jedis实例。
	 * @return
	 *      回调返回值。
	 */
	R call(Jedis jedis);
	
}
