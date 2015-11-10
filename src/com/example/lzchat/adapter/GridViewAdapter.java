package com.example.lzchat.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap.Config;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.lzchat.R;
import com.example.lzchat.bean.FriendMessage;
import com.example.lzchat.utils.AsyncImageLoderUtil;
import com.lidroid.xutils.BitmapUtils;

public class GridViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;
	FriendMessage message;

	public GridViewAdapter(Context context, FriendMessage message) {
		this.message = message;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if(message.messageImage==null){
			return 0;
		}
		if (message.messageImage.length > 9) {
			return 9;
		} else {
			return message.messageImage.length;
		}
	}

	@Override
	public Object getItem(int arg0) {
		return (message.messageImage != null ? (message.messageImage.length>9 ? 9: message.messageImage[arg0] ) : null);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public class ViewHolder {

		private ImageView mImageView;

	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.img_gridview_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.iv_gridview_img);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		AsyncImageLoderUtil.getInstance().displayListItemImage(message.messageImage[arg0], viewHolder.mImageView);
		
		return convertView;
	}

}
