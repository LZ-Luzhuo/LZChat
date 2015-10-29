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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.bean.Friend;
import com.example.lzchat.bean.UserBean;
import com.example.lzchat.dao.FriendDao;
import com.example.lzchat.net.HttpClientUtil;
import com.example.lzchat.utils.GsonTools;
import com.lidroid.xutils.util.LogUtils;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-28 下午10:31:55
 * 
 * 描述:搜索联系人
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class SearchContactActivity extends Activity implements
OnClickListener, TextWatcher {
	protected static final int SUCCESS = 0;
	protected static final int FAILURE = 1;
	private ImageView ivBack;
	private EditText etSearch;
	private Button btnClearSearch;

	private View vClickItem;
	private TextView tvSearchContent;
	private ProgressDialog progressDialog;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				// 成功
				UserBean userBeanmsg = (UserBean) msg.obj;
				Friend t = new Friend();
				t.name = userBeanmsg.nickname;
				t.account = userBeanmsg.phone_num;
				t.nickName = userBeanmsg.nickname;
				t.icon = userBeanmsg.photo;
				
				Intent intent = new Intent(SearchContactActivity.this,FriendDetailActivity.class);
				intent.putExtra(FriendDetailActivity.KEY_ENTER,FriendDetailActivity.ENTER_SEARCH);
				intent.putExtra(FriendDetailActivity.KEY_DATA, t);
				startActivity(intent);
	
				finish();
				break;
			case FAILURE:
				Toast.makeText(SearchContactActivity.this, "搜索的用户不存在!", 0).show();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_search_contact);
		
		initView();
	}

	private void initView() {
		ivBack = (ImageView) findViewById(R.id.bar_btn_back);
		etSearch = (EditText) findViewById(R.id.bar_et_search);
		btnClearSearch = (Button) findViewById(R.id.bar_btn_clear_search);

		vClickItem = findViewById(R.id.search_item);
		tvSearchContent = (TextView) findViewById(R.id.search_tv_content);

		vClickItem.setVisibility(View.GONE);
		btnClearSearch.setVisibility(View.GONE);
		
		ivBack.setOnClickListener(this);
		btnClearSearch.setOnClickListener(this);
		vClickItem.setOnClickListener(this);

		etSearch.addTextChangedListener(this);
		progressDialog = new ProgressDialog(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v == ivBack) {
			finish();
		} else if (v == btnClearSearch) {
			etSearch.setText("");
		} else if (v == vClickItem) {
			clickItem();
		}
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}
	
	@Override
	public void afterTextChanged(Editable s) {
		String searchContent = etSearch.getText().toString().trim();
		if (TextUtils.isEmpty(searchContent)) {
			vClickItem.setVisibility(View.GONE);
			btnClearSearch.setVisibility(View.GONE);
			return;
		}

		tvSearchContent.setText(searchContent);
		vClickItem.setVisibility(View.VISIBLE);
		btnClearSearch.setVisibility(View.VISIBLE);
	}
	
	private void clickItem() {
		String account = etSearch.getText().toString().trim();

		String phone = GlobalParams.sender;
		if (phone.equals(account)) {
			Toast.makeText(this, "不能添加自己!", 0).show();
			return;
		}

		// 已有的朋友
		FriendDao dao = new FriendDao(this);
		Friend friend = dao.queryFriendByAccount(phone, account);
		if (friend != null) {
			Intent intent = new Intent(this, FriendDetailActivity.class);
			intent.putExtra(FriendDetailActivity.KEY_ENTER,
					FriendDetailActivity.ENTER_CONTACT);
			intent.putExtra(FriendDetailActivity.KEY_DATA, friend);
			startActivity(intent);

			return;
		}

		//searchfriend
		progressDialog.setMessage("请稍候...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();

		final HttpClientUtil httpClientUtil = new HttpClientUtil();
		UserBean userBean = new UserBean();
		userBean.phone_num = account;
		userBean.sign = 3;
		
		final String beanToJson = GsonTools.beanToJson(userBean);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					InputStream is = httpClientUtil.sendXml(GlobalParams.URL+GlobalParams.SEARCHFRIEND, beanToJson);
					
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
					progressDialog.dismiss();
					LogUtils.e("register http request error!");
					e.printStackTrace();
				}
			}
		}).start();
	}		
		
}
