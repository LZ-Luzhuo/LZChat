package com.example.lzchat.test;

import java.io.File;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.os.Environment;
import android.test.AndroidTestCase;
import android.util.Base64;
import android.util.Log;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.bean.UserBean;
import com.example.lzchat.net.HttpClientUtil;
import com.example.lzchat.utils.Base64Coder;
import com.example.lzchat.utils.BitmapUtil;
import com.example.lzchat.utils.GsonTools;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

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
	
	public void test1(){
		Bitmap bitmapFromFile = BitmapUtil.getBitmapFromFile(new File(Environment.getExternalStorageDirectory(), "IMG_20151001_164543.jpg"));
		long bitmapsize = BitmapUtil.getBitmapsize(bitmapFromFile);
		BitmapUtil.saveFile(bitmapFromFile, new File(Environment.getExternalStorageDirectory(), "IMG_20151001_1645430.jpg"),60);
		System.out.println(bitmapsize);
	}
	
	public void test2(){
		Bitmap bitmapFromFile1 = BitmapUtil.getBitmapFromFile(new File(Environment.getExternalStorageDirectory(), "IMG_20151001_1645430.jpg"));
		long bitmapsize2 = BitmapUtil.getBitmapsize(bitmapFromFile1);
		System.out.println(bitmapsize2);
	}
	
	public void upload(){
		RequestParams params = new RequestParams();
		params.addHeader("phone_num", "15888888888");
		params.addHeader("password", "123456");
		params.addHeader("filename", "beijing.jpg");

		// 加入文件参数后默认使用MultipartEntity（"multipart/form-data"），
		// 如需"multipart/related"，xUtils中提供的MultipartEntity支持设置subType为"related"。
		// 使用params.setBodyEntity(httpEntity)可设置更多类型的HttpEntity（如：
		// MultipartEntity,BodyParamsEntity,FileUploadEntity,InputStreamUploadEntity,StringEntity）。
		// 例如发送json参数：params.setBodyEntity(new StringEntity(jsonStr,charset));
		params.addBodyParameter("file", new File(Environment.getExternalStorageDirectory(), "beijing.jpg"));

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.POST,
		    GlobalParams.URL+"/Upload",
		    params,
		    new RequestCallBack<String>() {

		        @Override
		        public void onSuccess(ResponseInfo<String> responseInfo) {
//		            testTextView.setText("reply: " + responseInfo.result);
		        	LogUtils.i("上传文件成功");
		        }

		        @Override
		        public void onFailure(HttpException error, String msg) {
//		            testTextView.setText(error.getExceptionCode() + ":" + msg);
		        	LogUtils.i("上传文件失败");
		        }
		});
	}
	
	public void Stringtocode(){
		String str = "sadf我是中文..";
		String encode = new String (Base64Coder.encode(str.getBytes()));
		System.out.println("编码:"+encode);
		String destr = Base64Coder.decodeString(encode);
		System.out.println("解码:"+destr);
	}
}
