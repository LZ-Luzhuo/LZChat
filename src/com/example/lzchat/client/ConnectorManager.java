package com.example.lzchat.client;

import com.example.lzchat.client.Connector.ConnectorListener;
import com.example.lzchat.client.request.AuthRequest;
import com.example.lzchat.client.request.Request;
import com.lidroid.xutils.util.LogUtils;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-21 下午11:42:17
 * 
 * 描述:Connector的管理,用户直接操作Connector的管理类(ConnectorManager),而非Connector类
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class ConnectorManager implements ConnectorListener {

	private static ConnectorManager instance;
	private Connector connector;
	private ConnectorListener listener;

	public static ConnectorManager getInstance() {
		if (instance == null) {
			synchronized (ConnectorManager.class) {
				if (instance == null) {
					instance = new ConnectorManager();
				}
			}
		}
		return instance;
	}

	private ConnectorManager() {

	}

	/**
	 * 连接(发送数据)
	 * @param auth 用户的认证信息,尽量使用经过AuthRequest封装的认证信息
	 */
	public void connnect(String auth) {
		connector = new Connector();
		connector.setConnectorListener(this);
		connector.connect();

		connector.auth(auth);
	}

	/**
	 * 通讯(发送数据)
	 * @param auth 用户的认证信息
	 */
	public void connnect(AuthRequest auth) {
		connector = new Connector();
		connector.setConnectorListener(this);
		connector.connect();

		connector.auth(auth.getData());
	}

	/**
	 * 添加请求信息(连接操作由服务管理,自动发送数据,客户端字仅需添加请求信息即可,收到的信息将有广播发出)
	 * @param request 请求信息,尽量使用经过Request封装的请求信息
	 */
	public void putRequest(String request) {
		connector.putRequest(request);
	}

	/**
	 * 添加请求信息(连接操作由服务管理,自动发送数据,客户端字仅需添加请求信息即可,收到的信息将有广播发出)
	 * @param request 经过Request封装的请求信息
	 */
	public void putRequest(Request request) {
		connector.putRequest(request.getData());
	}

	//接收ConnectorListener接口发来的数据
	@Override
	public void pushData(String data) {
		LogUtils.i("ConnManager_data : " + data);

		if (listener != null) {
			listener.pushData(data);
		}
	}

	public void setConnectorListener(ConnectorListener listener) {
		this.listener = listener;
	}

	public interface ConnectorListener {
		//再次将收到的信息网上传
		void pushData(String data);
	}
	
	/**
	 * 断开连接
	 */
	public void disconnect(){
		connector.disconnect();
	}
}
