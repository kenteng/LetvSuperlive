package com.lesports.stadium.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.GetMobileCodeInfo;
import com.lesports.stadium.bean.LoginResultData;
import com.lesports.stadium.bean.ServerError;
import com.lesports.stadium.common.Constants;
import com.lesports.stadium.http.paser.LetvLoginResultParser;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.utils.CommonUtils;
import com.lesports.stadium.utils.LetvUtil;
import com.lesports.stadium.utils.LoginUtil;
import com.lesports.stadium.utils.ToastUtil;
import com.lesports.stadium.view.CustomDialog;
import com.letv.component.core.async.TaskCallBack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * ***************************************************************
 * 
 * @ClassName: RegisterActivity
 * 
 * @Desc : 注册
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 盛之刚
 * 
 * @data:2016-2-23 下午12:00:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *  ***************************************************************
 */
public class RegisterActivity extends Activity implements OnClickListener{

	/**
	 * 返回
	 */
	private ImageView iv_back;
	
	/**
	 * 手机号输入框
	 */
	private EditText phoneNumber;
	
	/**
	 * 激活码
	 */
	private EditText activiation;
	
	/**
	 * 图片验证码
	 */
	private EditText codes;
	
	/**
	 * 密码
	 */
	private EditText userPassword;
	
	/**
	 * 获取激活码
	 */
	private TextView tv_send_code;
	
	/**
	 * 图片验证码
	 */
	private ImageView mVCodeImage;
	
	/**
	 * 使用协议
	 */
	private TextView tv_explain;
	
	/**
	 * 协议选择
	 */
	private CheckBox chk_explain;
	
	/**
	 * 注册
	 */
	private TextView tvRegister;
	
	/**
	 * 网络请求
	 */
	private NetService netService;
	
	/**
	 * cookId
	 */
	private String mCookid;
	
	/**
	 * 是否同意使用协议
	 */
	private boolean isAgreeProtol = true;
	
	/**
	 * 发送验证码
	 */
	public static final int SEND_ACTIVATION=0;
	
	/**
	 * 注册
	 */
	public static final int REGISTER=1;
	
	/**
	 * 计时器
	 */
	private TimeTimer mTimeTimer;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 授权
	 */
	private String auth;
	
    /**
     * 平台标识
     */
	private String plat;
	
	/**
	 * 图片验证码
	 */
	private String captchaValue;
	/**
	 * 信息提示
	 */
	private CustomDialog msgDialog;
	
	/**
	 * videoView
	 */
//	private VideoView mVideoView;
	
