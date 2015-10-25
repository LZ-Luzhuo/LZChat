package com.example.lzchat.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lzchat.R;
import com.example.lzchat.adapter.MainListViewAdapter;
import com.example.lzchat.utils.SharePrefUtil;
import com.example.lzchat.view.XListView;
import com.example.lzchat.view.XListView.IXListViewListener;
import com.lidroid.xutils.BitmapUtils;
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
public class FrindcirclesActivity extends Activity implements IXListViewListener, OnClickListener {
	@ViewInject(R.id.personal_return)
	private ImageView personal_return;
	@ViewInject(R.id.personal_publish)
	private ImageButton personal_publish;
	@ViewInject(R.id.mListView)
	private XListView mListView;
	@ViewInject(R.id.pb_head)
	private ProgressBar pb_head;
	
	@ViewInject(R.id.mReplaceBackground)
	private RelativeLayout mReplaceBackground;
	@ViewInject(R.id.iv_head_img)
	private ImageView iv_head_img;
	@ViewInject(R.id.nickname)
	private TextView nickname;
	
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_frinds);
		ViewUtils.inject(this);
		initView();
		
	}

	private void initView() {
		personal_return.setOnClickListener(this);
		personal_publish.setOnClickListener(this);
		
		mHandler = new Handler();
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View addHeaderView = inflater.inflate(R.layout.head_listview, null);
		ViewUtils.inject(this,addHeaderView);
		
		String avatar = SharePrefUtil.getString(this, "avatar", "");
		if(!avatar.equals("")){
			BitmapUtils bitmapUtils = new BitmapUtils(this);
			bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
			bitmapUtils.display(iv_head_img, avatar);
		}
		String lastnickname = SharePrefUtil.getString(this, "lastnickname", "");
		if(!lastnickname.equals(""))
			nickname.setText(lastnickname);
		
		mReplaceBackground.setOnClickListener(this);
		mListView.addHeaderView(addHeaderView);
		mListView.setAdapter(new MainListViewAdapter(this));
		mListView.setXListViewListener(this);
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
	
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
	}
	
	@Override
	public void onRefresh() {

		pb_head.setVisibility(View.VISIBLE);
		
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				pb_head.setVisibility(View.GONE);
				onLoad();
			}
		}, 2000);
	}
	
	@Override
	public void onLoadMore() {

	}

}
