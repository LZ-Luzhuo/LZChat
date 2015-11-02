package com.example.lzchat.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Toast;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.adapter.MainListViewAdapter;
import com.example.lzchat.bean.FriendMessage;
import com.example.lzchat.bean.MessageBean;
import com.example.lzchat.bean.UserBean;
import com.example.lzchat.client.receiver.PushReceiver;
import com.example.lzchat.net.HttpClientUtil;
import com.example.lzchat.net.NetUtil;
import com.example.lzchat.utils.GsonTools;
import com.example.lzchat.utils.SharePrefUtil;
import com.example.lzchat.view.XListView;
import com.example.lzchat.view.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
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
	protected static final int SUCCESS = 0;
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
	private ProgressDialog progressDialog;
	private String beanToJson;
	
	private List<FriendMessage> frindlist;
	private MainListViewAdapter viewAdapter;
	
	/**
	 * 接收CoreService发送过来的广播
	 */
	private PushReceiver receiver = new PushReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			LogUtils.i("receive");
			if (PushReceiver.FRINDCIRCLES.equals(action)) {
				System.out.println("FrindcirclesActivity_PushReceiver");
//				loadData();
				FriendMessage friendMessage = (FriendMessage) intent.getSerializableExtra("frindcircles");
				frindlist.add(0, friendMessage);
				viewAdapter.notifyDataSetChanged();
			}
		}
	};
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				MessageBean messageBean = (MessageBean) msg.obj;
				SharePrefUtil.saveString(FrindcirclesActivity.this, "frindcache", messageBean.message);
				Gson gson = new Gson();
				frindlist = gson.fromJson(messageBean.message, new TypeToken<List<FriendMessage>>() {}.getType());
				initView2();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_frinds);
		ViewUtils.inject(this);
		
		// 动态注册广播接收者
		IntentFilter filter = new IntentFilter();
		filter.addAction(PushReceiver.FRINDCIRCLES);
		registerReceiver(receiver, filter);
		
		
		initView();
		
		// TODO 如果有缓存先把缓存用上,在去请求网络
		String frindcache = SharePrefUtil.getString(this, "frindcache", "");
		if(!frindcache.equals("")){
			Gson gson = new Gson();
			frindlist = gson.fromJson(frindcache, new TypeToken<List<FriendMessage>>() {}.getType());
			initView2();
		}
		uploadData();
	}

	/**
	 * 从网络获取数据
	 */
	private void uploadData() {
		if(!NetUtil.checkNet(this)){
			Toast.makeText(this, "请检查网络!", 0).show();
			return;
		}
		
		MessageBean message = new MessageBean();
		message.password = SharePrefUtil.getString(this, "lastPassword", "");
		message.phone_num = SharePrefUtil.getString(this, "lastphone_num", "");
		message.sign = 2;
		
		beanToJson = GsonTools.beanToJson(message);
		
		new Thread(){
			@Override
			public void run() {
				super.run();
				try{
					HttpClientUtil clientUtil = new HttpClientUtil();
					InputStream is = clientUtil.sendXml(GlobalParams.URL+GlobalParams.MESSAGE, beanToJson);
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
					MessageBean regUserBean = GsonTools.jsonToBean(json, MessageBean.class);
					if(regUserBean!=null){
						if(regUserBean.successCode==1){
							Message message = new Message();
							message.what = SUCCESS;
							message.obj = regUserBean;
							handler.sendMessage(message);
						}else{
						}
					}
				} catch (IOException e) {
					LogUtils.e("register http request error!");
					e.printStackTrace();
				}
			}
		}.start();
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
	}
	
	/**
	 * 从网络获取到数据在进行初始化
	 */
	private void initView2(){
		viewAdapter = new MainListViewAdapter(this,frindlist);
		mListView.setAdapter(viewAdapter);
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
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

}
