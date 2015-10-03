package com.example.lzchat.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.lzchat.GlobalParams;
import com.example.lzchat.R;
import com.example.lzchat.bean.UserBean;
import com.example.lzchat.net.HttpClientUtil;
import com.example.lzchat.net.NetUtil;
import com.example.lzchat.utils.Base64Coder;
import com.example.lzchat.utils.BitmapUtil;
import com.example.lzchat.utils.GsonTools;
import com.example.lzchat.utils.SharePrefUtil;
import com.example.lzchat.view.ActionSheetDialog;
import com.example.lzchat.view.ActionSheetDialog.OnSheetItemClickListener;
import com.example.lzchat.view.ActionSheetDialog.SheetItemColor;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * =================================================
 * 
 * 作者:卢卓
 * 
 * 版本:1.0
 * 
 * 创建日期:2015-9-24 下午3:21:25
 * 
 * 描述:注册
 * 
 * 修订历史:
 * 
 * 
 * =================================================
 **/
public class RegisterActivity extends Activity implements OnClickListener{
	protected static final int SUCCESS = 0;
	protected static final int FAILURE = 1;
	@ViewInject(R.id.reg_register_btn)
	private Button reg_register_btn;
	@ViewInject(R.id.reg_camera_portrait)
	private ImageButton reg_camera_portrait;
	@ViewInject(R.id.reg_nickname)
	private EditText reg_nickname;
	@ViewInject(R.id.reg_phone_code)
	private EditText reg_phone_code;
	@ViewInject(R.id.reg_phone_num)
	private EditText reg_phone_num;
	@ViewInject(R.id.reg_password)
	private EditText reg_password;
	@ViewInject(R.id.reg_return_btn)
	private Button reg_return_btn;
	private ProgressDialog progressDialog;
	private File lastPhotoFile;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				Toast.makeText(RegisterActivity.this, "注册成功!", 0).show();
				UserBean userBeanmsg = (UserBean) msg.obj;
				SharePrefUtil.saveString(RegisterActivity.this, "lastphone_num", userBeanmsg.phone_num);
				SharePrefUtil.saveString(RegisterActivity.this, "lastPassword", userBeanmsg.password);
				updata();//上传照片
				RegisterActivity.this.finish();
				startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
				break;
			case FAILURE:
				Toast.makeText(RegisterActivity.this, "注册失败!", 0).show();
				break;
			}
		}
	};
	private Bitmap photo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_btn_register);
		ViewUtils.inject(this);
		initView();
	}

	private void initView() {
		reg_register_btn.setOnClickListener(this);
		reg_return_btn.setOnClickListener(this);
		reg_camera_portrait.setOnClickListener(this);
		
		progressDialog = new ProgressDialog(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reg_return_btn:
			finish();
			break;
		case R.id.reg_register_btn:
			progressDialog.setMessage("请稍候...");
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.show();
			register();
			break;
		case R.id.reg_camera_portrait:
			LogUtils.i("reg_camera_portrait");
			new ActionSheetDialog(RegisterActivity.this)
			.setTitle("请选择上传方式")
			.addSheetItem("拍照", SheetItemColor.Red,
					new OnSheetItemClickListener() {
						@Override
						public void onClick(int which) {
							// 调用系统照相机
							Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							// 指定调用相机拍照后照片的储存路径
							intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(getPhotoFile()));
							startActivityForResult(intent, 11);
						}
					})
					.addSheetItem("相册", SheetItemColor.Red, new OnSheetItemClickListener() {
						
						@Override
						public void onClick(int which) {
	                      Intent intent = new Intent(Intent.ACTION_PICK, null);
	                      intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
	                      startActivityForResult(intent, 22);
	                      
						}
					})
					.show();
		}
	}

	/**
	 * 注册信息
	 */
	private void register() {
		if(!NetUtil.checkNet(this)){
			progressDialog.dismiss();
			Toast.makeText(this, "请检查网络!",0).show();
			return;
		}
		String nickname = reg_nickname.getText().toString().trim();
		if ("".equals(nickname)) {
			progressDialog.dismiss();
			Toast.makeText(this, "昵称不能为空!",0).show();
			return;
		}
		String phone_num = reg_phone_num.getText().toString().trim();
		if ("".equals(phone_num)){
			progressDialog.dismiss();
			Toast.makeText(this, "手机号不能为空!",0).show();
			return;
		}
		String password = reg_password.getText().toString().trim();
		if ("".equals(password)) {
			progressDialog.dismiss();
			Toast.makeText(this, "密码不能为空!",0).show();
			return;
		}

		// 请求网络,发送信息
		final HttpClientUtil httpClientUtil = new HttpClientUtil();
		UserBean userBean = new UserBean();
		userBean.sign = 1;
		userBean.nickname = nickname;
		userBean.phone_num = phone_num;
		userBean.password = password;
		
		final String json = GsonTools.beanToJson(userBean);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					InputStream is = httpClientUtil.sendXml(GlobalParams.URL+GlobalParams.LOGIN, json);
					
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					int i = -1;
					while ((i = is.read()) != -1) {
						baos.write(i);
					}
					String json = baos.toString("utf-8");
					LogUtils.i(json);
					UserBean regUserBean = GsonTools.jsonToBean(json, UserBean.class);
					progressDialog.dismiss();
					// 注册成功
					if(regUserBean!=null){
						if(regUserBean.successCode==1){
							Message message = new Message();
							message.what = SUCCESS;
							message.obj = regUserBean;
							handler.sendMessage(message);
						}else{
							Message message = new Message();
							message.what = FAILURE;
							handler.sendMessage(message);
						}
					}
				} catch (IOException e) {
					LogUtils.e("register http request error!");
					e.printStackTrace();
				}
			}
		}).start();
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
        case 11:
            startPhotoZoom(Uri.fromFile(lastPhotoFile), 150);
            break;

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
        	photo = bundle.getParcelable("data");
        	// 保存图片到本地
        	File f = new File(getPhotoFile().getPath());
        	BitmapUtil.saveFile(photo, f,60);
        	//删除旧图片
        	String oldavatar = SharePrefUtil.getString(RegisterActivity.this, "avatar", "");
        	if(oldavatar!="")
        		new File(oldavatar).delete();
        	// 显示图片
        	SharePrefUtil.saveString(RegisterActivity.this, "avatar", f.getPath());
        	String avatar = SharePrefUtil.getString(RegisterActivity.this, "avatar", "");
        	LogUtils.i("avatar:"+avatar);
        }
    }
    
    private void updata(){
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
