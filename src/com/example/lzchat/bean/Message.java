package com.example.lzchat.bean;

public class Message {

	public long id;
	/**
	 * 当前登录用户
	 */
	public String owner;
	/**
	 * 接收者或发送者
	 */
	public String account;
	/**
	 * 0:发送 1:接收
	 */
	public int direction;
	/**
	 * 0:text 1:image
	 */
	public int type;
	/**
	 * 内容
	 */
	public String content;
	/**
	 * 图片
	 */
	public String url;
	/**
	 * 发送状态: 1.正在发送 2.已经成功发送 3.发送失败
	 */
	public int state;
	/**
	 * 是否已读:0.未读 1.已读
	 */
	public boolean read;
	/**
	 * 消息创建时间
	 */
	public long createTime;

}
