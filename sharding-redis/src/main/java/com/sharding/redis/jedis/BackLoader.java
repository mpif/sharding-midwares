package com.sharding.redis.jedis;

/**
 * 回源加载器。
 *
 */
public interface BackLoader<R> {

    /**
     * 从底层数据源中加载数据。
     *
     * @param key 数据的key。
     * @param cause 引起加载的原因。
     * @return
     *      加载后的数据。
     */
    R load(String key, Throwable cause);

}
