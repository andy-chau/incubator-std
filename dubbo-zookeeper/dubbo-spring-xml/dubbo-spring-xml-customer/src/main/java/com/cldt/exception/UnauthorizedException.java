package com.cldt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * 未授权异常(即未登录或无效Token)<br>
 * 状态码为401<br>
 * 
 * @ClassName: UnauthorizedException
 * @Description: User must authenticate before making a request.
 * @author: cldt
 *
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "User Unauthorized")
public class UnauthorizedException extends RuntimeException {
  
	/**
	 * @Fields serialVersionUID : 序列化id
	 */
	private static final long serialVersionUID = -1843920197936615035L;

}
