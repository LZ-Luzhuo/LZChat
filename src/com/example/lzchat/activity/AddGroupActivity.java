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
 * 创建日期:2015-9-23 下午4:03:35
 * 
 * 描述:发起群聊
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class AddGroupActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TextView textview = new TextView(this);
		textview.setText("AddGroupActivity");
		setContentView(textview);
	}
}
