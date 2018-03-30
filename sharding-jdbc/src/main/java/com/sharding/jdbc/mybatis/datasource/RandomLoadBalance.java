
package com.sharding.jdbc.mybatis.datasource;

import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

/**
 * 随机选择的负载策略。
 */
public class RandomLoadBalance implements LoadBalance{

	public DataSource select(List<DataSource> dataSources) {
		if(dataSources == null){
			throw new IllegalArgumentException("dataSources is null!");
		}
		int size = dataSources.size();
		if(size == 0){
			throw new IllegalArgumentException("dataSources is empty!");
		}
		if(size == 1){
			return dataSources.get(0);
		}
		Random random = new Random();
		int index = random.nextInt(size);
		return dataSources.get(index);
	}

}
