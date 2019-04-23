package com.cldt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * 
 * @ClassName:  DubboConsumerApplication   
 * @Description:消费者
 * @author: cldt
 * @date:   2018年9月2日 下午4:46:12   
 * @Copyright: 863263957@qq.com. All rights reserved. 
 *
 */
@SpringBootApplication
@ImportResource(locations = "classpath:consumer.xml")
public class DubboConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboConsumerApplication.class, args);
	}

}
