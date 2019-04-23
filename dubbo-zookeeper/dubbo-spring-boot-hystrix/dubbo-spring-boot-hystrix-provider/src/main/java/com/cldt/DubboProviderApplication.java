package com.cldt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * 
 * @ClassName:  DubboProviderApplication   
 * @Description:服务提供者启动类  
 * @author: cldt
 * @date:   2018年9月4日 下午3:46:10   
 * @Copyright: 863263957@qq.com. All rights reserved. 
 *
 */
@SpringBootApplication
@EnableHystrix //Hystrix断路器
public class DubboProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboProviderApplication.class, args); 
	}

}
