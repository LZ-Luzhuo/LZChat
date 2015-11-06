package com.example.lzchat.activity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.base.BaseActivity;
import com.example.lzchat.bean.Friend;
import com.example.lzchat.bean.Invitation;
import com.example.lzchat.client.ConnectorManager;
import com.example.lzchat.client.request.Request;
import com.example.lzchat.client.request.TextRequest;
import com.example.lzchat.dao.DB;
import com.example.lzchat.dao.FriendDao;
import com.example.lzchat.dao.InvitationDao;
import com.example.lzchat.utils.CommonUtil;
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
 * 创建日期:2015-10-28 上午12:19:50
 * 
 * 描述:新朋友
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class FriendNewActivity extends BaseActivity implements OnClickListener{
	private ListView listView;
	private FriendNewAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_friend_new);
		initView();
		loadData();
	}

	private void loadData() {
		String phone = GlobalParams.sender;
		InvitationDao dao = new InvitationDao(this);
		Cursor cursor = dao.queryCursor(phone);
		adapter.changeCursor(cursor);
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.friend_new_list_view);

		adapter = new FriendNewAdapter(this, null);
		listView.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View v) {
		// TODO 返回
		if (false) {
			finish();
		}
	}
	
	OnClickListener acceptListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Object o = v.getTag();
			if (o == null) {
				return;
			}

			// 更新数据库
			InvitationDao dao = new InvitationDao(getApplicationContext());
			Invitation invitation = (Invitation) o;
			invitation.agree = (true);
			dao.updateInvitation(invitation);

			// 添加到好友列表
			FriendDao friendDao = new FriendDao(getApplicationContext());
			Friend friend = friendDao.queryFriendByAccount(
					invitation.owner, invitation.account);
			if (friend == null) {
				friend = new Friend();
				friend.account = (invitation.account);
				friend.alpha = (CommonUtil.getFirstAlpha(invitation.name));
				friend.icon = (invitation.icon);
				friend.name = (invitation.name);
				friend.owner = (invitation.owner);
				friend.sort = (0);
				friendDao.addFriend(friend);
			}

			// ui更新
			adapter.changeCursor(dao.queryCursor(invitation.owner));

			// 添加接受朋友邀请的任务
			addAcceptFriendTask(invitation);
		}
	};
	
	private void addAcceptFriendTask(Invitation invitation) {
		// TODO 发送到服务器  通过未实现
		// 网络调用
		LogUtils.i("TCP----------网络调用");
		invitation.icon = GlobalParams.ico;
		invitation.name = GlobalParams.nickname;
		
		String beanToJson = GsonTools.beanToJson(invitation);

		GlobalParams.receiver = invitation.account;
//		GlobalParams.receiver = "A";
		Request request = new TextRequest(GlobalParams.sender, GlobalParams.token, GlobalParams.receiver, beanToJson);
		ConnectorManager.getInstance().putRequest(request);
		
	}
	
	private class FriendNewAdapter extends CursorAdapter {

		public FriendNewAdapter(Context context, Cursor c) {
			super(context, c);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return View.inflate(context, R.layout.item_new_friend, null);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ImageView ivIcon = (ImageView) view.findViewById(R.id.item_new_friend_icon);
			TextView tvName = (TextView) view.findViewById(R.id.item_new_friend_name);
			TextView tvAccept = (TextView) view.findViewById(R.id.item_new_friend_tv_accept);
			Button btnAccept = (Button) view.findViewById(R.id.item_new_friend_btn_accept);

			String account = cursor.getString(cursor.getColumnIndex(DB.Invitation.COLUMN_INVITATOR_ACCOUNT));
			String name = cursor.getString(cursor.getColumnIndex(DB.Invitation.COLUMN_INVITATOR_NAME));
			String icon = cursor.getString(cursor.getColumnIndex(DB.Invitation.COLUMN_INVITATOR_ICON));
			boolean agree = cursor.getInt(cursor.getColumnIndex(DB.Invitation.COLUMN_AGREE)) == 1;
			String content = cursor.getString(cursor.getColumnIndex(DB.Invitation.COLUMN_CONTENT));
			String owner = cursor.getString(cursor.getColumnIndex(DB.Invitation.COLUMN_OWNER));
			long id = cursor.getLong(cursor.getColumnIndex(DB.Invitation.COLUMN_ID));

			Invitation invitation = new Invitation();
			invitation.account = (account);
			invitation.agree = (agree);
			invitation.content = (content);
			invitation.icon = (icon);
			invitation.name = (name);
			invitation.owner = (owner);
			invitation.id = (id);

			if (!agree) {
				btnAccept.setVisibility(View.VISIBLE);
				tvAccept.setVisibility(View.GONE);
			} else {
				btnAccept.setVisibility(View.GONE);
				tvAccept.setVisibility(View.VISIBLE);
			}

			tvName.setText(name);
			
			if (!TextUtils.isEmpty(icon)) {
				BitmapUtils bitmapUtils = new BitmapUtils(FriendNewActivity.this);
				bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
				bitmapUtils.display(ivIcon, icon);
			}

			btnAccept.setOnClickListener(acceptListener);
			btnAccept.setTag(invitation);
		}
	}
}
