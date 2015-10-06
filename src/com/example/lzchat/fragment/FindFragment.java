package com.example.lzchat.fragment;

import android.content.Intent;
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
import com.example.lzchat.activity.FrindcirclesActivity;
import com.example.lzchat.activity.HomeActivity;
import com.example.lzchat.activity.NearbyPeopleActivity;
import com.example.lzchat.activity.ScanActivity;
import com.example.lzchat.activity.ShakeActivity;
import com.example.lzchat.utils.SharePrefUtil;
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
		ViewUtils.inject(this,inflate);
		initView();
		System.out.println();
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
			//打开朋友圈并隐藏未读信息红点
			SharePrefUtil.saveString(getActivity(), "frindcircles", "");
			startActivity(new Intent(getActivity(), FrindcirclesActivity.class));
			((HomeActivity)getActivity()).unread_talk_count.setVisibility(View.GONE);
			find_frindcircles_unreader.setVisibility(View.GONE);
			break;

		//扫一扫
		case R.id.find_scan:
			startActivity(new Intent(getActivity(), ScanActivity.class));
			break;
		
		//摇一摇
		case R.id.find_shake:
			startActivity(new Intent(getActivity(), ShakeActivity.class));
			break;
			
		//附近的人
		case R.id.find_nearby_people:
			startActivity(new Intent(getActivity(), NearbyPeopleActivity.class));
			break;
		}
		
	}
	
}
