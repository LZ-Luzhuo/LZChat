package com.example.lzchat.base;

import android.app.Service;

import com.example.lzchat.ChatApplication;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-18 下午10:47:52
 * 
 * 描述:所有Service都必须继承BaseService,
 * 		主要工作添加Service到ChatApplication,
 * 		移除Service到ChatApplication
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public abstract class BaseService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		
		((ChatApplication)getApplication()).addService(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		((ChatApplication)getApplication()).removeService(this);
	}
}
