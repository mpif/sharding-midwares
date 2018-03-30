
package com.sharding.jdbc.mybatis.datasource;

/**
 * DataSource局部相关的一些Key值。
 */
public class DataSourceLocalKeys {
	
	/**
	 * 存储虚拟数据源Key的线程变量。
	 */
	public static final ThreadLocal<String> CURRENT_VDS_KEY = new ThreadLocal<String>();
	
	/**
	 * 存储数据源组Key的线程变量。
	 */
	public static final ThreadLocal<String> CURRENT_DS_GROUP_KEY = new ThreadLocal<String>();
	
	/**
	 * 存储手工指定数据源组Key的线程变量。
	 */
	public static final ThreadLocal<String> CURRENT_MANUAL_DS_GROUP_KEY = new ThreadLocal<String>();
	
	public static final String MASTER_KEY = "master";
	public static final String SLAVE_KEY = "slave";
	
	/**
	 * 存储主从标示的线程变量。
	 */
	public static final ThreadLocal<String> CURRENT_MASTER_SLAVE_KEY = new ThreadLocal<String>(){

		@Override
		protected String initialValue() {
			return MASTER_KEY;
		}
		
	};
	
	public static void chooseMaster(){
		CURRENT_MASTER_SLAVE_KEY.set(MASTER_KEY);
	}
	
	public static void chooseSlave(){
		CURRENT_MASTER_SLAVE_KEY.set(SLAVE_KEY);
	}
	
	private DataSourceLocalKeys(){}

}
