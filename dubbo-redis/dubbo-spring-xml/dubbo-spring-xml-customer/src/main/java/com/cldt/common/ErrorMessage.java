package com.cldt.common;

import java.io.Serializable;

/**
 * 
 * @ClassName:  ErrorMessage   
 * @Description:返回封装错误信息   
 *
 */
public class ErrorMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 错误码
	 */
	private Integer status;

	/**
	 * 错误信息
	 */
	private String message;
	/**
	 * 时间
	 */
	private Long timestamp;

	
	
	public ErrorMessage() {
		super();
	}



	public ErrorMessage(Integer status,String message,long timestamp){
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
	}



	public Integer getStatus() {
		return status;
	}



	public void setStatus(Integer status) {
		this.status = status;
	}



	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
