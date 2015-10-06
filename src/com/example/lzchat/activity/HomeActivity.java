package com.example.lzchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lzchat.ConstantValue;
import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.activity.popwindow.AddPopWindow;
import com.example.lzchat.fragment.ChatFragment;
import com.example.lzchat.fragment.ContacterFragment;
import com.example.lzchat.fragment.FindFragment;
import com.example.lzchat.fragment.ProfileFragment;
import com.example.lzchat.utils.SharePrefUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-20 下午3:42:09
 * 
 * 描述:主页的Activity;并且初始化了闲聊,通讯,发现,我的标签页;
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class HomeActivity extends FragmentActivity implements OnClickListener{
	@ViewInject(R.id.unread_msg_count)
	private TextView unread_msg_count;
	@ViewInject(R.id.unread_address_count)
	private TextView unread_address_count;
	@ViewInject(R.id.unread_talk_count)
	public TextView unread_talk_count;
	
	private Fragment[] tabFragments;
	private ChatFragment chatFragment;
	private ContacterFragment contacterFragment;
	private FindFragment findFragment;
	private ProfileFragment profileFragment;
	
	private ImageView[] tabButtons;
	@ViewInject(R.id.iv_chat)
	private ImageView iv_chat;
	@ViewInject(R.id.iv_contacter)
	private ImageView iv_contacter;
	@ViewInject(R.id.iv_find)
	private ImageView iv_find;
	@ViewInject(R.id.ib_profile)
	private ImageView ib_profile;
	private TextView[] tabButtonsTexts;
	@ViewInject(R.id.tv_chat)
	private TextView tv_chat;
	@ViewInject(R.id.tv_contacter)
	private TextView tv_contacter;
	@ViewInject(R.id.tv_find)
	private TextView tv_find;
	@ViewInject(R.id.tv_profile)
	private TextView tv_profile;
	
	@ViewInject(R.id.iv_add)
	private ImageView iv_add;
	@ViewInject(R.id.iv_search)
	private ImageView iv_search;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_home);
		ViewUtils.inject(this);
		GlobalParams.hm = this;
		// finish掉WelcomeActivity
		if(GlobalParams.ac != null)
			GlobalParams.ac.finish();
		SharePrefUtil.saveString(this, ConstantValue.LOGIN, "123");
		initView();
		
		// title的添加和搜索的单击事件
		iv_add.setOnClickListener(this);
		iv_search.setOnClickListener(this);
	}

	
	/**
	 * 初始化界面
	 */
	public void initView() {
		// 没有新信息则不显示小红点
		if(SharePrefUtil.getString(this, ConstantValue.UNREADMSGCOUNT, null) == null || SharePrefUtil.getString(this, ConstantValue.UNREADMSGCOUNT, null).equals("")){
			unread_msg_count.setVisibility(View.GONE);
		}
		// 没有未加联系人则不显示小红点
		if(SharePrefUtil.getString(this, ConstantValue.UNREADADDRESS, null) == null || SharePrefUtil.getString(this, ConstantValue.UNREADADDRESS, null).equals("")){
			unread_address_count.setVisibility(View.GONE);
		}
		// 没有朋友圈信息则不显示小红点
		if(SharePrefUtil.getString(this, ConstantValue.UNREADTALKFRIEND, null) == null || SharePrefUtil.getString(this, ConstantValue.UNREADTALKFRIEND, null).equals("")){
			unread_talk_count.setVisibility(View.GONE);
		}
		
		// 1.1初始化闲聊聊天item
		chatFragment = new ChatFragment();
		// 1.2初始化联系人item
		contacterFragment = new ContacterFragment();
		// 1.3初始化发现页面
		findFragment = new FindFragment();
		// 1.4初始化我页面
		profileFragment = new ProfileFragment();
		tabFragments = new Fragment[]{chatFragment,contacterFragment,findFragment,profileFragment};
		
		//tab按钮
		tabButtons = new ImageView[]{iv_chat,iv_contacter,iv_find,ib_profile};
		tabButtonsTexts = new TextView[]{tv_chat,tv_contacter,tv_find,tv_profile};
		
		// 第一个tab选中
		tabButtons[0].setSelected(true);
		tabButtonsTexts[0].setTextColor(0xff6189ff);
		
		// 2.将4个Fragment界面添加到fragment容器中,并隐藏除chat外的其他界面
		getSupportFragmentManager().beginTransaction()
		.add(R.id.fragment_container, chatFragment)
		.add(R.id.fragment_container, contacterFragment)
		.add(R.id.fragment_container, findFragment)
		.add(R.id.fragment_container, profileFragment)
		.hide(contacterFragment).hide(findFragment).hide(profileFragment).show(chatFragment).commit();
	}
	
	/**
	 * tab标签的点击事件
	 */
	private int index;
	private int currentTabIndex;
	public void onTabClicked(View view) {
		switch (view.getId()) {
		case R.id.re_chat:
			index = 0;
			LogUtils.i("re_chat");
			break;
		case R.id.re_contacter:
			index = 1;
			LogUtils.i("re_contacter");
			break;
		case R.id.re_find:
			LogUtils.i("re_find");
			index = 2;
			break;
		case R.id.re_profile:
			LogUtils.i("re_profile");
			index = 3;
			break;
		}

		// 如果点击的tab不是当前tab,就把当前tab隐藏了,显示被点击的tab
		if (currentTabIndex != index) {
			FragmentTransaction Transaction = getSupportFragmentManager().beginTransaction().hide(tabFragments[currentTabIndex]);
			// 相对应的fragment没有添加则将其添加
			if (!tabFragments[index].isAdded()) {
				Transaction.add(R.id.fragment_container, tabFragments[index]);
			}
			Transaction.show(tabFragments[index]).commit();
		}
		//修正按钮状态
		tabButtons[currentTabIndex].setSelected(false);
		tabButtons[index].setSelected(true);
		tabButtonsTexts[currentTabIndex].setTextColor(0xFF999999);
		tabButtonsTexts[index].setTextColor(0xff6189ff);
		currentTabIndex = index;
	}
	
	/**
	 * title的添加和搜索按钮事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add:
			AddPopWindow addPopWindow = new AddPopWindow(HomeActivity.this);
			addPopWindow.show(iv_add);
			break;
		case R.id.iv_search:
			 startActivity(new Intent(HomeActivity.this,SearchFriends.class));
			break;
		default:
			break;
		}
	}
	
}
