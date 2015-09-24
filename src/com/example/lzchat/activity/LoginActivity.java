package com.example.lzchat.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-24 下午3:19:56
 * 
 * 描述:登录活动
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class LoginActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView textview = new TextView(this);
		textview.setText("LoginActivity");
		setContentView(textview);
	}
}
