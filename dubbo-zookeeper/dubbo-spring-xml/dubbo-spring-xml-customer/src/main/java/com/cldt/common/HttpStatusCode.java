package com.cldt.common;

/**
 * 
 * @ClassName: HttpStatusCode
 * @Description:具体业务请求状态码
 * @author: cldt
 *
 */
public enum HttpStatusCode {


	/**
	 * 具体业务状态码<br>
	 */
	ID_EXCEPTION(1001, "ID不成超过10");

	private final ErrorMessage errorMessage;

	HttpStatusCode(Integer value, String reasonPhrase) {
		this.errorMessage = new ErrorMessage(value, reasonPhrase, System.currentTimeMillis());
	}

	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

}
