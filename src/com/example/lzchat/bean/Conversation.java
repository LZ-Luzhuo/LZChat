package com.example.lzchat.bean;

public class Conversation {
	/**
	 * 当前登录用户
	 */
	public String owner;
	/**
	 * 接收者或发送者
	 */
	public String account;
	/**
	 * 头像
	 */
	public String icon;
	/**
	 * 名字
	 */
	public String name;
	/**
	 * 内容
	 */
	public String content;
	/**
	 * 未读数量
	 */
	public int unread;
	/**
	 * 更新时间
	 */
	public long updateTime;

}
