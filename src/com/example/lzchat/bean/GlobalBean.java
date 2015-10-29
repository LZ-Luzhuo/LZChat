package com.example.lzchat.bean;

import java.io.Serializable;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-27 下午9:07:04
 * 
 * 描述:未解析方便,而设置的全局bean
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class GlobalBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3940226087546349662L;
	public String account;// 账号
	public String name;// 用户名
	public String icon;// 用户图像
	public int sex;// 性别 0:未设置 1:女 2:男 3:其他
	public String sign;// 用户个性签名
	public String area;// 用户所在区域
	public String token;// 用户与服务器交互的唯一标
	public boolean current;// 是否是当前用户
	public long id;
	public String owner;
	public String path;
	public int state;
	public String content;
	public int unread;
	public long updateTime;
	public String nickName;
	public String alpha;
	public int sort;
	public boolean agree;

	/**
	 * // 0:发送 1:接收
	 */
	public int direction;// 0:发送 1:接收
	public int type;// 0:text 1:image
	public String url;
	public boolean read;
	public long createTime;
	/**
	 * 是否是朋友圈
	 */
	public boolean CircleFriends;

}
