package com.cldt.service;
/**
 * 
 * @ClassName:  DemoService   
 * @Description: Dubbo RPC API ，由服务提供方为服务消费方暴露接口
 * @author: cldt
 * @date:   2018年8月17日 下午7:49:11   
 * @Copyright: 863263957@qq.com. All rights reserved. 
 *
 */
public interface DemoService {
	   String sayHello(String name);
}
