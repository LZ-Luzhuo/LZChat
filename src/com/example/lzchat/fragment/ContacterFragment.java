package com.example.lzchat.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.activity.FriendAddActivity;
import com.example.lzchat.activity.FriendDetailActivity;
import com.example.lzchat.activity.FriendNewActivity;
import com.example.lzchat.bean.Friend;
import com.example.lzchat.client.receiver.PushReceiver;
import com.example.lzchat.dao.DB;
import com.example.lzchat.dao.FriendDao;
import com.example.lzchat.dao.InvitationDao;
import com.lidroid.xutils.util.LogUtils;

public class ContacterFragment extends Fragment implements OnClickListener,
OnItemClickListener {
	private ImageView ivAddFriend;
	private TextView tvTitle;

	private ListView listView;
	private ContactAdapter adapter;

	private View newFriendView;
	private View newFriendViewDot;
	
	private PushReceiver pushReceiver = new PushReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			LogUtils.i("receive");
			if (PushReceiver.INVITATION.equals(action)) {
				System.out.println("MainActivity_PushReceiver");
				loadData();
			}
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fra_contact, container, false);
		initView(view);
		return view;
	}
	
	private void initView(View view) {
		ivAddFriend = (ImageView) view.findViewById(R.id.bar_add_friend);
		tvTitle = (TextView) view.findViewById(R.id.bar_title);
		listView = (ListView) view.findViewById(R.id.contact_list_view);
		tvTitle.setText("通讯录");

		View headerView = View.inflate(getActivity(),
				R.layout.layout_contact_top, null);
		listView.addHeaderView(headerView);

		newFriendView = headerView.findViewById(R.id.contact_item_new_friend);
		newFriendViewDot = headerView
				.findViewById(R.id.contact_item_new_friend_dot);

		adapter = new ContactAdapter(getActivity(), null);
		listView.setAdapter(adapter);
		
		
		ivAddFriend.setOnClickListener(this);
		newFriendView.setOnClickListener(this);

		listView.setOnItemClickListener(this);
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(PushReceiver.INVITATION);
		getActivity().registerReceiver(pushReceiver, filter);
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(pushReceiver);
	}
	
	@Override
	public void onResume() {
		super.onResume();

		loadData();
	}
	
	private void loadData() {
		if (this == null) {
			return;
		}
		
		String phone = GlobalParams.sender;

		FriendDao friendDao = new FriendDao(getActivity());
		adapter.changeCursor(friendDao.queryFriends(phone));

		InvitationDao dao = new InvitationDao(getActivity());
		boolean hasUnagree = dao.hasUnagree(phone);
		if (hasUnagree) {
			newFriendViewDot.setVisibility(View.VISIBLE);
		} else {
			newFriendViewDot.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View v) {
		if (v == ivAddFriend) {
			clickAddFriend();
		} else if (v == newFriendView) {
			clickNewFriend();
		}
	}
	
	private void clickNewFriend() {
		if (this == null) {
			return;
		}
		startActivity(new Intent(getActivity(), FriendNewActivity.class));
	}

	private void clickAddFriend() {
		if (this == null) {
			return;
		}
		startActivity(new Intent(getActivity(), FriendAddActivity.class));
	}
	
	
	private class ContactAdapter extends CursorAdapter {

		public ContactAdapter(Context context, Cursor c) {
			super(context, c);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return View.inflate(context, R.layout.item_contact, null);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView tvName = (TextView) view
					.findViewById(R.id.item_contact_name);
			ImageView ivIcon = (ImageView) view
					.findViewById(R.id.item_contact_icon);

			String name = cursor.getString(cursor
					.getColumnIndex(DB.Friend.COLUMN_NAME));

			long id = cursor.getLong(cursor
					.getColumnIndex(DB.Friend.COLUMN_ID));
			String account = cursor.getString(cursor
					.getColumnIndex(DB.Friend.COLUMN_ACCOUNT));
			String alpha = cursor.getString(cursor
					.getColumnIndex(DB.Friend.COLUMN_ALPHA));
			String area = cursor.getString(cursor
					.getColumnIndex(DB.Friend.COLUMN_AREA));
			String icon = cursor.getString(cursor
					.getColumnIndex(DB.Friend.COLUMN_ICON));
			String nickName = cursor.getString(cursor
					.getColumnIndex(DB.Friend.COLUMN_NICKNAME));
			String owner = cursor.getString(cursor
					.getColumnIndex(DB.Friend.COLUMN_OWNER));
			int sex = cursor.getInt(cursor
					.getColumnIndex(DB.Friend.COLUMN_SEX));
			String sign = cursor.getString(cursor
					.getColumnIndex(DB.Friend.COLUMN_SIGN));
			int sort = cursor.getInt(cursor
					.getColumnIndex(DB.Friend.COLUMN_SORT));
			Friend friend = new Friend();
			friend.account = (account);
			friend.alpha = (alpha);
			friend.area = (area);
			friend.icon = (icon);
			friend.id = (id);
			friend.name = (name);
			friend.nickName = (nickName);
			friend.owner = (owner);
			friend.sex = (sex);
			friend.sign = (sign);
			friend.sort = (sort);

			tvName.setText(name);

			System.out.println("icon : " + icon);
			ivIcon.setImageResource(R.drawable.default_icon_user);
			if (!TextUtils.isEmpty(icon)) {
				Bitmap bitmap = BitmapFactory.decodeFile(icon);
				if (bitmap != null) {
					ivIcon.setImageBitmap(bitmap);
				}
			}

			view.setTag(friend);
		}
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Object tag = view.getTag();

		if (tag != null && tag instanceof Friend) {
			Friend friend = (Friend) tag;

			Intent intent = new Intent(getActivity(), FriendDetailActivity.class);
			intent.putExtra(FriendDetailActivity.KEY_ENTER,FriendDetailActivity.ENTER_CONTACT);
			intent.putExtra(FriendDetailActivity.KEY_DATA, friend);
			startActivity(intent);
		}
	}
	
}
