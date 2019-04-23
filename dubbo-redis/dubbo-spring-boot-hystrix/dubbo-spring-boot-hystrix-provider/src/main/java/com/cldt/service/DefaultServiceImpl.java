package com.cldt.service;

import java.util.Random;

import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

/**
 * 
 * @ClassName: DefaultDemoService
 * @Description:对外暴露接口实现类
 * @author: cldt
 * @date: 2018年8月17日 下午7:50:47
 * @Copyright: 863263957@qq.com. All rights reserved.
 *
 */
// demo.service.version 在application.properties中配置过了
@Service // dubbo注解
@Component
public class DefaultServiceImpl implements DefaultApiService {

	@HystrixCommand(commandProperties = {
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000") })
	public String defaultMethod(String str) {
		/*
		 * Hystrix超时配置的为2s,当实现类睡眠超过2s，服务调用者将执行服务降级函数
		 */
		int nextInt = new Random().nextInt(4000);
		System.out.println("sleep " + nextInt + "ms");
		try {
			Thread.sleep(nextInt);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "HELLO  " + str + " from Dubbo Spring Boot";
	}

}