package com.example.lzchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Window;
import android.widget.Button;

import com.example.lzchat.ConstantValue;
import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.utils.SharePrefUtil;
import com.lidroid.xutils.util.LogUtils;

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
public class WelcomeActivity extends Activity {
	protected static final int SUCCESS = 0;
	protected static final int FAILURE = 1;
	private Button login_denglu;
	private Button login_zhuce;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			super.handleMessage(msg);
			WelcomeActivity.this.finish();
			switch (msg.what) {
			case SUCCESS:
				// 3.销毁该活动展示主界面
				startActivity(new Intent(WelcomeActivity.this,HomeActivity.class));
				LogUtils.i("SUCCESS");
				break;
			case FAILURE:
				// 2.2没有登录显示登录/注册按钮
				// TODO
				// 以下是临时代码,只是为了显示主界面
				startActivity(new Intent(WelcomeActivity.this,HomeActivity.class));
//				startActivity(new Intent(WelcomeActivity.this,
//						RecommendActivity.class));
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
		LogUtils.allowI = GlobalParams.ShowLogI;
		LogUtils.allowE = GlobalParams.ShowLogE;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_welcome);
		login_denglu = (Button) findViewById(R.id.login_denglu);
		login_zhuce = (Button) findViewById(R.id.login_zhuce);

		new Thread() {
			@Override
			public void run() {
				try {
					// 1.睡3秒
					SystemClock.sleep(3000);
					// 获取登录标记
					String LoginTag = SharePrefUtil.getString(
							WelcomeActivity.this, ConstantValue.LOGIN, null);
					LogUtils.i("login:" + LoginTag);
					// 2.1是否已经登录,应经登录展示主界面
					if (LoginTag != null && !LoginTag.equals("")) {
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
