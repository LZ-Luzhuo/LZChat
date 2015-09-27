package com.example.lzchat.bean;

import java.util.List;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-25 下午9:35:41
 * 
 * 描述:用户基本信息类
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class UserBean {
	/**
	 * 标记 //1.注册 2.登录 3...
	 */
	public int sign; //1.注册 2.登录 3...
	/**
	 * 昵称
	 */
	public String nickname;
	/**
	 * 电话号码
	 */
	public String phone_num;
	/**
	 * 密码
	 */
	public String password;
	
	public int successCode; //1.请求处理成功 2.请求处理失败

	@Override
	public String toString() {
		return "UserBean [sign=" + sign + ", nickname=" + nickname
				+ ", phone_num=" + phone_num + ", password=" + password
				+ ", successCode=" + successCode + "]";
	}
	
	
	
}
