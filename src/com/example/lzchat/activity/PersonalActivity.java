package com.example.lzchat.activity;

import com.example.lzchat.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
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
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_personal_information);
	}
}
