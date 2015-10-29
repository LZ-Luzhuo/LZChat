package com.example.lzchat.client.receiver;

import android.content.BroadcastReceiver;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-21 下午11:25:37
 * 
 * 描述:推送广播接受者,获得来自CoreService(长连接服务)的广播数据(网络接收到的数据)
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public abstract class PushReceiver extends BroadcastReceiver {

	/**
	 * Action
	 */
	public static final String ACTION_TEXT = "action.text";
	
	/**
	 * Key
	 */
	public static final String DATA_KEY = "data";
	
	public static final String MESSAGE = "message";
	
	public static final String INVITATION = "Invitation";
}
