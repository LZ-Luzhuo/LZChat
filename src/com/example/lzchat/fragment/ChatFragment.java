package com.example.lzchat.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.activity.MessageActivity;
import com.example.lzchat.bean.Friend;
import com.example.lzchat.client.receiver.PushReceiver;
import com.example.lzchat.client.service.CoreService;
import com.example.lzchat.dao.DB;
import com.example.lzchat.dao.FriendDao;
import com.example.lzchat.dao.MessageDao;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.util.LogUtils;

public class ChatFragment extends Fragment implements OnItemClickListener{
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
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fra_chat, container, false);
//		setContentView(R.layout.fra_chat);
		
		listView = (ListView) view.findViewById(R.id.chat_list_view);
		listView.setOnItemClickListener(this);
		
		// 开启服务(服务里做打开连接,认证用户信息等操作) TODO 放到FragmentActivity
//		getActivity().startService(new Intent(getActivity(), CoreService.class));
		
		// 动态注册广播接收者
		IntentFilter filter = new IntentFilter();
		filter.addAction(PushReceiver.ACTION_TEXT);
		getActivity().registerReceiver(receiver, filter);
		
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(getActivity(), MessageActivity.class);
		intent.putExtra("messager", (String) view.getTag());
		startActivity(intent);
	}
	
	private void loadData() {
		if (this == null) {
			return;
		}

		// TODO 账号
		String phone = GlobalParams.sender;
		
		MessageDao dao = new MessageDao(getActivity());
		Cursor cursor = dao.queryConversation(phone);

		adapter = new ConversationAdapter(getActivity(), cursor);
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
			ImageView ivIcon = (ImageView) view.findViewById(R.id.item_converation_icon);
			// 未读信息数
			TextView tvUnread = (TextView) view.findViewById(R.id.item_converation_tv_unread);
			// 接收或发送朋友的姓名
			TextView tvName = (TextView) view.findViewById(R.id.item_converation_name);
			// 内容简要
			TextView tvContent = (TextView) view.findViewById(R.id.item_converation_content);
			// 获取数据库_内容
			String content = cursor.getString(cursor.getColumnIndex(DB.Conversation.COLUMN_CONTENT));
			// 获取未读数
			int unread = cursor.getInt(cursor.getColumnIndex(DB.Conversation.COLUMN_UNREAD));
			
			
			// 获取数据库_姓名(根据phone查找nickname),friend数据库
			String name = cursor.getString(cursor.getColumnIndex(DB.Conversation.COLUMN_ACCOUNT));
			FriendDao friendDao = new FriendDao(getActivity());
			Friend friend = friendDao.queryFriendByAccount(GlobalParams.sender, name);
			tvName.setText(friend.name);
			if(friend.icon!=null){
				BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
				bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
				bitmapUtils.display(ivIcon, friend.icon);
			}

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
//			tvName.setText(name);
			tvContent.setText(content);
			
//			if (!TextUtils.isEmpty(icon)) {
//				BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
//				bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
//				bitmapUtils.display(ivIcon, icon);
//			}

			view.setTag(name);
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroy();

		// 注销广播监听
		getActivity().unregisterReceiver(receiver);
		
		// 关闭服务(如果需要),服务器会关闭连接 TODO 放到FragmentActivity
//		getActivity().stopService(new Intent(getActivity(), CoreService.class));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}
}
