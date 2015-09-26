package com.example.lzchat.test;

import java.io.InputStream;

import android.test.AndroidTestCase;
import android.util.Log;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.bean.UserBean;
import com.example.lzchat.net.HttpClientUtil;
import com.example.lzchat.utils.GsonTools;

public class HttpClientTest extends AndroidTestCase{

	private static final String TAG = "HttpClientTest";

	public void test(){
		// 请求网络,发送信息
		HttpClientUtil httpClientUtil = new HttpClientUtil();
		UserBean userBean = new UserBean();
		userBean.sign = 1;
		userBean.nickname = "xiaoxian";
		userBean.phone_num = "15888888888";
		userBean.password = "123456";
		
		String json = GsonTools.beanToJson(userBean);
		InputStream resource = httpClientUtil.sendXml(GlobalParams.URL, json);
		if(resource!=null){
			Log.i(TAG, resource.toString());
		}else{
			Log.i(TAG, "resource:null");
		}
	}
}
