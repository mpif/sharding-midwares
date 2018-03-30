package com.sharding.redis.jedis;

import java.lang.reflect.Type;

/**
 * 回源客户端。
 * <p>基于RedisClient，在读取缓存失败或者缓存未命中时，从底层数据源加载数据。
 *
 */
public interface LoadingBackClient {

    /**
     * 获取目标键对应的值，在读取缓存失败或者缓存未命中时，从底层数据源中加载。
     *
     * @param key 键。
     * @param backLoader 回源加载器。
     * @param expireSecond 从底层数据源加载到缓存后的缓存失效时间。
     * @return
     *      键对应的值。
     */
    String get(String key, BackLoader<String> backLoader, int expireSecond);

    /**
     * 获取目标键对应的值，在读取缓存失败或者缓存未命中时，从底层数据源中加载。
     *
     * @param key 键。
     * @param backLoader 回源加载器。
     * @param expireSecond 从底层数据源加载到缓存后的缓存失效时间。
     * @return
     *      键对应的值。
     */
    Long getLong(String key, BackLoader<Long> backLoader, int expireSecond);

    /**
     * 获取目标键对应的值，在读取缓存失败或者缓存未命中时，从底层数据源中加载。
     *
     * @param key 键。
     * @param backLoader 回源加载器。
     * @param expireSecond 从底层数据源加载到缓存后的缓存失效时间。
     * @return
     *      键对应的值。
     */
    Integer getInt(String key, BackLoader<Integer> backLoader, int expireSecond);

    /**
     * 获取目标键对应的值，在读取缓存失败或者缓存未命中时，从底层数据源中加载。
     *
     * @param key 键。
     * @param backLoader 回源加载器。
     * @param expireSecond 从底层数据源加载到缓存后的缓存失效时间。
     * @return
     *      键对应的值。
     */
    Double getDouble(String key, BackLoader<Double> backLoader, int expireSecond);

    /**
     * 获取目标键对应的值，在读取缓存失败或者缓存未命中时，从底层数据源中加载。
     * <p>缓存中保存的Json。
     *
     * @param key 键。
     * @param backLoader 回源加载器。
     * @param objectClass 值的类型。
     * @param expireSecond 从底层数据源加载到缓存后的缓存失效时间。
     * @return
     *      键对应的值。
     */
    <O> O getFromJson(String key, BackLoader<O> backLoader, Class<O> objectClass, int expireSecond);

    /**
     * 获取目标键对应的值，在读取缓存失败或者缓存未命中时，从底层数据源中加载。
     * <p>缓存中保存的Json。
     *
     * @param key 键。
     * @param backLoader 回源加载器。
     * @param typeOfT 值的类型信息。
     * @param expireSecond 从底层数据源加载到缓存后的缓存失效时间。
     * @return
     *      键对应的值。
     */
    <T> T getFromJson(String key, BackLoader<T> backLoader, Type typeOfT, int expireSecond);


}
