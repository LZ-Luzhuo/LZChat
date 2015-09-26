package com.example.lzchat.utils;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-3 下午8:37:27
 * 
 * 描述:Json数据的各种转换,
 * 	       该类实质使用的是Gson.
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class GsonTools {

	public GsonTools() {
	}

	/**
	 * Bean --> Json
	 * @param object Bean
	 * @return jsonString
	 */
	public static String beanToJson(Object object) {
		Gson gson = new Gson();
		String gsonString = gson.toJson(object);
		return gsonString;
	}

	/**
	 * Json --> Bean
	 * @param json jsonString
	 * @param cls Bean.class
	 * @return Bean
	 */
	public static <T> T jsonToBean(String json, Class<T> cls) {
		Gson gson = new Gson();
		T t = gson.fromJson(json, cls);
		return t;
	}
	
	/**
	 * List --> Json
	 * @param list List
	 * @return jsonString
	 */
	public static String listToJson(List list){
		return beanToJson(list);
	}
	
	/**
	 * Json --> List
	 * @param json jsonString
	 * @param cls Bean.class
	 * @return List<Bean>
	 */
	public static <T> List<T> JsonToList(String json, Class<T> cls) {
		Gson gson = new Gson();
		List<T> list = gson.fromJson(json, new TypeToken<List<T>>() {}.getType());
		return list;
	}
	
	/**
	 * Map --> Json
	 * @param map Map
	 * @return jsonString
	 */
	public static String mapToJson(Map map){
		Gson gson = new Gson();
		String json = gson.toJson(map);
		return json;
	}
	
	/**
	 * Json --> Map
	 * @param json jsonString
	 * @return Map<String, T>
	 */
	public static <T> Map<String, T> jsonToMap(String json) {
		Map<String, T> map = null;
		Gson gson = new Gson();
		map = gson.fromJson(json, new TypeToken<Map<String, T>>() {
		}.getType());
		return map;
	}

}
