package com.example.lzchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lzchat.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-4 下午10:53:17
 * 
 * 描述:fragment-发现
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class FindFragment extends Fragment implements OnClickListener{
	@ViewInject(R.id.find_frindcircles)
	private LinearLayout find_frindcircles;
	@ViewInject(R.id.find_frindcircles_update)
	private ImageView find_frindcircles_update;
	@ViewInject(R.id.find_frindcircles_unreader)
	private ImageView find_frindcircles_unreader;
	@ViewInject(R.id.find_scan)
	private LinearLayout find_scan;
	@ViewInject(R.id.find_shake)
	private LinearLayout find_shake;
	@ViewInject(R.id.find_nearby_people)
	private LinearLayout find_nearby_people;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_find, null);
		initView();
		ViewUtils.inject(this,inflate);
		return inflate;
	}

	private void initView() {
		find_frindcircles.setOnClickListener(this);
		find_scan.setOnClickListener(this);
		find_shake.setOnClickListener(this);
		find_nearby_people.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//朋友圈
		case R.id.find_frindcircles:
			
			break;

		//扫一扫
		case R.id.find_scan:
			
			break;
		
		//摇一摇
		case R.id.find_shake:
			
			break;
			
		//附近的人
		case R.id.find_nearby_people:
			
			break;
		}
		
	}
	
}
