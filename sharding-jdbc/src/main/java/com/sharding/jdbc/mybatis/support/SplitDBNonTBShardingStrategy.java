
package com.sharding.jdbc.mybatis.support;

import com.sharding.jdbc.mybatis.ShardingStrategy;

/**
 * 分库不分表的拆分策略实现。
 * <p>具体策略：
 * <p>通过字段值算出hashCode，然后对分表总数取模作为分表号。
 * 分表号除以分库总数作为分库号。
 *
 */
public class SplitDBNonTBShardingStrategy extends DefaultShardingStrategy implements ShardingStrategy {

	public <V> NumberPair sharding(V shardingValue,
			int shardingTableCount, int shardingDBCount) {
		
		NumberPair numberPair = super.sharding(shardingValue, shardingTableCount, shardingDBCount);
		
		// 只分库不分表，则库号和表号是一样的，0号库0号表，1号库1号表
		int shardingTableNo = numberPair.getDataSourceNo();
		int shardingDBNo = numberPair.getDataSourceNo();
		return NumberPair.pair(shardingTableNo, shardingDBNo);
	}
	
}
