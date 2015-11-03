package com.example.lzchat;

import com.example.lzchat.activity.HomeActivity;
import com.example.lzchat.activity.WelcomeActivity;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-19 下午11:00:13
 * 
 * 描述:全局属性的配置,配置一些可能需要变动的属性
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class GlobalParams {
	/**
	 * 显示log.i
	 */
	public static boolean ShowLogI = true;
	/**
	 * 显示log.e
	 */
	public static boolean ShowLogE = true;
	
	/**
	 * 代理的ip
	 */
	public static String PROXY="";
	/**
	 * 代理的端口
	 */
	public static int PORT=0;
	/**
	 * WelcomeActivity的引用
	 */
	public static WelcomeActivity ac;
	/**
	 * HomeActivity的引用
	 */
	public static HomeActivity hm;
	
	/**
	 * address
	 */
	public final static String URL = "http://114.215.201.157:80/LZChatService";
	public final static String ICONURL = URL+"/usericon/";
	// TCP连接主机地址
	public final static String dstName = "114.215.201.157";
	// TCP连接端口
	public final static int dstPort = 10086;
	
	public static String LOGIN = "/UserLogin";
	
	public static String UPDATE = "/Update";
	
	public static String MESSAGE = "/Message";
	
	
	// 当前用户
	public static String sender = "111";
	// 用户标志
	public static String token = "111";
	//接收信息的用户
	public static String receiver = "B";
	/**
	 * 当前用户头像信息
	 */
	public static String ico = "http://123";
	/**
	 * 当前用户昵称
	 */
	public static String nickname = "nicheng";

	
	public static String SEARCHFRIEND = "/Searchfriend";
}
