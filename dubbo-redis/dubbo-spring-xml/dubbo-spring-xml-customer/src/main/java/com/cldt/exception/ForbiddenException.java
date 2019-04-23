package com.cldt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * 无权限访问<br>
 * 状态码为403<br>
 * 
 * @ClassName: ForbiddenException
 * @Description:Policy does not allow current user to do this operation.
 * @author: cldt
 *
 */
@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "No Permission To Access")
public class ForbiddenException extends RuntimeException {

	/**
	 * @Fields serialVersionUID : 序列化id
	 */
	private static final long serialVersionUID = 1566943100966900331L;

}
