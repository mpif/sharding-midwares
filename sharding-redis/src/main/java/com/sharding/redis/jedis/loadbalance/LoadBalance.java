
package com.sharding.redis.jedis.loadbalance;

import java.util.List;

import redis.clients.jedis.Jedis;

/**
 * Jedis负载均衡策略。
 */
public interface LoadBalance {

	/**
	 * 从一组Jedis中选择一个。
	 * 
	 * @param jedisList 一组Jedis。
	 * @return
	 *      选出的Jedis。
	 */
	Jedis select(List<Jedis> jedisList);
	
}
