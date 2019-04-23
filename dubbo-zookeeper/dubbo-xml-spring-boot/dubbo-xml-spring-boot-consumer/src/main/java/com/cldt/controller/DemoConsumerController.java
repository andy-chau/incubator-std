package com.cldt.controller;

import javax.annotation.Resource;

import com.cldt.service.DefaultApiService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @ClassName: DemoConsumerController
 * @Description:web调用服务提供者对外暴露的rpc接口
 * @author: cldt
 * @date: 2018年9月2日 下午4:50:38
 * @Copyright: 863263957@qq.com. All rights reserved.
 *
 */
@RestController
public class DemoConsumerController {

	/**
	 * 引入服务提供者
	 */
	@Resource(name = "defaultService")
	private DefaultApiService defaultService;

	@RequestMapping("/sayHello")
	public String sayHello(@RequestParam String name) {
		return defaultService.defaultMethod(name);
	}

}