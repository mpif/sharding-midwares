
package com.sharding.redis.jedis.loadbalance;

import java.util.List;
import java.util.Random;

import com.sharding.redis.jedis.loadbalance.AbstractLoadBalance;
import redis.clients.jedis.Jedis;

/**
 * 随机负载均衡器。
 */
public class RandomLoadBalance extends AbstractLoadBalance {

	@Override
	protected Jedis doSelect(List<Jedis> jedisList) {
		Random random = new Random();
		int index = random.nextInt(jedisList.size());
		return jedisList.get(index);
	}

}
