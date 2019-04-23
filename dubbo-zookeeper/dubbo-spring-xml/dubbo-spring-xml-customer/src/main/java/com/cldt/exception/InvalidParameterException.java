package com.cldt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 无效参数异常<br>
 * 状态码为400<br>
 * 
 * @ClassName: InvalidParameterException
 * @Description: Some content in the request was invalid.
 * @author: cldt.
 *
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Request Content")
public class InvalidParameterException extends RuntimeException {

	/**
	 * @Fields serialVersionUID : 序列化id
	 */
	private static final long serialVersionUID = 1455338957597326459L;

	private Object message;

	public InvalidParameterException() {
		super();
	}

	public InvalidParameterException(Object message) {
		super();
		this.message = message;
	}

}