	public static final String VIDEO_NAME = "welcome_video.mp4";
	
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			BackData result=(BackData)msg.obj;
			switch (msg.what) {
			case SEND_ACTIVATION:				
				if (result.getObject() == null) {
					ToastUtil.showToast(RegisterActivity.this,
							R.string.get_mobile_code_fail);
					return;
				}
				Log.e("SSSSsup", result.getObject().toString());
				LoginResultData res=useWayJsonDataofLogin(String.valueOf(result.getObject()));
//				LoginResultData res=LetvLoginResultParser.parseReponseToLoginUserInfo(String.valueOf(result.getObject()));
				if ("0".equals(res.getErrorCode())) {
					ToastUtil.showToast(RegisterActivity.this,
							R.string.registeractivity_send);
				}
				if ("1011".equals(res.getErrorCode())) {
					ToastUtil.showToast(RegisterActivity.this,
							R.string.login_errorcode_1011);
					closeTimeTimer();
					tv_send_code.setText(getString(R.string.get_activation_code));
					tv_send_code.setEnabled(true);
				}
				if ("1000".equals(res.getErrorCode())) {
					ToastUtil.showToast(RegisterActivity.this,
							R.string.login_errorcode_1000);
					closeTimeTimer();
					tv_send_code.setText(getString(R.string.get_activation_code));
					tv_send_code.setEnabled(true);
				}
				if ("1026".equals(res.getErrorCode())) {
					ToastUtil.showToast(RegisterActivity.this,
							R.string.login_errorcode_1026);
					closeTimeTimer();
					tv_send_code.setText(getString(R.string.get_activation_code));
					tv_send_code.setEnabled(true);
				}
				if ("1027".equals(res.getErrorCode())) {
					ToastUtil.showToast(RegisterActivity.this,
							R.string.login_errorcode_1027);
					closeTimeTimer();
					tv_send_code.setText(getString(R.string.get_activation_code));
					tv_send_code.setEnabled(true);
				}
				break;
			case REGISTER:
				if (result.getObject() == null) {
					ToastUtil.showToast(RegisterActivity.this,R.string.regist_fail);
					return;
				}		
//				Log.e("SSSSSSStv_register", result.getObject().toString());
				LoginResultData datas=useWayJsonDataofLogin(String.valueOf(result.getObject()));
//				LoginResultData datas = LetvLoginResultParser.parseReponseToLoginUserInfo(String.valueOf(result.getObject()));
				if (datas.getErrorCode() != null) {
					if (datas.getErrorCode().equals("1000")) {
						ToastUtil.showToast(RegisterActivity.this,
								R.string.param_error);
					}
					if (datas.getErrorCode().equals("1020")) {
						ToastUtil.showToast(RegisterActivity.this,
								R.string.username_error);
					}
					if (datas.getErrorCode().equals("1021")) {
						ToastUtil.showToast(RegisterActivity.this,
								R.string.username_exist);
					}
			
					if (datas.getErrorCode().equals("1011")) {
						ToastUtil.showToast(RegisterActivity.this,
								R.string.login_errorcode_1011);
					}
					if (datas.getErrorCode().equals("1012")) {
						ToastUtil.showToast(RegisterActivity.this,
								R.string.mobile_exist);
					}
					if (datas.getErrorCode().equals("1013")) {
						ToastUtil.showToast(RegisterActivity.this,
								R.string.usernickname_exist);
					}
					if (datas.getErrorCode().equals("1023")) {
						ToastUtil.showToast(RegisterActivity.this,
								R.string.code_mobile_error);
					}
					if (datas.getErrorCode().equals("1022")) {
						ToastUtil.showToast(RegisterActivity.this,
								R.string.mobile_code_error);
					}
					if (datas.getErrorCode().equals("1031")) {
						ToastUtil.showToast(RegisterActivity.this,
								R.string.ip_reg_exist);
					}
					if (datas.getErrorCode().equals("500")) {
						ToastUtil.showToast(RegisterActivity.this,
								R.string.reg_filied);
					}
				}
				if (datas.getBean() != null) {
					showDiaLog("注册成功");
//					AlertDialog dialog = new AlertDialog.Builder(RegisterActivity.this)
//							.setMessage(R.string.regist_success)
//							.setPositiveButton(
//									R.string.ok,
//									new DialogInterface.OnClickListener() {
//
//										@Override
//										public void onClick(
//												DialogInterface dialog,
//												int which) {
//											dialog.dismiss();
//											RegisterActivity.this.finish();
//										}
//									}).create();
//					dialog.setCancelable(false);
//					dialog.show();
				}
			break;
			default:
				break;
			}
		}

	
	};
	/**
	 * 用来解析获取验证码以后的数据
	 * @param valueOf
	 * @return
	 * {
    "action": "clientSendMsg",
    "bean": [],
    "errorCode": "0",
    "message": "手机激活码已经下发",
    "responseType": "json",
    "status": "1"
}
	 */
	private LoginResultData useWayJsonDataofLogin(String valueOf) {
		// TODO Auto-generated method stub
		LoginResultData data=new LoginResultData();
		if(!TextUtils.isEmpty(valueOf)){
			try {
				JSONObject obj=new JSONObject(valueOf);
			
				if(obj.has("errorCode")){
					data.setErrorCode(obj.getString("errorCode"));
				}
				if(obj.has("message")){
					data.setMessage(obj.getString("message"));
				}
				return data;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return data;
	};
	/**
	 * 乐视规则
	 */
	private TextView tv_explain_rule;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register);
		
		initBaseData();
		initView();
		initData();
		initListener();
	}

	
	
	private void initBaseData(){
		isAgreeProtol=true;
		netService=NetService.getInStance();
	}
	
	private void initView() {
//		mVideoView = (VideoView) findViewById(R.id.videoView);
		
		iv_back=(ImageView)findViewById(R.id.iv_back);
		phoneNumber=(EditText)findViewById(R.id.edt_phone);
		activiation=(EditText)findViewById(R.id.edt_activation_code);
		codes=(EditText)findViewById(R.id.edt_codes);
		userPassword=(EditText)findViewById(R.id.edt_password);
		tv_send_code=(TextView)findViewById(R.id.tv_send_code);
		mVCodeImage=(ImageView)findViewById(R.id.iv_image);
		chk_explain=(CheckBox)findViewById(R.id.chk_explain);
		tv_explain=(TextView)findViewById(R.id.tv_explain);
		tvRegister=(TextView)findViewById(R.id.tv_register);
		tv_explain_rule = (TextView)findViewById(R.id.tv_explain_rule);
//		File videoFile = getFileStreamPath(VIDEO_NAME);
//        if (!videoFile.exists()) {
//            videoFile = copyVideoFile();
//        }
//
//        playVideo(videoFile);
	}
	
