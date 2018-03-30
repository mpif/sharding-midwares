package com.sharding.redis.jedis.statistic;

/**
 * Redis客户端统计量。
 */
public class ClientStatistic {

    //已经连接的客户端数量。
    private int connectedClientCount;

    //正在阻塞的客户端数量。
    private int blockedClientCount;

    public int getConnectedClientCount() {
        return connectedClientCount;
    }

    public void setConnectedClientCount(int connectedClientCount) {
        this.connectedClientCount = connectedClientCount;
    }

    public int getBlockedClientCount() {
        return blockedClientCount;
    }

    public void setBlockedClientCount(int blockedClientCount) {
        this.blockedClientCount = blockedClientCount;
    }

    @Override
    public String toString() {
        return "ClientStatistic{" +
                "connectedClientCount=" + connectedClientCount +
                ", blockedClientCount=" + blockedClientCount +
                '}';
    }

}
