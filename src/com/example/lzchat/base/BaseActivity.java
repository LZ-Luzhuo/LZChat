package com.example.lzchat.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.lzchat.ChatApplication;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-18 下午10:45:35
 * 
 * 描述:所有activity必须继承BaseActivity,
 * 		主要工作添加activity到ChatApplication,
 * 		移除activity到ChatApplication
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class BaseActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		((ChatApplication) getApplication()).addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		((ChatApplication) getApplication()).removeActivity(this);
	}
}
