package com.example.lzchat.bean;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-28 下午3:39:25
 * 
 * 描述:请求添加
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class Invitation {

	public long id;
	/**
	 * 当前登录用户
	 */
	public String owner;
	/*
	 * 邀请人
	 */
	public String account;
	/**
	 * 邀请人昵称
	 */
	public String name;
	/**
	 * 邀请人头像
	 */
	public String icon;
	/**
	 * 邀请内容
	 */
	public String content;
	/**
	 * 0.未处理,1.已添加
	 */
	public boolean agree;

	@Override
	public String toString() {
		return "Invitation [id=" + id + ", owner=" + owner + ", account="
				+ account + ", name=" + name + ", icon=" + icon + ", content="
				+ content + ", agree=" + agree + "]";
	}

}
