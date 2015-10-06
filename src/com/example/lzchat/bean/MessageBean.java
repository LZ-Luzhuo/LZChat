package com.example.lzchat.bean;

public class MessageBean {
	/**
	 * 标记 //1.新写的日志 2.获取日志...
	 */
	public int sign; //1.新写的日志 2.获取日志...
	/**
	 * 电话号码
	 */
	public String phone_num;
	/**
	 * 密码
	 */
	public String password;
	/**
	 * 日志
	 */
	public String message;
	
	public int successCode; //1.请求处理成功 2.请求处理失败

	@Override
	public String toString() {
		return "MessageBean [sign=" + sign + ", phone_num=" + phone_num
				+ ", password=" + password + ", message=" + message
				+ ", successCode=" + successCode + "]";
	}
	
	
}
