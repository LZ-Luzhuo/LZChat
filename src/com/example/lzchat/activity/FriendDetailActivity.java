package com.example.lzchat.activity;

import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.base.BaseActivity;
import com.example.lzchat.bean.Friend;
import com.example.lzchat.bean.Invitation;
import com.example.lzchat.client.ConnectorManager;
import com.example.lzchat.client.request.Request;
import com.example.lzchat.client.request.TextRequest;
import com.example.lzchat.utils.GsonTools;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.util.LogUtils;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-28 上午12:26:55
 * 
 * 描述:朋友详情
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class FriendDetailActivity extends BaseActivity implements OnClickListener{
	public static final String KEY_ENTER = "enter";
	public static final String KEY_DATA = "data";

	public static final int ENTER_SEARCH = 1;
	public static final int ENTER_CONTACT = 2;
	
	private int enterFlag;
	private Friend friend;
	
	private ImageView mIvIconView;

	private TextView mTvNameView;
	private TextView mTvAccountView;
	private TextView mTvNickNameView;

	private TextView mTvSignView;

	private Button mBtnAdd;
	private Button mBtnSend;
	
	private ImageView iv_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_friend_detail);
		
		enterFlag = getIntent().getIntExtra(KEY_ENTER, -1);
		if (enterFlag == -1) {
			throw new RuntimeException("没有定义入口");
		}
		friend = (Friend) getIntent().getSerializableExtra(KEY_DATA);
		
		initView();
	}

	private void initView() {
		mIvIconView = (ImageView) findViewById(R.id.friend_detail_iv_icon);
		mTvNameView = (TextView) findViewById(R.id.friend_detail_tv_name);
		mTvAccountView = (TextView) findViewById(R.id.friend_detail_tv_account);
		mTvNickNameView = (TextView) findViewById(R.id.friend_detail_tv_nickname);

		mTvSignView = (TextView) findViewById(R.id.friend_detail_tv_sign);

		mBtnAdd = (Button) findViewById(R.id.friend_detail_btn_add);
		mBtnSend = (Button) findViewById(R.id.friend_detail_btn_send);
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		
		if (enterFlag == ENTER_SEARCH) {
			mTvAccountView.setVisibility(View.GONE);
			mTvNickNameView.setVisibility(View.GONE);

			mBtnAdd.setVisibility(View.VISIBLE);
			mBtnSend.setVisibility(View.GONE);

			mTvNameView.setText(friend.name);
			if(friend.icon != null){
				BitmapUtils bitmapUtils = new BitmapUtils(this);
				bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
				bitmapUtils.display(mIvIconView, friend.icon);
			}
		} else if (enterFlag == ENTER_CONTACT) {
			mTvAccountView.setVisibility(View.VISIBLE);
			mTvNickNameView.setVisibility(View.VISIBLE);

			mBtnAdd.setVisibility(View.GONE);
			mBtnSend.setVisibility(View.VISIBLE);

			mTvNameView.setText(friend.name);
			mTvAccountView.setText("黑信号:" + friend.account);
			mTvNickNameView.setText("昵称:" + friend.nickName);
			if(friend.icon != null){
				BitmapUtils bitmapUtils = new BitmapUtils(this);
				bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
				bitmapUtils.display(mIvIconView, friend.icon);
			}
		}
		
		mBtnAdd.setOnClickListener(this);
		mBtnSend.setOnClickListener(this);
		iv_back.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO 返回
		if (v == iv_back) {
			finish();
		} else if (v == mBtnAdd) {
			clickAdd();
		} else if (v == mBtnSend) {
			clickSend();
		}
	}
	

	private void clickSend() {
		// 跳转到发消息页面
		Intent intent = new Intent(this, MessageActivity.class);
		GlobalParams.receiver = friend.account;
		intent.putExtra("messager", friend.account);
		startActivity(intent);
	}

	private void clickAdd() {
		// 发送邀请
		
		LogUtils.i("TCP----------FriendDetailActivity");
		Invitation invitation = new Invitation();
		invitation.account = friend.account;
		GlobalParams.receiver = friend.account;
//		GlobalParams.receiver = "A";
		invitation.agree = false;
		invitation.icon = GlobalParams.ico;
		invitation.name = GlobalParams.nickname;
		invitation.owner = GlobalParams.sender;
		
		String beanToJson = GsonTools.beanToJson(invitation);

		Request request = new TextRequest(GlobalParams.sender, GlobalParams.token, GlobalParams.receiver, beanToJson);
		ConnectorManager.getInstance().putRequest(request);
		
		Toast.makeText(this, "成功发送邀请!!!", 0).show();
	}
}
