package com.example.lzchat;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.example.lzchat.utils.AsyncImageLoderUtil;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.util.Log;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-18 下午10:39:23
 * 
 * 描述:管理activity,需要在清单<application>里配置android:name=".ChatApplication"
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class ChatApplication extends Application {
	private List<Activity> activitys = new LinkedList<Activity>();
	private List<Service> services = new LinkedList<Service>();

	@Override
	public void onCreate() {
		super.onCreate();
		AsyncImageLoderUtil.init(this);
		Log.d("ChatApplication", "init");
	}

	/**
	 * 添加activity
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activitys.add(activity);
	}

	/**
	 * 移除指定activity
	 * @param activity
	 */
	public void removeActivity(Activity activity) {
		activitys.remove(activity);
	}

	/**
	 * 添加Service
	 * @param service
	 */
	public void addService(Service service) {
		services.add(service);
	}

	/**
	 * 移除指定Service
	 * @param service
	 */
	public void removeService(Service service) {
		services.remove(service);
	}

	/**
	 * 退出应用程序时,清空activity,service
	 */
	public void closeApplication() {
		closeActivitys();
		closeServices();
	}

	private void closeActivitys() {
		ListIterator<Activity> iterator = activitys.listIterator();
		while (iterator.hasNext()) {
			Activity activity = iterator.next();
			if (activity != null) {
				activity.finish();
			}
		}
		activitys.clear();
	}

	private void closeServices() {
		ListIterator<Service> iterator = services.listIterator();
		while (iterator.hasNext()) {
			Service service = iterator.next();
			if (service != null) {
				stopService(new Intent(this, service.getClass()));
			}
		}
		services.clear();
	}
}
