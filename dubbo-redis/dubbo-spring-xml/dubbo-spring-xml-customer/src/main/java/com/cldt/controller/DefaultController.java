package com.cldt.controller;




import org.springframework.web.bind.annotation.GetMapping;
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
		return "hello spring dubbo service consumter ";
	}

	
}
