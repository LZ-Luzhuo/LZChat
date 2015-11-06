package com.example.lzchat.activity;



import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.base.BaseActivity;
import com.example.lzchat.bean.Friend;
import com.example.lzchat.bean.Message;
import com.example.lzchat.client.ConnectorManager;
import com.example.lzchat.client.receiver.PushReceiver;
import com.example.lzchat.client.request.Request;
import com.example.lzchat.client.request.TextRequest;
import com.example.lzchat.dao.DB;
import com.example.lzchat.dao.FriendDao;
import com.example.lzchat.dao.MessageDao;
import com.example.lzchat.utils.CommonUtil;
import com.example.lzchat.utils.GsonTools;
import com.example.lzchat.utils.SharePrefUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.util.LogUtils;

public class MessageActivity extends BaseActivity implements OnClickListener, TextWatcher{
	private String messager;
	private ListView listView;
	private MessageAdapter adapter;

	private Button btnSend;
	private EditText etContent;
	private ImageView iv_back;
	private TextView nickname;
	
	/**
	 * 接收CoreService发送过来的广播
	 */
	private PushReceiver receiver = new PushReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			LogUtils.i("receive");
			if (PushReceiver.ACTION_TEXT.equals(action)) {
				System.out.println("MessageActivity_PushReceiver");
				loadData();
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_message);
		messager = getIntent().getStringExtra("messager");
		
		// 动态注册广播接收者
		IntentFilter filter = new IntentFilter();
		filter.addAction(PushReceiver.ACTION_TEXT);
		registerReceiver(receiver, filter);
		
		initView();
		loadData();
	}


	private void initView() {
		listView = (ListView) findViewById(R.id.message_list_view);
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		btnSend = (Button) findViewById(R.id.message_btn_send);
		etContent = (EditText) findViewById(R.id.message_et_content);
		btnSend.setEnabled(false);
		nickname = (TextView) findViewById(R.id.nickname);
		if(!TextUtils.isEmpty(messager)){
			FriendDao friendDao = new FriendDao(this);
			Friend friend = friendDao.queryFriendByAccount(GlobalParams.sender, messager);
			nickname.setText(friend.name);
		}
		
		adapter = new MessageAdapter(this, null);
		listView.setAdapter(adapter);
		
		btnSend.setOnClickListener(this);
		etContent.addTextChangedListener(this);
		iv_back.setOnClickListener(this);
		
	}
	
	private void loadData() {
		String phone = GlobalParams.sender;

		MessageDao dao = new MessageDao(this);
		final Cursor cursor = dao.queryMessage(phone, messager);
		adapter.changeCursor(cursor);
		listView.post(new Runnable() {
			@SuppressLint("NewApi")
			@Override
			public void run() {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					listView.smoothScrollToPositionFromTop(cursor.getCount(), 0);
				} else {
					listView.smoothScrollToPosition(cursor.getCount());
				}
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();

		String phone = GlobalParams.sender;
		MessageDao dao = new MessageDao(this);
		dao.clearUnread(phone, messager);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	@Override
	public void onClick(View v) {
		// TODO 返回
		if (v == iv_back) {
			finish();
		} else if (v == btnSend) {
			clickSend();
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
		String content = etContent.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			btnSend.setEnabled(false);
		} else {
			btnSend.setEnabled(true);
		}
	}
	
	// 发送消息
	private void clickSend() {
		String content = etContent.getText().toString().trim();
		String phone = GlobalParams.sender;

		// 存储到本地
		final MessageDao dao = new MessageDao(this);
		final Message msg = new Message();
		msg.account = (messager);
		msg.content = (content);
		msg.createTime = (System.currentTimeMillis());
		msg.direction = (0);
		msg.owner = (phone);
		msg.read = (true);
		msg.state = (1);
		msg.type = (0);
		dao.addMessage(msg);
		// 更新ui
		loadData();

		etContent.setText("");

		// 网络调用
		LogUtils.i("TCP----------网络调用");
		msg.url = GlobalParams.ico;
		String beanToJson = GsonTools.beanToJson(msg);

		Request request = new TextRequest(GlobalParams.sender, GlobalParams.token, messager, beanToJson);
		ConnectorManager.getInstance().putRequest(request);
		
		Toast.makeText(getApplicationContext(), "发送成功", 0).show();
		msg.state = (2);
		dao.updateMessage(msg);
		// 更新ui
		loadData();
	}
	
	
	private class MessageAdapter extends CursorAdapter {

		public MessageAdapter(Context context, Cursor c) {
			super(context, c);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return View.inflate(context, R.layout.item_message, null);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView tvTime = (TextView) view.findViewById(R.id.item_message_tv_time);
			View senderView = view.findViewById(R.id.item_message_sender);
			View receiverView = view.findViewById(R.id.item_message_receiver);

			int direction = cursor.getInt(cursor.getColumnIndex(DB.Message.COLUMN_DIRECTION));
			long createTime = cursor.getLong(cursor.getColumnIndex(DB.Message.COLUMN_CREATE_TIME));
			tvTime.setText(CommonUtil.getDateFormat(createTime));

			if (direction == 0) {
				// 发送
				senderView.setVisibility(View.VISIBLE);
				receiverView.setVisibility(View.GONE);

				ImageView senderIconView = (ImageView) view.findViewById(R.id.item_message_sender_icon);
				TextView senderContentView = (TextView) view.findViewById(R.id.item_message_sender_tv_content);
				ProgressBar pbLoading = (ProgressBar) view.findViewById(R.id.item_message_sender_pb_state);
				ImageView faildView = (ImageView) view.findViewById(R.id.item_message_sender_iv_faild);

				senderContentView.setText(cursor.getString(cursor.getColumnIndex(DB.Message.COLUMN_CONTENT)));

				int state = cursor.getInt(cursor.getColumnIndex(DB.Message.COLUMN_STATE));
				
				String avatar = SharePrefUtil.getString(MessageActivity.this, "avatar", "");
				if(!avatar.equals("")){
					senderIconView.setImageURI(Uri.fromFile(new File(avatar)));
				}

				// 1.正在发送 2.已经成功发送 3.发送失败
				if (state == 1) {
					pbLoading.setVisibility(View.VISIBLE);
					faildView.setVisibility(View.GONE);
				} else if (state == 2) {
					pbLoading.setVisibility(View.GONE);
					faildView.setVisibility(View.GONE);
				} else {
					pbLoading.setVisibility(View.GONE);
					faildView.setVisibility(View.VISIBLE);
				}

			} else {
				// 接收
				senderView.setVisibility(View.GONE);
				receiverView.setVisibility(View.VISIBLE);

				ImageView receiverIconView = (ImageView) view.findViewById(R.id.item_message_receiver_icon);
				TextView receiverContentView = (TextView) view.findViewById(R.id.item_message_receiver_tv_content);

				receiverContentView.setText(cursor.getString(cursor.getColumnIndex(DB.Message.COLUMN_CONTENT)));
				String icon = cursor.getString(cursor.getColumnIndex(DB.Message.COLUMN_URL));
				if (!TextUtils.isEmpty(icon)) {
					BitmapUtils bitmapUtils = new BitmapUtils(MessageActivity.this);
					bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
					bitmapUtils.display(receiverIconView, icon);
				}
			}
		}
	}
}
