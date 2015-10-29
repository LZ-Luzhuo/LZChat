package com.example.lzchat.client.request;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-22 上午12:22:55
 * 
 * 描述: 认证信息的请求封装,生成json数据;这里指定了发送认证信息的一些规则
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class AuthRequest implements Request {
	private Map<String, String> map = new HashMap<String, String>();

	public AuthRequest(String sender, String token) {
		// 请求类型类型:request:发送/response:接收
		map.put("type", "request");
		// 序列
		map.put("sequence", UUID.randomUUID().toString());
		// 行为:auth:认证/邀请/text:信息
		map.put("action", "auth");
		// 发送者账号
		map.put("sender", sender); //发送者账户
		// 发送者标志(安全)
		map.put("token", token); //发送者标志
	}

	@Override
	public String getData() {
		return new Gson().toJson(map);
	}

}
