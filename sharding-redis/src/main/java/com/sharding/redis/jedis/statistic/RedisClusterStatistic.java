
package com.sharding.redis.jedis.statistic;

import java.util.List;

/**
 * Redis集群统计量。
 */
public class RedisClusterStatistic {

    //redis服务器组统计量。
    private List<RedisGroupStatistic> groupStatistics;

    public List<RedisGroupStatistic> getGroupStatistics() {
        return groupStatistics;
    }

    public void setGroupStatistics(List<RedisGroupStatistic> groupStatistics) {
        this.groupStatistics = groupStatistics;
    }

    @Override
    public String toString() {
        return "RedisStatistic{" +
                "groupStatistics=" + groupStatistics +
                '}';
    }

}
