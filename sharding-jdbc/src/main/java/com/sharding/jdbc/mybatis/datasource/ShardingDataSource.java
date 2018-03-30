
package com.sharding.jdbc.mybatis.datasource;

/**
 * 支持分库分表、读写分离数据源。
 */
public class ShardingDataSource extends AbstractRoutingDataSource{
	
	@Override
	protected String determineCurrentLookupKey() {
		return DataSourceLocalKeys.CURRENT_VDS_KEY.get();
	}

}
