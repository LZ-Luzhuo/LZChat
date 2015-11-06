package com.example.lzchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.example.lzchat.ConstantValue;
import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.base.BaseActivity;
import com.example.lzchat.utils.SharePrefUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-19 下午10:02:30
 * 
 * 描述:第一个显示的欢迎界面
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class WelcomeActivity extends BaseActivity {
	protected static final int SUCCESS = 0;
	protected static final int FAILURE = 1;
	
	@ViewInject(R.id.login_denglu)
	private Button login_denglu;
	@ViewInject(R.id.login_zhuce)
	private Button login_zhuce;
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				// 3.销毁该活动展示主界面
				startActivity(new Intent(WelcomeActivity.this,HomeActivity.class));
				LogUtils.i("SUCCESS");
				WelcomeActivity.this.finish();
				break;
			case FAILURE:
				// 2.2没有登录显示登录/注册按钮
				login_denglu.setVisibility(View.VISIBLE);
				login_denglu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						WelcomeActivity.this.finish();
						startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
					}
				});
				login_zhuce.setVisibility(View.VISIBLE);
				login_zhuce.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						WelcomeActivity.this.finish();
						startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
					}
				});
				
				// TODO
				// 以下是临时代码,只是为了显示主界面
//				startActivity(new Intent(WelcomeActivity.this,HomeActivity.class));
				LogUtils.i("FAILURE");
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalParams.ac = this;
		LogUtils.allowI = GlobalParams.ShowLogI;
		LogUtils.allowE = GlobalParams.ShowLogE;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_welcome);
		ViewUtils.inject(this);
		System.out.println();
		
		new Thread() {
			@Override
			public void run() {
				try {
					// 1.睡3秒
					SystemClock.sleep(3000);
					// 获取登录标记
					String LoginTag = SharePrefUtil.getString(
							WelcomeActivity.this, ConstantValue.LOGIN, "");
					LogUtils.i("login:" + LoginTag);
					// 2.1是否已经登录,应经登录展示主界面
					if (LoginTag != "") {
						Message message = new Message();
						message.what = SUCCESS;
						handler.sendMessage(message);
					} else {
						Message message = new Message();
						message.what = FAILURE;
						handler.sendMessage(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

}
