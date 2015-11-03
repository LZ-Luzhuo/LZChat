package com.example.lzchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.lzchat.ConstantValue;
import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.utils.SharePrefUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-28 下午11:25:27
 * 
 * 描述:设置详情页
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class SettingActivity extends Activity implements OnClickListener{
	@ViewInject(R.id.setting_exit)
	private LinearLayout setting_exit;
	@ViewInject(R.id.personal_return)
	private ImageButton personal_return;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_personal_setting);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		setting_exit.setOnClickListener(this);
		personal_return.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 退出
		case R.id.setting_exit:
			// 清除一些记录
			SharePrefUtil.saveString(this, ConstantValue.LOGIN, "");
//			SharePrefUtil.saveString(this, "lastPassword", "");
//			SharePrefUtil.saveString(this, "avatar", "");
//			SharePrefUtil.saveString(this, "icon", "");
			SettingActivity.this.finish();
			if(GlobalParams.hm != null)
				GlobalParams.hm.finish();
			startActivity(new Intent(SettingActivity.this, LoginActivity.class));
			break;
		case R.id.personal_return:
			setResult(Activity.RESULT_OK, new Intent());
			SettingActivity.this.finish();
			break;
		}
	}
}
