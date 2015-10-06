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
 * 创建日期:2015-10-6 下午2:13:30
 * 
 * 描述:发表文章
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class PublishActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textview = new TextView(this);
		textview.setText("PublishActivity");
		setContentView(textview);
	}
}
