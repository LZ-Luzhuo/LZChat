package com.example.lzchat.activity.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

//
//import com.easemob.chat.EMChatManager;
//import com.easemob.chat.EMMessage;
//import com.easemob.chat.EMMessage.Type;
//import com.easemob.util.EasyUtils;
//import com.luobo.app.fx.MainActivity;
//import com.luobo.app.utils.CommonUtils;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.NotificationCompat;
//import android.view.View;
//
public class BaseActivity extends FragmentActivity{
//
//	private static final int notifiId=11;
//	protected NotificationManager notificationManager;
	@Override
	protected void onCreate(Bundle arg0) {
//		// TODO Auto-generated method stub
		super.onCreate(arg0);
//		notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}
	@Override
	protected void onResume() {
//		// TODO Auto-generated method stub
		super.onResume();
//		EMChatManager.getInstance().activityResumed();
//		IntentFilter filter = new IntentFilter();  
//        filter.addAction("com.app.luobo.exit");  
//        this.registerReceiver(this.finishAppReceiver, filter); 
	}
	@Override
	protected void onStart() {
//		// TODO Auto-generated method stub
		super.onStart();
	}
//	/**
//	 * 返回
//	 * @param view
//	 */
//	public void back(View view){
//		finish();
//	}
//	/**
//	 * 当应用在前台的时候。如果当前消息不属于当前回话，在状态栏提示一下
//	 * @param message
//	 */
//	protected void notifyNewMessage(EMMessage message) {
//        // 如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
//        // 以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
//        if (!EasyUtils.isAppRunningForeground(this)) {
//            return;
//        }
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                this).setSmallIcon(getApplicationInfo().icon)
//                .setWhen(System.currentTimeMillis()).setAutoCancel(true);
//
//        String ticker = CommonUtils.getMessageDigest(message, this);
//        if (message.getType() == Type.TXT)
//            ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
//        // 设置状态栏提示
//        mBuilder.setTicker(message.getFrom() + ": " + ticker);
//
//        // 必须设置pendingintent，否则在2.3的机器上会有bug
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, notifiId,
//                intent, PendingIntent.FLAG_ONE_SHOT);
//        mBuilder.setContentIntent(pendingIntent);
//
//        Notification notification = mBuilder.build();
//        notificationManager.notify(notifiId, notification);
//        notificationManager.cancel(notifiId);
//    }
//	/**
//	 * 初始化控件
//	 */
//	public void initView(){
//		
//	}
//	/**
//	 * 初始化数据
//	 */
//	public void initData(){
//		
//		
//	}
//	/**
//	 * 获取数据
//	 */
//	public void getData(){
//		
//	}
//	 /** 
//     * 关闭Activity的广播，放在自定义的基类中，让其他的Activity继承这个Activity就行 
//     */  
//    protected BroadcastReceiver finishAppReceiver = new BroadcastReceiver() {  
//        @Override  
//        public void onReceive(Context context, Intent intent) {  
//            finish();  
//        }  
//    };  
//  
    @Override  
    protected void onDestroy() {   
//        super.onDestroy();  
//        this.unregisterReceiver(this.finishAppReceiver);  
    }  
//
}
