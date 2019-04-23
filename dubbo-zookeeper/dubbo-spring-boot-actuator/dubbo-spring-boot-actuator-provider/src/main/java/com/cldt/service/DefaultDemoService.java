package com.cldt.service;

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
@Service
public class DefaultDemoService implements DemoService {

    public String sayHello(String name) {
        return "Hello, " + name + " (from Spring Boot)";
    }

}