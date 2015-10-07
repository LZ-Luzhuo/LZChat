package com.example.lzchat.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.bean.MessageBean;
import com.example.lzchat.net.HttpClientUtil;
import com.example.lzchat.net.NetUtil;
import com.example.lzchat.utils.GsonTools;
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
 * 创建日期:2015-10-6 下午2:13:30
 * 
 * 描述:发表文章
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class PublishActivity extends Activity implements OnClickListener{
	@ViewInject(R.id.personal_return)
	private ImageButton personal_return;
	@ViewInject(R.id.personal_send)
	private Button personal_send;
	@ViewInject(R.id.message)
	private EditText message;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_publish);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		personal_send.setOnClickListener(this);
		personal_return.setOnClickListener(this);
		progressDialog = new ProgressDialog(PublishActivity.this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personal_send:
			progressDialog.setMessage("发表中...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
			sendMessage();
			break;
		case R.id.personal_return:
			PublishActivity.this.finish();
			break;
		}
	}
	
	/**
	 * 访问网络发送数据
	 */
	private void sendMessage() {
		if(!NetUtil.checkNet(this))
			Toast.makeText(this, "请检查网络!", 0);
		MessageBean messageBean = new MessageBean();
		String mes = message.getText().toString();
		if(mes != null && mes != "")
			messageBean.message = mes;
		String lastphone_num = SharePrefUtil.getString(this, "lastphone_num", "");
		if(lastphone_num != "")
			messageBean.phone_num = lastphone_num;
		String lastPassword = SharePrefUtil.getString(this, "lastPassword", "");
		if(lastPassword != "")
			messageBean.password = lastPassword;
		messageBean.sign = 1;
		
		final String mess = GsonTools.beanToJson(messageBean);
		
		new Thread(){
			@Override
			public void run() {
				super.run();
				HttpClientUtil clientUtil = new HttpClientUtil();
				clientUtil.sendXml(GlobalParams.URL+GlobalParams.MESSAGE, mess);
				progressDialog.dismiss();
				PublishActivity.this.finish();
			}
		}.start();
	}
}
