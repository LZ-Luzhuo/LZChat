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
 * 创建日期:2015-9-28 下午11:20:36
 * 
 * 描述:个人详情信息
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class PersonalActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView textview = new TextView(this);
		textview.setText("PersonalActivity");
		setContentView(textview);
	}
}
