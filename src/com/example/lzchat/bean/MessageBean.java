package com.example.lzchat.bean;

import java.io.Serializable;
import java.util.Arrays;

public class MessageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 312433834735390980L;
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
	 * 消息
	 */
	public String message;
	
	public int successCode; //1.请求处理成功 2.请求处理失败
	/**
	 * 头像
	 */
	public String photo;
	/**
	 * 昵称
	 */
	public String nickname;
	/**
	 * 地址
	 */
	public String address;
	/**
	 * 发表时间
	 */
	public String time;
	/**
	 * 评语
	 */
	public String[] comment;
	/**
	 * 日志图像110x110
	 */
	public String[] messageImage;
	/**
	 * 点赞
	 */
	public String[] good;
	
	/**
	 * 是否是朋友圈
	 */
	public boolean CircleFriends = true;
	@Override
	public String toString() {
		return "MessageBean [sign=" + sign + ", phone_num=" + phone_num
				+ ", password=" + password + ", message=" + message
				+ ", successCode=" + successCode + ", photo=" + photo
				+ ", nickname=" + nickname + ", address=" + address + ", time="
				+ time + ", comment=" + Arrays.toString(comment)
				+ ", messageImage=" + Arrays.toString(messageImage) + ", good="
				+ Arrays.toString(good) + "]";
	}
	
	
}
