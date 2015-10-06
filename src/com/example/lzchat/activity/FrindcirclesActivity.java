package com.example.lzchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.lzchat.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-5 下午3:31:32
 * 
 * 描述:朋友圈详情
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class FrindcirclesActivity extends Activity implements OnClickListener {
	@ViewInject(R.id.personal_return)
	private ImageButton personal_return;
	@ViewInject(R.id.personal_publish)
	private ImageView personal_publish;
	@ViewInject(R.id.personal_top_layout)
	private LinearLayout personal_top_layout;
	// TODO
	@ViewInject(R.id.listview)
	private ListView listview;

	private View talk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_circles);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		personal_return.setOnClickListener(this);
		personal_publish.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 直接打开发表心情的界面
		case R.id.personal_publish:
			startActivity(new Intent(FrindcirclesActivity.this,PublishActivity.class));
			break;
		case R.id.personal_return:
			FrindcirclesActivity.this.finish();
			break;
		}
	}
}
