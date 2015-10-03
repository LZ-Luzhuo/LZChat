package com.example.lzchat.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

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
 * 创建日期:2015-9-30 上午11:41:21
 * 
 * 描述:卡片状的活动会话框,点击不同的卡片实现打开不同的界面
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class ActionSheetDialog {
	@ViewInject(R.id.scroll_content)
	private ScrollView scroll_content;
	@ViewInject(R.id.linear_content)
	private LinearLayout linear_content;
	@ViewInject(R.id.tv_title)
	private TextView tv_title;
	@ViewInject(R.id.tv_cancel)
	private TextView tv_cancel;
	
	private Context context;
	private Display display;
	private Dialog dialog;
	private boolean showTitle = false;
	private List<SheetItem> sheetItemList;

	public ActionSheetDialog(Context context) {
		this.context = context;
		display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		View view = LayoutInflater.from(context).inflate(R.layout.view_actionsheet, null);
		ViewUtils.inject(this,view);
		view.setMinimumWidth(display.getWidth());
		
		// 取消
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
		// 设置会话框布局和参数
		dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
		dialog.setContentView(view);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
		layoutParams.x = 0;
		layoutParams.y = 0;
		dialogWindow.setAttributes(layoutParams);
	}

	/**
	 * 设置标题
	 * @param title 标题
	 * @return ActionSheetDialog
	 */
	public ActionSheetDialog setTitle(String title) {
		showTitle = true;
		tv_title.setVisibility(View.VISIBLE);
		tv_title.setText(title);
		return this;
	}

	/**
	 * 点击ActionSheetDialog以外区域是否关闭,默认关闭;不能相应返回键,若需要可考虑用setCanceledOnTouchOutside().
	 * @param cancel true:关闭,false:不关闭
	 * @return ActionSheetDialog
	 */
	public ActionSheetDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	/**
	 * touch到ActionSheetDialog以外区域是否关闭,关闭
	 * @param cancel true:关闭,false:不关闭
	 * @return
	 */
	public ActionSheetDialog setCanceledOnTouchOutside(boolean cancel) {
		dialog.setCanceledOnTouchOutside(cancel);
		return this;
	}

	/**
	 * 添加卡片
	 * @param strItem 卡片名称
	 * @param color 条目字体颜色，null:默认蓝色
	 * @param listener 卡片点击监听
	 * @return ActionSheetDialog
	 */
	public ActionSheetDialog addSheetItem(String strItem, SheetItemColor color,OnSheetItemClickListener listener) {
		if (sheetItemList == null) {
			sheetItemList = new ArrayList<SheetItem>();
		}
		sheetItemList.add(new SheetItem(strItem, color, listener));
		return this;
	}

	/**
	 * 卡片点击监听
	 * @author Luzhuo
	 *
	 */
	public interface OnSheetItemClickListener {
		void onClick(int which);
	}
	
	/**
	 * 一个卡片的属性
	 * @author Luzhuo
	 *
	 */
	public class SheetItem {
		String name;
		OnSheetItemClickListener itemClickListener;
		SheetItemColor color;

		public SheetItem(String name, SheetItemColor color,OnSheetItemClickListener itemClickListener) {
			this.name = name;
			this.color = color;
			this.itemClickListener = itemClickListener;
		}
	}
	
	/**
	 * 显示卡片
	 */
	public void show() {
		setSheetItems();
		dialog.show();
	}
	
	/**
	 * 卡片排版
	 */
	private void setSheetItems() {
		if (sheetItemList == null || sheetItemList.size() <= 0) {
			return;
		}

		int size = sheetItemList.size();

		// 添加条目过多的时候控制高度
		if (size >= 7) {
			LinearLayout.LayoutParams params = (LayoutParams) scroll_content.getLayoutParams();
			params.height = display.getHeight() / 2;
			scroll_content.setLayoutParams(params);
		}

		// 循环添加条目
		for (int i = 1; i <= size; i++) {
			final int index = i;
			SheetItem sheetItem = sheetItemList.get(i - 1);
			String strItem = sheetItem.name;
			SheetItemColor color = sheetItem.color;
			final OnSheetItemClickListener listener = (OnSheetItemClickListener) sheetItem.itemClickListener;

			TextView textView = new TextView(context);
			textView.setText(strItem);
			textView.setTextSize(18);
			textView.setGravity(Gravity.CENTER);

			// 背景图片
			if (size == 1) {
				if (showTitle) {
					textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
				} else {
					textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
				}
			} else {
				if (showTitle) {
					if (i >= 1 && i < size) {
						textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				} else {
					if (i == 1) {
						textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
					} else if (i < size) {
						textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				}
			}

			// 字体颜色,颜色为空设置蓝色
			if (color == null) {
				textView.setTextColor(Color.parseColor(SheetItemColor.Blue.getName()));
			} else {
				textView.setTextColor(Color.parseColor(color.getName()));
			}

			// 高度
			float scale = context.getResources().getDisplayMetrics().density;
			int height = (int) (45 * scale + 0.5f);
			textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height));

			// 点击事件
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onClick(index);
					dialog.dismiss();
				}
			});
			
			linear_content.addView(textView);
		}
	}

	/**
	 * 卡片颜色,这里只有Blue,Red色,需要其他颜色需要修改代码添加
	 * @author Luzhuo
	 *
	 */
	public enum SheetItemColor {
		Blue("#037BFF"), Red("#FD4A2E");
		
		private String name;

		private SheetItemColor(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
