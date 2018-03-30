package com.sharding.redis.jedis;

import com.sharding.redis.jedis.support.ShardedRedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by caishengzhi on 2018/3/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring*.xml")
@TestPropertySource(properties={"env=std-test"})
public class JedisShardingTest {

    @Resource
    private ShardedRedisClient redisClient;

    @Test
    public void redisClientTest() {

        String key = "com.lejr.redis.key.jedisTestKeyStr";
        String val = "hello Jedis";
        redisClient.set(key, val);

        String redisVal = redisClient.getString(key);
        System.out.println("redisVal:");
        System.out.println(redisVal);


    }


}
