package com.example.lzchat.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.bean.UserBean;
import com.example.lzchat.net.HttpClientUtil;
import com.example.lzchat.utils.Base64Coder;
import com.example.lzchat.utils.BitmapUtil;
import com.example.lzchat.utils.GsonTools;
import com.example.lzchat.utils.SharePrefUtil;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-28 下午11:20:36
 * 
 * 描述:个人详情信息
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class PersonalActivity extends Activity implements OnClickListener{
	protected static final int SUCCESS = 0;
	protected static final int FAILURE = 1;
	@ViewInject(R.id.personal_photo)
	private ImageView personal_photo;
	@ViewInject(R.id.personal_nickname)
	private TextView personal_nickname;
	@ViewInject(R.id.personal_phonenumber)
	private TextView personal_phonenumber; 
	@ViewInject(R.id.personal_adderss)
	private TextView personal_adderss;
	@ViewInject(R.id.personal_sex)
	private TextView personal_sex;
	@ViewInject(R.id.personal_region)
	private TextView personal_region;
	@ViewInject(R.id.personal_photo_layout)
	private LinearLayout personal_photo_layout;
	@ViewInject(R.id.personal_return)
	private ImageButton personal_return;
	private File lastPhotoFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_personal_information);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		String lastphone_num = SharePrefUtil.getString(PersonalActivity.this, "lastphone_num", "");
		if(lastphone_num != "")
			personal_phonenumber.setText(lastphone_num);
		String lastnickname = SharePrefUtil.getString(PersonalActivity.this, "lastnickname", "");
		if(lastnickname != "")
			personal_nickname.setText(lastnickname);
    	String avatar = SharePrefUtil.getString(PersonalActivity.this, "avatar", "");
    	if(avatar != ""){
    		BitmapUtils bitmapUtils = new BitmapUtils(PersonalActivity.this);
    		bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
    		bitmapUtils.display(personal_photo, avatar);
    	}
		String address = SharePrefUtil.getString(PersonalActivity.this, "address", "");
		if(address != "")
			personal_adderss.setText(address);
		String sex = SharePrefUtil.getString(PersonalActivity.this, "sex", "");
		if(sex != "")
			personal_sex.setText(sex);
		String region = SharePrefUtil.getString(PersonalActivity.this, "region", "");
		if(region != "")
			personal_region.setText(region);
			
		personal_photo_layout.setOnClickListener(PersonalActivity.this);
		personal_return.setOnClickListener(PersonalActivity.this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.personal_photo_layout:
			Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
            startActivityForResult(intent, 22);
			break;
		case R.id.personal_return:
			setResult(Activity.RESULT_OK, new Intent());
			PersonalActivity.this.finish();
			break;
		}
	}
	
	/**
	 * 将相机的图片地址转成File,并将其存到字段里
	 * @return
	 */
	private File getPhotoFile() {
        Date date = new Date(System.currentTimeMillis());
        lastPhotoFile = new File(Environment.getExternalStorageDirectory(),new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss").format(date) + ".jpg");
        return lastPhotoFile;
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
        case 22:
            if (data != null)
                startPhotoZoom(data.getData(), 150);
            break;

        case 33:
            if (data != null) 
                setPicToView(data);
            break;
        }
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 调用系统照相裁剪功能
	 * @param uri
	 * @param size
	 */
	private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, 33);
    }
	
	
	// 将图片压缩后保存到本地文件
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
        	// 获取剪裁后的图片
        	Bitmap photo = bundle.getParcelable("data");
        	// 保存图片到本地
        	File f = new File(getPhotoFile().getPath());
        	BitmapUtil.saveFile(photo, f,60);
        	//删除旧图片
        	String oldavatar = SharePrefUtil.getString(PersonalActivity.this, "avatar", "");
        	if(oldavatar!="")
        		new File(oldavatar).delete();
        	// 显示图片
        	SharePrefUtil.saveString(PersonalActivity.this, "avatar", f.getPath());
        	String avatar = SharePrefUtil.getString(PersonalActivity.this, "avatar", "");
        	LogUtils.i("avatar:"+avatar);
        	if(avatar != ""){
        		BitmapUtils bitmapUtils = new BitmapUtils(this);
        		bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);
        		bitmapUtils.display(personal_photo, avatar);
        	}
        		
        	// 上传图片到服务器
        	UserBean userBean = new UserBean();
        	String lastphone_num = SharePrefUtil.getString(this, "lastphone_num", "");
        	if(lastphone_num!="")
        		userBean.phone_num = lastphone_num;
        	String lastPassword = SharePrefUtil.getString(this, "lastPassword", "");
        	if(lastphone_num!="")
        		userBean.password = lastPassword;
        	userBean.sign = 3;
        	
        	// 将图片转成字符串形式
        	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	 		photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	 		byte[] b = stream.toByteArray();
	 		// 将图片流以字符串形式存储下来
	 		String file = new String(Base64Coder.encode(b));
	 		LogUtils.i(file);
	 		if(file!=null){
	 			userBean.photo = file;
	 		}
        	final String beanToJson = GsonTools.beanToJson(userBean);
        	new Thread(){
				@Override
				public void run() {
					super.run();
					HttpClientUtil clientUtil = new HttpClientUtil();
		        	clientUtil.sendXml(GlobalParams.URL+GlobalParams.UPDATE, beanToJson);
				}
        	}.start();
        }
    }
}
