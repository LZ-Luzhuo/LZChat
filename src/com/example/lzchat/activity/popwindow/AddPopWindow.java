package com.example.lzchat.activity.popwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.lzchat.R;
import com.example.lzchat.activity.AddFriendsActivity;
import com.example.lzchat.activity.AddGroupActivity;
import com.example.lzchat.activity.Feedback;
import com.example.lzchat.activity.FriendAddActivity;
import com.example.lzchat.activity.ScanActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-22 下午10:23:01
 * 
 * 描述:点击HomeActivity上title的添加按钮弹出的PopWindow
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class AddPopWindow extends PopupWindow implements OnClickListener{
	private View inflate;
	private Activity context;
	
	@ViewInject(R.id.add_group)
	private RelativeLayout add_group;
	@ViewInject(R.id.add_friend)
	private RelativeLayout add_friend;
	@ViewInject(R.id.add_scan)
	private RelativeLayout add_scan;
	@ViewInject(R.id.add_feedback)
	private RelativeLayout add_feedback;

	public AddPopWindow(Activity context) {
		this.context = context;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflate = inflater.inflate(R.layout.title_pop_add, null);
		
		ViewUtils.inject(this,inflate);

		this.setContentView(inflate);
		this.setWidth(LayoutParams.WRAP_CONTENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.update();
		this.setBackgroundDrawable(new ColorDrawable(0000000000));
		
		add_group.setOnClickListener(this);
		add_friend.setOnClickListener(this);
		add_scan.setOnClickListener(this);
		add_feedback.setOnClickListener(this);
		
	}

	/**
	 * 展示pop
	 * @param view
	 */
	public void show(View view) {
        if (!this.isShowing()) {
            this.showAsDropDown(view, 0, 0);
        } else {
            this.dismiss();
        }
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_group:
			context.startActivity(new Intent(context,AddGroupActivity.class));  
            AddPopWindow.this.dismiss();
			break;
			
		case R.id.add_friend:
			context.startActivity(new Intent(context,FriendAddActivity.class));  
            AddPopWindow.this.dismiss();
			break;
			
		case R.id.add_scan:
			context.startActivity(new Intent(context,ScanActivity.class));  
            AddPopWindow.this.dismiss();
			break;
			
		case R.id.add_feedback:
			context.startActivity(new Intent(context,Feedback.class));  
            AddPopWindow.this.dismiss();
			break;
		}
	}
}
