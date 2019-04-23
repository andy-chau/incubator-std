package com.cldt.exception;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.lang3.StringUtils;
import com.cldt.common.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 
 * @ClassName: ControllerAdviceException
 * @Description:web层统一异常处理
 * @author: cldt
 *
 */
@ControllerAdvice
public class ControllerAdviceException extends ResponseEntityExceptionHandler {

	/**
	 * 前后端分离异步异常处理
	 * 
	 * @param request
	 * @param ex
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	@ExceptionHandler({ Exception.class })
	@ResponseBody
	ResponseEntity<?> handleControllerException(HttpServletRequest request, Throwable ex, HttpServletResponse response)
			throws ServletException, IOException {
		HttpStatus status = getStatus(request);
		String message = "";

		// 文件上传
		if (ex instanceof MultipartException) {
			MultipartException e = (MultipartException) ex;
			FileSizeLimitExceededException ee = (FileSizeLimitExceededException) e.getCause().getCause();
			String acutalSize = "实际为:" + (ee.getActualSize() / 1024) + "KB";
			message = "允许上传为:" + (ee.getPermittedSize() / 1024) + "KB" + acutalSize;
		}

		ErrorMessage cx = createExceptionAndSendMessage(status, request, ex);
		if (StringUtils.isNotBlank(message)) {
			cx.setMessage(message);
		}
		return new ResponseEntity<>(cx, status);

	}

	/**
	 * 错误异常封装
	 * 
	 * @param status
	 * @param request
	 * @param ex
	 * @return
	 */
	public ErrorMessage createExceptionAndSendMessage(HttpStatus status, HttpServletRequest request, Throwable ex) {
		ErrorMessage customErrorType = new ErrorMessage();
		customErrorType.setStatus(status.value());
		customErrorType.setMessage(ex.getLocalizedMessage());
		customErrorType.setTimestamp(System.currentTimeMillis());
		return customErrorType;
	}

	/**
	 * 获取系统错误码
	 * 
	 * @param request
	 * @return
	 */
	private HttpStatus getStatus(HttpServletRequest request) {
		Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
		if (statusCode == null) {
			return HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return HttpStatus.valueOf(statusCode);
	}

}