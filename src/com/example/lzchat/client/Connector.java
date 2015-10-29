package com.example.lzchat.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import com.example.lzchat.GlobalParams;
import com.lidroid.xutils.util.LogUtils;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-21 下午11:12:57
 * 
 * 描述:长连接管理
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class Connector {
	private String dstName = GlobalParams.dstName;
	private int dstPort = GlobalParams.dstPort;
	private Socket client;
	private ConnectorListener listener;

	//ArrayBlockingQueue 是一个阻塞队列
	private ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(8); //初始化长度8

	public Connector() {

	}

	/**
	 * 握手连接
	 */
	public void connect() {
		try {
			// 三次握手
			if (client == null || client.isClosed()) {
				client = new Socket(dstName, dstPort);
			}

			// 运行发送数据,有数据就发送
			new Thread(new RequestWorker()).start();

			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						InputStream is = client.getInputStream();
						byte[] buffer = new byte[1024];
						int len = -1;
						while ((len = is.read(buffer)) != -1) {
							final String text = new String(buffer, 0, len);

							//获得服务器数据
							LogUtils.i("text : " + text);

							if (listener != null) {
								listener.pushData(text);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 自动发送数据的运行类
	 * @author Luzhuo
	 */
	public class RequestWorker implements Runnable {
		@Override
		public void run() {

			// 数据通讯
			OutputStream os;
			try {
				os = client.getOutputStream();

				while (true) {
					//阻塞式方法
					String content = queue.take();
					os.write(content.getBytes());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 认证(发送数据)
	 * @param auth 认证的json字符串,尽量使用经过AuthRequest封装的认证信息
	 */
	public void auth(String auth) {
		putRequest(auth);
	}


	/**
	 * 通讯(发送数据)
	 * @param content
	 */
	public void putRequest(String content) {
		try {
			//添加到ArrayBlockingQueue集合,会自动发送请求
			queue.put(content);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 断开连接
	 */
	public void disconnect() {
		try {
			if (client != null && !client.isClosed()) {
				client.close();
				client = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置连接监听
	 * @param listener
	 */
	public void setConnectorListener(ConnectorListener listener) {
		this.listener = listener;
	}

	/**
	 * 连接监听
	 * @author Luzhuo
	 */
	public interface ConnectorListener {
		/**
		 * 收到数据
		 * @param data
		 */
		void pushData(String data);
	}
}
