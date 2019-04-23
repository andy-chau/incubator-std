package com.cldt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

/**
 * 
 * @ClassName:  DubboConsumerApplication   
 * @Description:消费者
 * @author: cldt
 * @date:   2018年8月17日 下午8:50:33   
 * @Copyright: 863263957@qq.com. All rights reserved. 
 *
 */
@SpringBootApplication
@EnableHystrix //Hystrix断路器
public class DubboConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DubboConsumerApplication.class, args); 
	}

}
