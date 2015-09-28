package com.example.lzchat.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.bean.UserBean;
import com.example.lzchat.net.HttpClientUtil;
import com.example.lzchat.net.NetUtil;
import com.example.lzchat.utils.GsonTools;
import com.example.lzchat.utils.SharePrefUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-24 下午3:19:56
 * 
 * 描述:登录活动
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class LoginActivity extends Activity implements OnClickListener{
	protected static final int SUCCESS = 0;
	protected static final int FAILURE = 1;
	@ViewInject(R.id.login_password)
	private EditText login_password;
	@ViewInject(R.id.login_usernumber)
	private EditText login_usernumber;
	@ViewInject(R.id.login_login)
	private Button login_login;
	@ViewInject(R.id.login_top_left)
	private ImageView login_top_left;
	
	private String saveName;
	private String savePassword;
	private ProgressDialog progressDialog;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
//				Toast.makeText(LoginActivity.this, "登录成功!", 0).show();
				UserBean userBeanmsg = (UserBean) msg.obj;
				SharePrefUtil.saveString(LoginActivity.this, "lastnickname", userBeanmsg.nickname);
				SharePrefUtil.saveString(LoginActivity.this, "lastphone_num", userBeanmsg.phone_num);
				SharePrefUtil.saveString(LoginActivity.this, "lastPassword", userBeanmsg.password);
				LoginActivity.this.finish();
				startActivity(new Intent(LoginActivity.this, HomeActivity.class));
				break;
			case FAILURE:
				Toast.makeText(LoginActivity.this, "登录失败!", 0).show();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_btn_login);
		ViewUtils.inject(this);
		
		initView();
	}

	private void initView() {
		saveName = SharePrefUtil.getString(this, "lastphone_num", "");
		savePassword = SharePrefUtil.getString(this, "lastPassword", "");
		LogUtils.i("save:"+saveName +","+ savePassword);
		
		login_usernumber.setText(saveName);
		login_password.setText(savePassword);
		login_login.setOnClickListener(this);
		progressDialog = new ProgressDialog(LoginActivity.this);
		login_top_left.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_login:
			progressDialog.setMessage("正在登陆...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
			login();
			break;
		case R.id.login_top_left:
			LoginActivity.this.finish();
			break;
		}
	}

	private void login() {
		if(!NetUtil.checkNet(this)){
			progressDialog.dismiss();
			Toast.makeText(this, "请检查网络!",0).show();
			return;
		}
		String usernumber = login_usernumber.getText().toString().trim();
		if ("".equals(login_usernumber)) {
			progressDialog.dismiss();
			Toast.makeText(this, "账号不能为空!",0).show();
			return;
		}
		String password = login_password.getText().toString().trim();
		if ("".equals(login_password)) {
			progressDialog.dismiss();
			Toast.makeText(this, "密码不能为空!",0).show();
			return;
		}
		
		// 请求网络,发送信息
		final HttpClientUtil httpClientUtil = new HttpClientUtil();
		UserBean userBean = new UserBean();
		userBean.sign = 2;
		userBean.phone_num = usernumber;
		userBean.password = password;
		
		final String json = GsonTools.beanToJson(userBean);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					InputStream is = httpClientUtil.sendXml(GlobalParams.URL+GlobalParams.LOGIN, json);
					if(is == null){
						return;
					}
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
