package com.example.lzchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.example.lzchat.R;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-28 上午12:23:32
 * 
 * 描述:添加朋友
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class FriendAddActivity extends Activity implements OnClickListener{
	private EditText mSearchView;
	private View mScanView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_friend_add);
		initView();
	}

	private void initView() {
		mSearchView = (EditText) findViewById(R.id.friend_add_et_search);
		mSearchView.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TOOD　返回
		if (false) {
			finish();
		} else if (v == mSearchView) {
			startActivity(new Intent(this, SearchContactActivity.class));
		}
	}
}
