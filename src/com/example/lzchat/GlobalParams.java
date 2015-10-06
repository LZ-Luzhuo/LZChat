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
	public static String URL = "http://192.168.0.103:80/LZChatService";
	
	public static String LOGIN = "/UserLogin";
	
	public static String UPDATE = "/Update";
	
	public static String MESSAGE = "/Message";
}
