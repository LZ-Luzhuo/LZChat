package com.example.lzchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.lzchat.R;
import com.example.lzchat.base.BaseActivity;

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
public class FriendAddActivity extends BaseActivity implements OnClickListener{
	private EditText mSearchView;
	private View mScanView;
	private ImageView iv_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_friend_add);
		initView();
	}

	private void initView() {
		mSearchView = (EditText) findViewById(R.id.friend_add_et_search);
		mSearchView.setOnClickListener(this);
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TOOD　返回
		if (v == iv_back) {
			finish();
		} else if (v == mSearchView) {
			startActivity(new Intent(this, SearchContactActivity.class));
		}
	}
}
