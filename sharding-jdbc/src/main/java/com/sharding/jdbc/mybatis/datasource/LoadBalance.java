
package com.sharding.jdbc.mybatis.datasource;

import java.util.List;

import javax.sql.DataSource;

/**
 * 数据源负载策略。
 */
public interface LoadBalance {

	/**
	 * 从一组数据源中选择一个数据源。
	 * 
	 * @param dataSources 数据源组。
	 * @return
	 *      选择的数据源。
	 */
	DataSource select(List<DataSource> dataSources);
	
}
