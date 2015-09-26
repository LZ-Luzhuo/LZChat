package com.example.lzchat.net;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.lzchat.ConstantValue;
import com.example.lzchat.GlobalParams;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-25 下午9:15:02
 * 
 * 描述:Http客户端工具
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class HttpClientUtil {
	private HttpClient client;
	private HttpPost post;
	private HttpGet get;
	
	public HttpClientUtil() {
		client = new DefaultHttpClient();
		//判断是否需要设置代理信息
		if(StringUtils.isNotBlank(GlobalParams.PROXY)){ //有值就设置代理
			//设置代理信息
			HttpHost host = new HttpHost(GlobalParams.PROXY, GlobalParams.PORT);  //设置代理主机名，端口号
			client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
		}
	}
	
	/**
	 * 向指定的连接发送xml文件
	 * @param uri 地址
	 * @param xml xml文件
	 */
	public InputStream sendXml(String uri,String xml){
		post = new HttpPost(uri);
		
		try {
			StringEntity entity = new StringEntity(xml, ConstantValue.ENCONDING); //xml字符串，编码
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			
			//200
			if(response.getStatusLine().getStatusCode()==200){
				return response.getEntity().getContent();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
