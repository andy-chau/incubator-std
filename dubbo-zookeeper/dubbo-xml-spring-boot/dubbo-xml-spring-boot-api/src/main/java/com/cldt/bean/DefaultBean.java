package com.cldt.bean;
/**
 * 
 * @ClassName:  DefaultBean   
 * @Description:默认Bean
 * @author: cldt
 * @date:   2018年8月22日 下午5:30:27   
 * @Copyright: 863263957@qq.com. All rights reserved. 
 *
 */
public class DefaultBean {
	private String str;
	private String methodName;
	private long timestamp;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "DefaultBean [str=" + str + ", methodName=" + methodName + ", timestamp=" + timestamp + "]";
	}
	

}
