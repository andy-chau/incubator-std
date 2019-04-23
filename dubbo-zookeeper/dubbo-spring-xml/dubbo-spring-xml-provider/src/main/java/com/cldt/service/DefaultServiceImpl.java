package com.cldt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.cldt.bean.DefaultBean;

/**
 * 
 * @ClassName: DefaultDemoService
 * @Description:对外暴露接口实现类
 * @author: cldt
 * @date: 2018年8月21日 下午8:18:29
 * @Copyright: 863263957@qq.com. All rights reserved.
 *
 */

public class DefaultServiceImpl implements DefaultApiService {

	//模拟数据
	public static List<DefaultBean> list = new ArrayList<DefaultBean>();
	static {

		DefaultBean defaultBean = new DefaultBean();
		defaultBean.setStr("1");
		defaultBean.setMethodName("defaultMethod");
		defaultBean.setTimestamp(System.currentTimeMillis());
		list.add(defaultBean);
		DefaultBean defaultBean2 = new DefaultBean();
		defaultBean2.setStr("2");
		defaultBean2.setMethodName("defaultMethod2");
		defaultBean2.setTimestamp(System.currentTimeMillis());
		list.add(defaultBean2);
		DefaultBean defaultBean3 = new DefaultBean();
		defaultBean3.setStr("3");
		defaultBean3.setMethodName("defaultMethod3");
		defaultBean3.setTimestamp(System.currentTimeMillis());
		list.add(defaultBean);
		DefaultBean defaultBean4 = new DefaultBean();
		defaultBean4.setStr("4");
		defaultBean4.setMethodName("defaultMethod4");
		defaultBean4.setTimestamp(System.currentTimeMillis());
		list.add(defaultBean4);
		DefaultBean defaultBean5 = new DefaultBean();
		defaultBean5.setStr("5");
		defaultBean5.setMethodName("defaultMethod5");
		defaultBean5.setTimestamp(System.currentTimeMillis());
		list.add(defaultBean5);
	}

	@Override
	public String defaultMethod(String str) {
		Random random = new Random();
		int nextInt = random.nextInt(6);
		String res = null;
		if (nextInt > 0) {
			DefaultBean defaultBean = list.get(nextInt - 1);
			res = defaultBean.getStr() + "#" + defaultBean.getMethodName() + "#" + defaultBean.getTimestamp()
					+ "###^^^^^^^^^^^" + str + "^^^^^^^^^^^^";
		} else {
			DefaultBean defaultBean = list.get(nextInt);
			res = defaultBean.getStr() + "#" + defaultBean.getMethodName() + "#" + defaultBean.getTimestamp()
					+ "###^^^^^^^^^^^" + str + "^^^^^^^^^^^^";
		}
		return res;
	}

}