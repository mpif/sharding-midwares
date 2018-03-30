package com.sharding.redis.jedis.statistic;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis组统计量。
 */
public class RedisGroupStatistic {

    //Redis主节点统计量。
    private RedisStatistic masterStatistic;

    //Redis从节点统计量列表。
    private List<RedisStatistic> slaveStatistics = new ArrayList<RedisStatistic>();

    public RedisStatistic getMasterStatistic() {
        return masterStatistic;
    }

    public void setMasterStatistic(RedisStatistic masterStatistic) {
        this.masterStatistic = masterStatistic;
    }

    public List<RedisStatistic> getSlaveStatistics() {
        return slaveStatistics;
    }

    public void setSlaveStatistics(List<RedisStatistic> slaveStatistics) {
        this.slaveStatistics = slaveStatistics;
    }

    public void addSlaveStatistic(RedisStatistic slaveStatistic){
        slaveStatistics.add(slaveStatistic);
    }

    @Override
    public String toString() {
        return "RedisGroupStatistic{" +
                "masterStatistic=" + masterStatistic +
                ", slaveStatistics=" + slaveStatistics +
                '}';
    }

}
