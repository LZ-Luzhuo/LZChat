package com.example.lzchat.client.request;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-22 下午11:17:40
 * 
 * 描述:文本信息的请求
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.example.lzchat.utils.Base64Coder;
import com.google.gson.Gson;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-22 上午12:28:15
 * 
 * 描述:文本信息的请求封装,生成json数据;这里指定了发送文本信息的一些规则
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class TextRequest implements Request {
	private Map<String, String> map = new HashMap<String, String>();

	public TextRequest(String sender, String token, String receiver,
			String content) {
		String encode = new String (Base64Coder.encode(content.getBytes()));
		map.put("type", "request"); //访问类型:请求
		map.put("sequence", UUID.randomUUID().toString());
		map.put("action", "text"); //请求类型:文本信息
		map.put("sender", sender);
		map.put("token", token);
		map.put("receiver", receiver); //接收者账户
		map.put("content", encode);
	}

	@Override
	public String getData() {
		return new Gson().toJson(map);
	}

}
