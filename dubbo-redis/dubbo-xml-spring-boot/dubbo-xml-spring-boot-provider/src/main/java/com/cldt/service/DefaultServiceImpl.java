package com.cldt.service;

import org.springframework.stereotype.Component;
/**
 * 
 * @ClassName:  DefaultServiceImpl   
 * @Description:对外暴露接口实现类 
 * @author: cldt
 * @date:   2018年9月2日 下午4:51:17   
 * @Copyright: 863263957@qq.com. All rights reserved. 
 *
 */
@Component
public class DefaultServiceImpl implements DefaultApiService{

	public String defaultMethod(String str) {
		return "hello "+str;
	}



}