package com.example.lzchat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lzchat.R;

public class CommentListViewAdapter extends BaseAdapter {

	private static final String TAG = "CommentListViewAdapter";
	private LayoutInflater inflater;
	private Context context;
	private String text;
	private int number;
	private String str;

	public CommentListViewAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return 3;
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

		private TextView mTextView;

	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater
					.inflate(R.layout.listview_comment_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mTextView = (TextView) convertView
					.findViewById(R.id.tv_comment_text);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		text = "Mcdull:����ǵͷ��ճ����д�����ʶ�Ŷ���̿�����ڷ���ʮ���V��M�ڴ�v�����ӵľ������ɽ��Ʒ����ڿ�SD��������ȿ־巳����";
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			str = String.valueOf(ch);
			if (str.equals(":")) {
				number = i;
				break;
			}
		}

		SpannableStringBuilder builder = new SpannableStringBuilder(text);

		// ForegroundColorSpan Ϊ����ǰ��ɫ��BackgroundColorSpanΪ���ֱ���ɫ
		ForegroundColorSpan redSpan = new ForegroundColorSpan(
				Color.parseColor("#336699"));
		builder.setSpan(redSpan, 0, number, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		viewHolder.mTextView.setText(builder);
		return convertView;
	}
}