//	 private File copyVideoFile() {
//	        File videoFile;
//	        try {
//	            FileOutputStream fos = openFileOutput(VIDEO_NAME, MODE_PRIVATE);
//	            InputStream in = getResources().openRawResource(R.raw.welcome_video);
//	            byte[] buff = new byte[1024];
//	            int len = 0;
//	            while ((len = in.read(buff)) != -1) {
//	                fos.write(buff, 0, len);
//	            }
//	        } catch (FileNotFoundException e) {
//	            e.printStackTrace();
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//	        videoFile = getFileStreamPath(VIDEO_NAME);
//	        if (!videoFile.exists())
//	            throw new RuntimeException("video file has problem, are you sure you have welcome_video.mp4 in res/raw folder?");
//	        return videoFile;
//	    }
	  
//	    private void playVideo(File videoFile) {
//	        mVideoView.setVideoPath(videoFile.getPath());
//	        mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
//	        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//	            @Override
//	            public void onPrepared(MediaPlayer mediaPlayer) {
//	                mediaPlayer.setLooping(true);
//	                mediaPlayer.start();
//	            }
//	        });
//	    }

	
	private void initData(){
		loadimage();
	}
	
	
	private void initListener(){
		iv_back.setOnClickListener(this);
		tv_send_code.setOnClickListener(this);
		mVCodeImage.setOnClickListener(this);
		tv_explain.setOnClickListener(this);
		tvRegister.setOnClickListener(this);
		tv_explain_rule.setOnClickListener(this);
		chk_explain.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					isAgreeProtol=true;
				}else{
					isAgreeProtol=false;
				}
			}
		});
	}
	
	
	/**
	 * 加载验证码图片
	 */
	private void loadimage() {
		netService.setUrl(Constants.PIC_CODE_URL);
		netService.loader(new NetLoadListener() {
			
			@Override
			public void onloaderListener(BackData result) {
				if(result.getObject()!=null){
					String url=String.valueOf(result.getObject());
					//目前没有匹配成功 只需判空
//					if (LetvUtil.checkURL(url)) {
//						new requestGetverification().execute(url);
//					}
					if(!TextUtils.isEmpty(url)){
						new requestGetverification().execute(url);
					}
				}
			}
		});
	}
	
	/**
	 * 获取cookie
	 */
	private String getCaptchaId(HttpClient httpClient) {
		List<Cookie> cookies = ((AbstractHttpClient) httpClient)
				.getCookieStore().getCookies();
		String captchaId = null;
		for (int i = 0; i < cookies.size(); i++) {
			Cookie cookie = cookies.get(i);
			String cookieName = cookie.getName();
			if (!TextUtils.isEmpty(cookieName)
					&& cookieName.equals("captchaId")) {
				captchaId = cookie.getValue();
			}
		}
		return captchaId;
	}
	
	/**
	 * 获取到的图片和cookeId
	 *
	 */
	public class requestCodeImageData {
		public Bitmap bitmap;
		public String cookeid;
	}
	
	private class requestGetverification extends
			AsyncTask<String, Void, requestCodeImageData> {
			@Override
			protected requestCodeImageData doInBackground(String... params) {
			
				// baseUrl
				String baseUrl = params[0];
				requestCodeImageData data;
				// 将URL与参数拼接
				HttpGet getMethod = new HttpGet(baseUrl);
				HttpClient httpClient = new DefaultHttpClient();
				try {
					HttpResponse response = httpClient.execute(getMethod); // 发起GET请求
					if (response.getStatusLine().getStatusCode() == 200) {
						data = new requestCodeImageData();
						data.cookeid = getCaptchaId(httpClient);
						HttpEntity entity = response.getEntity();
						if (entity == null) {
			
						}
						InputStream is = null;
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						try {
							is = entity.getContent();
							byte[] buf = new byte[1024];
							int readBytes = -1;
							while ((readBytes = is.read(buf)) != -1) {
								baos.write(buf, 0, readBytes);
							}
						} finally {
							if (baos != null) {
								baos.close();
							}
							if (is != null) {
								is.close();
							}
						}
						byte[] imageArray = baos.toByteArray();
						data.bitmap = BitmapFactory.decodeByteArray(imageArray, 0,
								imageArray.length);
						return data;
					}
			
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			@Override
			protected void onPostExecute(requestCodeImageData data) {
				super.onPostExecute(data);
				if (data != null && data.bitmap != null) {
					mCookid = data.cookeid;
					mVCodeImage.setImageBitmap(data.bitmap);
				}
			
			}
	}
			
	/**
	 * 获取激活码
	 */
	private void doGetAuthCode() {
		//============================================================================
		if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
			  phoneNumber.requestFocus();
			  ToastUtil.showToast(RegisterActivity.this, R.string.phone_number_is_not_null);
			return;
		}
		//手机规则验证
/*		if (!CommonUtils.isRegexPhone(phoneNumber.getText().toString())) {
			  phoneNumber.requestFocus();
			  ToastUtil.showToast(RegisterActivity.this, R.string.phone_number_is_worng);
			return;
		}*/
		if (TextUtils.isEmpty(codes.getText().toString())) {
			codes.requestFocus();
			ToastUtil.showToast(RegisterActivity.this, R.string.vfcode_is_not_null);
			return;
		}
		if (codes.getText().toString().trim().length()!=4) {
			codes.requestFocus();
			ToastUtil.showToast(RegisterActivity.this, R.string.vfcode_is_not_right);
			return;
		}
		if (!ToastUtil.isMobileNO(phoneNumber.getText().toString())) {
			phoneNumber.requestFocus();
			ToastUtil.showToast(RegisterActivity.this, R.string.input_right_phone_num);
			return;
		}
		if (tv_send_code.isEnabled()) {
			startTimeTimer();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_explain_rule:
			Intent intent=new Intent();
			intent.setClass(RegisterActivity.this, AgreementActivity.class);
			startActivity(intent);
			break;		
		case R.id.tv_send_code:
			doGetAuthCode();
			break;
		case R.id.tv_explain:
			if(chk_explain.isChecked()){
				isAgreeProtol=false;
				chk_explain.setChecked(false);
			}else{
				isAgreeProtol=true;
				chk_explain.setChecked(true);
			}
			break;
		case R.id.iv_image:
			loadimage();
			break;
		case R.id.tv_register:
			if (!isAgreeProtol) {
				ToastUtil.showToast(RegisterActivity.this, R.string.agree_letv_protol);
				return;
			}
			if (!LetvUtil.isConnect(RegisterActivity.this)) {
				Toast.makeText(RegisterActivity.this, R.string.toast_net_null,
						Toast.LENGTH_SHORT).show();
				return;
			}
			doRegister();
			break;
		default:
			break;
		}
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		closeTimeTimer();
//
//	  mVideoView.stopPlayback();
	}

	/**
	 * 获取激活码
	 */
	private void startTimeTimer() {
		if (tv_send_code.isEnabled()) {
			tv_send_code.setEnabled(false);
			closeTimeTimer();
			mobile = phoneNumber.getText().toString();
			auth = CommonUtils.md5(Constants.KEY + mobile);
			plat = Constants.PLAT;
			captchaValue = codes.getText().toString();
			//https://sso.letv.com/sdk/clientSendMsg?mobile=13718396459&auth=9231e193b1cc56a296869e4b86f64c36&plat=sports_jinqu&action=reg&isCIBN=0&captchaValue=xvx8&captchaId=0057bfc18af4b16996736238f72a83cf
			String url = "https://sso.letv.com/sdk/clientSendMsg?mobile="
					+ mobile + "&auth=" + auth + "&plat=" + plat + "&action="
					+ "reg" + "&isCIBN=0&captchaValue=" + captchaValue
					+ "&captchaId=" + mCookid;

			mTimeTimer = new TimeTimer(url);
			mTimeTimer.execute();
		}
	}

	/**
	 * 停止计时
	 */
	private void closeTimeTimer() {
		if (null != mTimeTimer) {
			if (!mTimeTimer.isCancelled()) {
				mTimeTimer.cancel(true);
			}
			mTimeTimer = null;
		}
	}

	/**
	 * 15秒倒计时,获取激活码Task
	 */
	class TimeTimer extends AsyncTask<Void, Integer, Integer> implements
			TaskCallBack {

		private int times = 60;
		private String urlString;
		private int resultCode = -1;

		public TimeTimer(String url) {
			this.urlString = url;
		}

		@Override
		public void onPreExecute() {
			super.onPreExecute();
			tv_send_code.setEnabled(false);
			netService.setUrl(urlString);
			netService.loader(new NetLoadListener() {
				
				@Override
				public void onloaderListener(BackData result) {
				  Message msg=new Message();
				  msg.what=SEND_ACTIVATION;
				  Log.e("SSSSSSSSS", result.getObject().toString());
				  msg.obj=result;
				  
				  handler.sendMessage(msg);
				}
			});
		}

		@Override
		protected Integer doInBackground(Void... params) {
			while (times != 0) {
				publishProgress(times);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				times--;
			}
			return resultCode;
		}

		@Override
		public void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			tv_send_code.setText(String.valueOf(values[0])
					+ getString(R.string.registeractivity_second));
		}

		@Override
		public void onPostExecute(Integer result) {
			super.onPostExecute(result);
			tv_send_code.setText(getString(R.string.get_activation_code));
			tv_send_code.setEnabled(true);
		}

		@Override
		public void callback(int code, String msg, Object object) {
			if (object == null) {
				//激活码发送失败，请重试;
				ToastUtil.showToast(RegisterActivity.this,  R.string.get_mobile_code_fail);
				return;
			}
			final GetMobileCodeInfo mobileCodeInfo = (GetMobileCodeInfo) object;
			if (code == TaskCallBack.CODE_OK) {// 返回正确数据
				if (object instanceof ServerError) {
					//激活码发送失败，请重试;
					phoneNumber.requestFocus();
				} else {
					if (mobileCodeInfo.getStatus() != 1) {
						ToastUtil.showToast(RegisterActivity.this,
								mobileCodeInfo.getMessage());
						return;
					}
				}
			} else if (code == TaskCallBack.CODE_ERROR_DATA) {// 出错
				ToastUtil.showToast(RegisterActivity.this, R.string.get_mobile_code_fail);
			} else if (code == TaskCallBack.CODE_ERROR_NETWORK_CONNECT) { // 连接错误
				ToastUtil.showToast(RegisterActivity.this, R.string.get_mobile_code_fail);
			} else if (code == TaskCallBack.CODE_ERROR_NETWORK_NO) { // 无网络
				ToastUtil.showToast(RegisterActivity.this, R.string.get_mobile_code_fail);
			} else {
				ToastUtil.showToast(RegisterActivity.this, mobileCodeInfo.getMessage());
			}
		}

	}
	
	/**
	 * 注册
	 */
	private void doRegister() {
		String mobile = null;
		String password = null;
		String vcode = null;
		if (!checkPhoneFormat()) {
			return;
		}
		mobile = phoneNumber.getText().toString();
		vcode = activiation.getText().toString();
		password = userPassword.getText().toString();
		netService.setParams("mobile", mobile);
		//netService.setParams("password", CommonUtils.md5(password));
		netService.setParams("password", password);
		netService.setParams("plat", Constants.PLAT);
		netService.setParams("code", vcode);
		netService.setParams("clientip", Constants.getIpAddress(RegisterActivity.this));
		netService.setUrl(Constants.USER_REG_URL);
		netService.loader(new NetLoadListener() {			
			@Override
			public void onloaderListener(BackData result) {
				  Message msg=new Message();
				  msg.what=REGISTER;
				  msg.obj=result;
				  handler.sendMessage(msg);
			}
		});
	}
	
	
	/**
	 * 检查手机注册各项内容是否填写正确
	 */
	private boolean checkPhoneFormat() {

		if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
			// 手机号码不能为空
			ToastUtil.showToast(RegisterActivity.this, this.getResources()
					.getString(R.string.input_phone_num));
			phoneNumber.requestFocus();
			return false;
		}

		if (!LoginUtil.isMobileNO(phoneNumber.getText().toString())) {
			ToastUtil.showToast(RegisterActivity.this, "请输入正确的手机号码");
			phoneNumber.requestFocus();
			return false;
		}

		if (TextUtils.isEmpty(activiation.getText().toString().trim())) {
			String alert = getResources().getString(
					R.string.input_verify_num);
			ToastUtil.showToast(RegisterActivity.this, alert);
			activiation.requestFocus();
			return false;
		}

		if (TextUtils.isEmpty(userPassword.getText().toString())) {
			// 密码不能为空
			ToastUtil.showToast(RegisterActivity.this, getResources()
					.getString(R.string.input_pwd));
			userPassword.requestFocus();
			return false;
		}

		if (!LoginUtil.passwordFormat(userPassword.getText().toString())) {
			// 密码格式不正确
			ToastUtil.showToast(RegisterActivity.this, getResources()
					.getString(R.string.pwd_alert));
			userPassword.requestFocus();
			return false;
		}

		return true;
	}
	/**
	 * 提示注册信息成功
	 * 
	 * @param msg
	 */
	private void showDiaLog(String msg) {
		msgDialog = null;
		msgDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				msgDialog.dismiss();
				finish();
			}
		});
		msgDialog.setRemindMessage(msg);
		msgDialog.setRemindTitle("温馨提示");
		msgDialog.setConfirmTxt("确认");
		msgDialog.show();
	}
}
