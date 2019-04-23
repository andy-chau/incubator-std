package com.cldt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 
 * @ClassName:  DubboProviderApplication   
 * @Description:服务提供者启动类  
 * @author: cldt
 * @date:   2018年9月2日 下午4:46:24   
 * @Copyright: 863263957@qq.com. All rights reserved. 
 *
 */
@SpringBootApplication
@ImportResource(locations="classpath:provider.xml")
public class DubboProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboProviderApplication.class, args); 
	}

}
