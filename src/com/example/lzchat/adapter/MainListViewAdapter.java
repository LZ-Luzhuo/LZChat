package com.example.lzchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.lzchat.R;
import com.example.lzchat.view.CommentListView;
import com.example.lzchat.view.GridView;

public class MainListViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private Context context;

	public MainListViewAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return 10;
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

		private GridView mGridView;
		private GridView mImgGridView;
		private CommentListView mListView;

	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listview_main_item, null);
			viewHolder = new ViewHolder();

			viewHolder.mGridView = (GridView) convertView
					.findViewById(R.id.gv_comment_head);
			viewHolder.mImgGridView = (GridView) convertView
					.findViewById(R.id.gv_listView_main_gridView);
			viewHolder.mListView = (CommentListView) convertView
					.findViewById(R.id.lv_item_listView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.mGridView.setVisibility(View.VISIBLE);
		viewHolder.mImgGridView.setVisibility(View.VISIBLE);
		viewHolder.mGridView
				.setAdapter(new HeadCommentGridViewAdapter(context));
		viewHolder.mImgGridView.setAdapter(new GridViewAdapter(context, arg0));
		viewHolder.mListView.setAdapter(new CommentListViewAdapter(context));

		return convertView;
	}
}
