package com.sharding.redis.jedis.support;

/**
 * 缓存未命中异常。
 */
class CacheMissException extends Exception {

    public CacheMissException(String key) {
        super("key ["+key+"] is not in cache!");
    }

}
