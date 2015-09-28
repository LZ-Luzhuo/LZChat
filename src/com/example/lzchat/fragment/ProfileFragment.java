package com.example.lzchat.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lzchat.R;
import com.example.lzchat.activity.PersonalActivity;
import com.example.lzchat.activity.SettingActivity;
import com.example.lzchat.utils.SharePrefUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-28 下午10:29:27
 * 
 * 描述:'我'-fragment
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class ProfileFragment extends Fragment implements OnClickListener{
	@ViewInject(R.id.profile_collect_layout)
	private LinearLayout profile_collect_layout;
	@ViewInject(R.id.profile_wallet_layout)
	private LinearLayout profile_wallet_layout;
	@ViewInject(R.id.profile_card_layout)
	private LinearLayout profile_card_layout;
	@ViewInject(R.id.profile_setting_layout)
	private LinearLayout profile_setting_layout;
	@ViewInject(R.id.profile_personal_layout)
	private LinearLayout profile_personal_layout;
	
	@ViewInject(R.id.profile_phonenumber)
	private TextView profile_phonenumber;
	@ViewInject(R.id.profile_nickname)
	private TextView profile_nickname;
	@ViewInject(R.id.profile_photo)
	private ImageView profile_photo;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.fragment_profile, null);
		ViewUtils.inject(this,inflate);
		initView();
		return inflate;
	}

	private void initView() {
		profile_collect_layout.setOnClickListener(this);
		profile_wallet_layout.setOnClickListener(this);
		profile_card_layout.setOnClickListener(this);
		profile_setting_layout.setOnClickListener(this);
		profile_personal_layout.setOnClickListener(this);
		
		profile_phonenumber.setText(SharePrefUtil.getString(getActivity(), "lastphone_num", ""));
		profile_nickname.setText(SharePrefUtil.getString(getActivity(), "lastnickname", ""));
		
		String avatar = SharePrefUtil.getString(getActivity(), "avatar", "");
		if(avatar != ""){
			BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
			bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
			bitmapUtils.display(profile_photo, avatar);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.profile_personal_layout:
			startActivityForResult(new Intent(getActivity(), PersonalActivity.class), 5);
			break;
		case R.id.profile_setting_layout:
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
			switch (requestCode) {
			case 5:
				String avatar = SharePrefUtil.getString(getActivity(), "avatar", "");
				if(avatar != ""){
					BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
					bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
					bitmapUtils.display(profile_photo, avatar);
				}
				break;
			}
		}
	}
}
