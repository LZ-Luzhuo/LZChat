package com.example.lzchat.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-10-3 下午12:26:05
 * 
 * 描述:对Bitmap图片进行处理的工具类
 * 		该类还不够完善,非常不好用
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class BitmapUtil {
	/**
	 * 压缩图片,指定压缩到xkb,加载到内存的大小不变
	 * @param image Bitmap图片
	 * @return 压缩后的Bitmap
	 */
	public static Bitmap compressImage(Bitmap image, int kb) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100; 
		while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
	/**
	 * 从文件路径获取图片
	 * @return
	 */
	public static Bitmap getBitmapFromFile(File filePath){
		Bitmap bitmap = BitmapFactory.decodeFile(filePath.getPath());
		return bitmap;
	}
	
	/**  
	  * 保存并压缩文件  
	  * @param bm  
	  * @param fileName  
	  * @param quality 压缩品质(0,100],100不压缩
	  */    
	public static void saveFile(Bitmap bm, File filePath, int quality) {
		try {
			// 文件不存在则创建
			if(filePath.exists()){
				filePath.delete();
			}
		    	filePath.getParentFile().mkdir();
		    	filePath.createNewFile();
		    	 
		    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		    bm.compress(Bitmap.CompressFormat.JPEG, quality, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取Bitmap的大小
	 * @return
	 */
	@SuppressLint("NewApi")
	public static long getBitmapsize(Bitmap bitmap){
		//大于Android API（12）的版本
		//Build.VERSION.SDK_INT来获取当前API的版本
		//HONEYCOMB_MR1 = 12;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
			return bitmap.getByteCount();
		return bitmap.getRowBytes() * bitmap.getHeight();
	 	}
	}
