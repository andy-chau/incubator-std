package com.cldt.controller;

import java.util.Date;

/**
 * 
 * @Description:用户实体对象
 * @Project:boot-sis 
 * @File:UserEntity.java 
 * @Package:com.cldt.user.entity 
 * @Date:2018年7月5日下午8:44:23
 * @author:cldt 
 * @Copyright (c) 2018, 863263957@qq.com All Rights Reserved. 
 *
 */
public class UserEntity {
	private Integer id;
	private String account;
	private String name;
	private String sex;
	private String phone;
	private String email;
	private String quanPin;
	private String jianPin;
	private String ip;
	private Integer deptId;
	private String roleId;
	private String idCard;
	private String fileUrl;
	private String password;
	private String deleteFlag;
	private Date createTime;
	private String creator;
	private Date updateTime;
	private String updator;
	private Long version;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQuanPin() {
		return quanPin;
	}

	public void setQuanPin(String quanPin) {
		this.quanPin = quanPin;
	}

	public String getJianPin() {
		return jianPin;
	}

	public void setJianPin(String jianPin) {
		this.jianPin = jianPin;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}
	

}
