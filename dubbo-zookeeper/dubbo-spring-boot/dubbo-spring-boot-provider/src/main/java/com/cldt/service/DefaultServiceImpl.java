package com.cldt.service;

import com.cldt.bean.DefaultBean;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;

/**
 * 
 * @ClassName:  DefaultDemoService   
 * @Description:对外暴露接口实现类
 * @author: cldt
 * @date:   2018年8月17日 下午7:50:47   
 * @Copyright: 863263957@qq.com. All rights reserved. 
 *
 */
//demo.service.version 在application.properties中配置过了
@Service//dubbo注解
@Component
public class DefaultServiceImpl implements DefaultApiService{

	public String defaultMethod(String str) {
		DefaultBean defaultBean = new DefaultBean();
		defaultBean.setStr(str);
		defaultBean.setMethodName("defaultMethod");
		defaultBean.setTimestamp(System.currentTimeMillis());
		return defaultBean.toString();
	}



}