package com.cldt.controller;

import java.util.ArrayList;
import java.util.List;

import com.cldt.common.HttpStatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * 400 - [无效请求参数内容] Bad Request Some content in the request was invalid. <br>
 * 401 - [用户未授权或Token过期] Unauthorized User must authenticate before making a
 * request.<br>
 * 403 - [无权限访问] Forbidden Policy doesnot allow current user to do this
 * operation.<br>
 * 
 * @ClassName: UserController
 * @Description:RESTful API演示demo
 * @author: cldt
 *
 */
@RestController
public class UserController {
	public static List<UserEntity> list = new ArrayList<UserEntity>();
	static {
		for (int i = 0; i <= 10; i++) {
			UserEntity userEntity = new UserEntity();
			long currentTimeMillis = System.currentTimeMillis();
			userEntity.setAccount(String.valueOf(currentTimeMillis));
			userEntity.setName("张三" + i);
			list.add(userEntity);
		}
	}

	/**
	 * 查询用户列表
	 * 
	 * @return
	 */
	@GetMapping("rest/user")
	public ResponseEntity<Object> getUserList() {
		return new ResponseEntity<Object>(list, HttpStatus.OK);
	}

	/**
	 * 查询用户对象
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("rest/user/{id}")
	public ResponseEntity<Object> getUserById(@PathVariable Integer id) {
		if (id > 10) {
			return new ResponseEntity<Object>(HttpStatusCode.ID_EXCEPTION.getErrorMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(list.get(id), HttpStatus.OK);
	}

}
