package com.example.lzchat.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.bean.UserBean;
import com.example.lzchat.net.HttpClientUtil;
import com.example.lzchat.net.NetUtil;
import com.example.lzchat.utils.GsonTools;
import com.example.lzchat.utils.SharePrefUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-24 下午3:21:25
 * 
 * 描述:注册
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class RegisterActivity extends Activity implements OnClickListener{
	protected static final int SUCCESS = 0;
	protected static final int FAILURE = 1;
	@ViewInject(R.id.reg_register_btn)
	private Button reg_register_btn;
	@ViewInject(R.id.reg_camera_portrait)
	private ImageButton reg_camera_portrait;
	@ViewInject(R.id.reg_nickname)
	private EditText reg_nickname;
	@ViewInject(R.id.reg_phone_code)
	private EditText reg_phone_code;
	@ViewInject(R.id.reg_phone_num)
	private EditText reg_phone_num;
	@ViewInject(R.id.reg_password)
	private EditText reg_password;
	@ViewInject(R.id.reg_return_btn)
	private Button reg_return_btn;
	private ProgressDialog progressDialog;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				Toast.makeText(RegisterActivity.this, "注册成功!", 0).show();
				UserBean userBeanmsg = (UserBean) msg.obj;
				SharePrefUtil.saveString(RegisterActivity.this, "lastphone_num", userBeanmsg.phone_num);
				SharePrefUtil.saveString(RegisterActivity.this, "lastPassword", userBeanmsg.password);
				RegisterActivity.this.finish();
				startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
				break;
			case FAILURE:
				Toast.makeText(RegisterActivity.this, "注册失败!", 0).show();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_btn_register);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		reg_register_btn.setOnClickListener(this);
		reg_return_btn.setOnClickListener(this);
		
		progressDialog = new ProgressDialog(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_return_btn:
			finish();
			break;
		case R.id.reg_register_btn:
			progressDialog.setMessage("请稍候...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
			register();
			break;
		}
	}

	/**
	 * 注册信息
	 */
	private void register() {
		if(!NetUtil.checkNet(this)){
			progressDialog.dismiss();
			Toast.makeText(this, "请检查网络!",0).show();
			return;
		}
		String nickname = reg_nickname.getText().toString().trim();
		if ("".equals(nickname)) {
			progressDialog.dismiss();
			Toast.makeText(this, "昵称不能为空!",0).show();
			return;
		}
		String phone_num = reg_phone_num.getText().toString().trim();
		if ("".equals(phone_num)){
			progressDialog.dismiss();
			Toast.makeText(this, "手机号不能为空!",0).show();
			return;
		}
		String password = reg_password.getText().toString().trim();
		if ("".equals(password)) {
			progressDialog.dismiss();
			Toast.makeText(this, "密码不能为空!",0).show();
			return;
		}

		// 请求网络,发送信息
		final HttpClientUtil httpClientUtil = new HttpClientUtil();
		UserBean userBean = new UserBean();
		userBean.sign = 1;
		userBean.nickname = nickname;
		userBean.phone_num = phone_num;
		userBean.password = password;
		
		final String json = GsonTools.beanToJson(userBean);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					InputStream is = httpClientUtil.sendXml(GlobalParams.URL+GlobalParams.LOGIN, json);
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int i = -1;
					while ((i = is.read()) != -1) {
						baos.write(i);
					}
					String json = baos.toString("utf-8");
					LogUtils.i(json);
					UserBean regUserBean = GsonTools.jsonToBean(json, UserBean.class);
					progressDialog.dismiss();
					// 注册成功
					if(regUserBean!=null){
						if(regUserBean.successCode==1){
							Message message = new Message();
							message.what = SUCCESS;
							message.obj = regUserBean;
							handler.sendMessage(message);
						}else{
							Message message = new Message();
							message.what = FAILURE;
							handler.sendMessage(message);
						}
					}
				} catch (IOException e) {
					LogUtils.e("register http request error!");
					e.printStackTrace();
				}
			}
		}).start();
	}
}
