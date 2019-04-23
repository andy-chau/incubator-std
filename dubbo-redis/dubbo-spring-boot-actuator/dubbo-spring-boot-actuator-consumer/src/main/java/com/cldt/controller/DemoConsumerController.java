package com.cldt.controller;

import com.cldt.service.DemoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
/**
 * 
 * @ClassName:  DemoConsumerController   
 * @Description:web调用服务提供者对外暴露的rpc接口
 * @author: cldt
 * @date:   2018年8月18日 上午9:41:30   
 * @Copyright: 863263957@qq.com. All rights reserved. 
 *
 */
@RestController
public class DemoConsumerController {

	/**
	 * 引入服务提供者
	 */
	//com.alibaba.dubbo.config.annotation.Reference
    @Reference
    private DemoService demoService;

    @RequestMapping("/sayHello")
    public String sayHello(@RequestParam String name) {
      return demoService.sayHello(name);
    }

}