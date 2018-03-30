
package com.sharding.jdbc.mybatis;

/**
 * 测试相关支持类。
 */
public class TestSupports {
	
	/**
	 * 选择影子表。
	 */
	public static void chooseShadowTable(){
		SHADOW_FLAG.set(Boolean.TRUE);
	}
	
	/**
	 * 清空影子表标记。
	 */
	public static void clearShadowFlag(){
		SHADOW_FLAG.remove();
	}
	
	/**
	 * 判断是否选择了影子表。
	 * 
	 * @return
	 */
	protected static boolean isShadowTableChoosed(){
		Boolean flag = SHADOW_FLAG.get();
		if(flag == null){
			return false;
		}else{
			return flag.booleanValue();
		}
	}

	private static final ThreadLocal<Boolean> SHADOW_FLAG = 
			new ThreadLocal<Boolean>(){
				@Override
				protected Boolean initialValue() {
					return Boolean.FALSE;
				}
			};

	private TestSupports(){}
	
}
