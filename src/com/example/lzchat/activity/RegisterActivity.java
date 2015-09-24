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
 * 创建日期:2015-9-24 下午3:21:25
 * 
 * 描述:注册
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class RegisterActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView textview = new TextView(this);
		textview.setText("RegisterActivity");
		setContentView(textview);
	}
}
