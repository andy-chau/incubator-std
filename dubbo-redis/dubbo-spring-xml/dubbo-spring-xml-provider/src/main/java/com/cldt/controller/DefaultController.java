package com.cldt.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @ClassName: DefaultController
 * @Description:默认控制
 * @author: cldt
 * @date: 2018年8月21日 下午7:39:56
 * @Copyright: 863263957@qq.com. All rights reserved.
 *
 */
@RestController
public class DefaultController {

	@GetMapping(value = "/")
	public String defaultMethod() {
		return "hello spring dubbo service provider ";
	}


}
