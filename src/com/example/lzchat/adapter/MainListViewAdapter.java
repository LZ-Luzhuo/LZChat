package com.example.lzchat.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.bean.Friend;
import com.example.lzchat.bean.FriendMessage;
import com.example.lzchat.dao.FriendDao;
import com.example.lzchat.utils.SharePrefUtil;
import com.example.lzchat.view.CommentListView;
import com.example.lzchat.view.GridView;
import com.lidroid.xutils.BitmapUtils;

public class MainListViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<FriendMessage> frindlist;

	public MainListViewAdapter(Context context,List frindlist) {
		this.context = context;
		this.frindlist = frindlist;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return frindlist.size();
	}

	@Override
	public Object getItem(int arg0) {

		return null;
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	public class ViewHolder {

		private GridView mImgGridView;
		private CommentListView mListView;
		private ImageView photo;
		private TextView nickname;
		private TextView message;
		private TextView address;
		private TextView time;
		private LinearLayout dianzan;
		private TextView dianzhan_name;

	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_main_item, null);
			viewHolder = new ViewHolder();

			viewHolder.dianzhan_name = (TextView)convertView.findViewById(R.id.dianzhan_name);
			viewHolder.dianzan = (LinearLayout)convertView.findViewById(R.id.dianzan);
			viewHolder.mImgGridView = (GridView) convertView.findViewById(R.id.gv_listView_main_gridView);
			viewHolder.mListView = (CommentListView) convertView.findViewById(R.id.lv_item_listView);
			// 头像
			viewHolder.photo = (ImageView)convertView.findViewById(R.id.photo);
			// 昵称
			viewHolder.nickname = (TextView)convertView.findViewById(R.id.nickname);
			// 消息
			viewHolder.message = (TextView)convertView.findViewById(R.id.tv_message);
			// 地区
			viewHolder.address = (TextView)convertView.findViewById(R.id.address);
			// 发布时间
			viewHolder.time = (TextView)convertView.findViewById(R.id.time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		FriendMessage message = frindlist.get(arg0);

		viewHolder.mImgGridView.setVisibility(View.VISIBLE);
		
		viewHolder.mImgGridView.setAdapter(new GridViewAdapter(context, message));
		viewHolder.mListView.setAdapter(new CommentListViewAdapter(context,message));
		
		if(message.photo.equals("123")){
			// 从本地获取图片
			// 是自己
			if(message.phone_num.equals(GlobalParams.sender)){
				String avatar = SharePrefUtil.getString(context, "avatar", "");
				if(!avatar.equals("")){
					BitmapUtils bitmapUtils = new BitmapUtils(context);
					bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
					bitmapUtils.display(viewHolder.photo, avatar);
				}else{
					//如果缓存内没有头像,则设置默认头像
					viewHolder.photo.setImageResource(R.drawable.profile_touxiang);
				}
			}else{
				FriendDao dao = new FriendDao(context);
				Friend friend = dao.queryFriendByAccount(GlobalParams.sender, message.phone_num);
				if(friend!=null){
					BitmapUtils bitmapUtils = new BitmapUtils(context);
					bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
					bitmapUtils.display(viewHolder.photo, friend.icon);
				}else{
					// 如果没有头像或不是朋友关系,则设置默认头像
					viewHolder.photo.setImageResource(R.drawable.profile_touxiang);
				}
			}
		}else{
			if(!TextUtils.isEmpty(message.photo)){
				BitmapUtils bitmapUtils = new BitmapUtils(context);
				bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
				bitmapUtils.display(viewHolder.photo, message.photo);
			}else{
				// 没有头像则设置默认头像
				viewHolder.photo.setImageResource(R.drawable.profile_touxiang);
			}
		}
		
		if(!TextUtils.isEmpty(message.nickname)){
			viewHolder.nickname.setText(message.nickname);
		}else{
			viewHolder.nickname.setText("神秘人");
		}
		
		if(!TextUtils.isEmpty(message.message)){
			viewHolder.message.setText(message.message);
		}else{
			viewHolder.message.setText("神秘内容");
		}
		
		if(!TextUtils.isEmpty(message.address)){
			viewHolder.address.setText(message.address);
		}else{
			viewHolder.address.setText("杭州");
		}
		
		if(!TextUtils.isEmpty(message.time)){
			viewHolder.time.setText(message.time);
		}else{
			viewHolder.time.setText("刚刚");
		}
		
		if(message.good!=null){
			viewHolder.dianzan.setVisibility(View.VISIBLE);
			String str = "";
			for (int i = 0; i < message.good.length; i++) {
				if(i==0){
					str += message.good[0];
				}else{
					str += (","+message.good[i]);
				}
			}
			viewHolder.dianzhan_name.setText(str);
		}else{
			viewHolder.dianzan.setVisibility(View.GONE);
		}

		return convertView;
	}
}
