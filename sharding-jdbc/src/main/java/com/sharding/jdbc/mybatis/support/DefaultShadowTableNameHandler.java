
package com.sharding.jdbc.mybatis.support;


import com.sharding.jdbc.mybatis.TableNameHandler;

/**
 * 用于线上测试的影子表名处理器。
 * <p>这个处理器的逻辑是将分表名称替换为影子表。
 * <p>比如原始表是user_0004,那么影子表就是shadow_user_0004。
 */
public class DefaultShadowTableNameHandler  implements TableNameHandler {

	private static final String SHADOWTABLE_PREFIX = "shadow_";
	
	private TableNameHandler tableNameHandler;
	
	public DefaultShadowTableNameHandler(TableNameHandler tableNameHandler) {
		super();
		this.tableNameHandler = tableNameHandler;
	}

	@Override
	public String generateRealTableName(String tablePrefix, int shardingTableNo, int shardingTableCount) {
		StringBuilder builder = new StringBuilder();
		builder.append(SHADOWTABLE_PREFIX);
		builder.append(tableNameHandler.generateRealTableName(tablePrefix, shardingTableNo, shardingTableCount));
		return builder.toString();
	}

}
