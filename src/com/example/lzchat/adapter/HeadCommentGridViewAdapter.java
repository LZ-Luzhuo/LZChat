package com.example.lzchat.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.lzchat.R;
import com.example.lzchat.bean.FriendMessage;

public class HeadCommentGridViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List<FriendMessage> frindlist;

	public HeadCommentGridViewAdapter(Context context,List<FriendMessage> frindlist) {
		this.context = context;
		this.frindlist = frindlist;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return 5;
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

		private ImageView mImageView;

	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.praise_gridview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.iv_head_img);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}
}
