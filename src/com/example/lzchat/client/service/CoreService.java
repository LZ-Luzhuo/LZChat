package com.example.lzchat.client.service;

import android.content.Intent;
import android.os.IBinder;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.base.BaseService;
import com.example.lzchat.bean.Friend;
import com.example.lzchat.bean.FriendMessage;
import com.example.lzchat.bean.GlobalBean;
import com.example.lzchat.bean.Invitation;
import com.example.lzchat.bean.Message;
import com.example.lzchat.bean.MessageBean;
import com.example.lzchat.client.ConnectorManager;
import com.example.lzchat.client.ConnectorManager.ConnectorListener;
import com.example.lzchat.client.receiver.PushReceiver;
import com.example.lzchat.client.request.AuthRequest;
import com.example.lzchat.dao.FriendDao;
import com.example.lzchat.dao.InvitationDao;
import com.example.lzchat.dao.MessageDao;
import com.example.lzchat.utils.Base64Coder;
import com.example.lzchat.utils.CommonUtil;
import com.example.lzchat.utils.GsonTools;
import com.lidroid.xutils.util.LogUtils;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-21 下午11:13:16
 * 
 * 描述:长连接服务,
 * 		需配置清单文件:<service android:name=".service.CoreService"/>
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class CoreService extends BaseService implements ConnectorListener {
	private ConnectorManager connectorManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		connectorManager = ConnectorManager.getInstance();

		new Thread(new Runnable() {

			@Override
			public void run() {
				connectorManager.setConnectorListener(CoreService.this);

				//连接,发送认证信息
				AuthRequest request = new AuthRequest(GlobalParams.sender, GlobalParams.token);
				connectorManager.connnect(request);
			}
		}).start();
	}

	/**
	 * 将收到的数据发送广播
	 */
	@Override
	public void pushData(String data) {
		// 获得Connector从服务器获得的信息
		LogUtils.i("coreService_data : " + data);
		data = Base64Coder.decodeString(data);
		
		
		// 将Json转成bean
		GlobalBean jsonToBean = GsonTools.jsonToBean(data, GlobalBean.class);
		
		
		// 将bean分成特定信息
		if(jsonToBean.state == 1){ // 接收信息
			if(jsonToBean.CircleFriends == true){
				// 是朋友圈信息
				MessageBean messageBean = new MessageBean();
				messageBean.address = jsonToBean.address;
				messageBean.comment = jsonToBean.comment;
				messageBean.good = jsonToBean.good;
				messageBean.message = jsonToBean.message;
				messageBean.messageImage = jsonToBean.messageImage;
				messageBean.nickname = jsonToBean.nickname;
				messageBean.password = jsonToBean.password;
				messageBean.phone_num = jsonToBean.phone_num;
				messageBean.photo = jsonToBean.sign;
				messageBean.successCode = jsonToBean.successCode;
				messageBean.time = jsonToBean.time;
				System.out.println();
				
			}else{
				// 添加到数据库
				MessageDao dao = new MessageDao(getApplicationContext());
				Message message = new Message();
				message.account = jsonToBean.owner;
				message.content = jsonToBean.content;
				message.createTime = jsonToBean.createTime;
				
				message.direction = 1;
				message.owner = jsonToBean.account;
				message.read = false;
				message.state = 1;
				message.type = 0;
				message.url = jsonToBean.url;
				dao.addMessage(message);
				
				// 通知数据更新
				Intent intent = new Intent();
				intent.setAction(PushReceiver.ACTION_TEXT);
				sendBroadcast(intent);
			}
		}else{ //邀请没有state属性
			if(jsonToBean.CircleFriends == true){
				// 是朋友圈信息
				FriendMessage messageBean = new FriendMessage();
				messageBean.address = jsonToBean.address;
				messageBean.comment = jsonToBean.comment;
				messageBean.good = jsonToBean.good;
				messageBean.message = jsonToBean.message;
				messageBean.messageImage = jsonToBean.messageImage;
				messageBean.nickname = jsonToBean.nickname;
				messageBean.phone_num = jsonToBean.phone_num;
				messageBean.photo = jsonToBean.photo;
				messageBean.successCode = jsonToBean.successCode;
				messageBean.time = jsonToBean.time;
				
				// 通知数据更新
				Intent intent = new Intent();
				intent.setAction(PushReceiver.FRINDCIRCLES);
				intent.putExtra("frindcircles", messageBean);
				sendBroadcast(intent);
				
			}else{
				// 邀请
				if(jsonToBean.agree==false){
					InvitationDao dao = new InvitationDao(getApplicationContext());
					Invitation invitation = new Invitation();
					invitation.account = jsonToBean.owner;
					invitation.agree = false;
					invitation.content = "请求添加好友!";
					invitation.icon = jsonToBean.icon;
					invitation.name = jsonToBean.name;
					invitation.owner = jsonToBean.account;
					dao.addInvitation(invitation);
					
					// 通知数据更新
					Intent intent = new Intent();
					intent.setAction(PushReceiver.INVITATION);
					sendBroadcast(intent);
				}else{
					// 通过邀请
					FriendDao dao = new FriendDao(getApplicationContext());
					Friend friend = new Friend();
					friend.account = (jsonToBean.owner);
					friend.alpha = (CommonUtil.getFirstAlpha(jsonToBean.name));
					friend.icon = (jsonToBean.icon);
					friend.name = (jsonToBean.name);
					friend.owner = (jsonToBean.account);
					friend.sort = (0);
					dao.addFriend(friend);
					
					// 通知数据更新
					Intent intent = new Intent();
					intent.setAction(PushReceiver.INVITATION);
					sendBroadcast(intent);
				}
			}
		}

		
//		Intent intent = new Intent();
//		intent.setAction(PushReceiver.ACTION_TEXT);
//		intent.putExtra(PushReceiver.DATA_KEY, data);
//		sendBroadcast(intent);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtils.i("disconnect");
		connectorManager.disconnect();
	}

}
