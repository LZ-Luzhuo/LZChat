package com.example.lzchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lzchat.activity.MessageActivity;
import com.example.lzchat.client.receiver.PushReceiver;
import com.example.lzchat.client.service.CoreService;
import com.example.lzchat.dao.DB;
import com.example.lzchat.dao.MessageDao;
import com.lidroid.xutils.util.LogUtils;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-23 上午12:23:28
 * 
 * 描述:关于长连接的案列,在子线程中进行阻塞,效率低,适合少量用户的链接.如果用户量大,可以考虑使用mina的nio(非阻塞).
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class MainActivity extends Activity implements OnItemClickListener {
	private ListView listView;
	private ConversationAdapter adapter;
	
	/**
	 * 接收CoreService发送过来的广播
	 */
	private PushReceiver receiver = new PushReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			LogUtils.i("receive");
			if (PushReceiver.ACTION_TEXT.equals(action)) {
				System.out.println("MainActivity_PushReceiver");
				loadData();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fra_chat);
		
		listView = (ListView) findViewById(R.id.chat_list_view);
		listView.setOnItemClickListener(this);
		
		// 开启服务(服务里做打开连接,认证用户信息等操作) TODO 放到FragmentActivity
		startService(new Intent(this, CoreService.class));
		
		// 动态注册广播接收者
		IntentFilter filter = new IntentFilter();
		filter.addAction(PushReceiver.ACTION_TEXT);
		registerReceiver(receiver, filter);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, MessageActivity.class);
		intent.putExtra("messager", (String) view.getTag());
		startActivity(intent);
	}
	
	private void loadData() {
		if (this == null) {
			return;
		}

		// TODO 账号
		String phone = GlobalParams.sender;
		
		MessageDao dao = new MessageDao(this);
		Cursor cursor = dao.queryConversation(phone);

		adapter = new ConversationAdapter(this, cursor);
		listView.setAdapter(adapter);

	}
	
	private class ConversationAdapter extends CursorAdapter {

		public ConversationAdapter(Context context, Cursor c) {
			super(context, c);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return View.inflate(context, R.layout.item_conversation, null);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// 未读信息数
			TextView tvUnread = (TextView) view.findViewById(R.id.item_converation_tv_unread);
			// 接收或发送朋友的姓名
			TextView tvName = (TextView) view.findViewById(R.id.item_converation_name);
			// 内容简要
			TextView tvContent = (TextView) view.findViewById(R.id.item_converation_content);
			// 获取数据库_姓名
			String name = cursor.getString(cursor.getColumnIndex(DB.Conversation.COLUMN_ACCOUNT));
			// 获取数据库_内容
			String content = cursor.getString(cursor.getColumnIndex(DB.Conversation.COLUMN_CONTENT));
			// 获取未读数
			int unread = cursor.getInt(cursor.getColumnIndex(DB.Conversation.COLUMN_UNREAD));

			if (unread <= 0) {
				tvUnread.setVisibility(View.GONE);
				tvUnread.setText("");
			} else {
				if (unread >= 99) {
					tvUnread.setText("99");
				} else {
					tvUnread.setText("" + unread);
				}
				tvUnread.setVisibility(View.VISIBLE);
			}
			tvName.setText(name);
			tvContent.setText(content);

			view.setTag(name);
		}
	}
	
//	public void sendMessage(View v){
//		LogUtils.i("TCP----------");
//		
//		final String content = et.getText().toString().trim();
//		if (TextUtils.isEmpty(content)) {
//			return;
//		}
//
//		Request request = new TextRequest(GlobalParams.sender, GlobalParams.token, GlobalParams.receiver, content);
//		ConnectorManager.getInstance().putRequest(request);
//	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		// 注销广播监听
		unregisterReceiver(receiver);
		
		// 关闭服务(如果需要),服务器会关闭连接 TODO 放到FragmentActivity
		stopService(new Intent(this, CoreService.class));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}
	
	
	
	
	
	
	
	public void addFrinds(View v){
		// TODO 打开添加朋友,这是测试代码
		Intent intent = new Intent(this, ContactFragment.class);
		startActivity(intent);
	}
}
