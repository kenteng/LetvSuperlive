package com.lesports.stadium.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.RegisterActivity.requestCodeImageData;
import com.lesports.stadium.bean.LoginResultData;
import com.lesports.stadium.bean.LoginUserInfo;
import com.lesports.stadium.common.Constants;
import com.lesports.stadium.http.paser.LetvLoginResultParser;
import com.lesports.stadium.listener.SinaWbAuthListener;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.net.Code.HttpSetting;
import com.lesports.stadium.ui.ClearEditText;
import com.lesports.stadium.utils.CommonUtils;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.DensityUtil;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.LetvUtil;
import com.lesports.stadium.utils.LoginUtil;
import com.lesports.stadium.utils.ToastUtil;
import com.letv.component.utils.DebugLog;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * ***************************************************************
 * 
 * @ClassName: LoginActivity
 * 
 * @Desc : 登录
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 盛之刚
 * 
 * @data:2016-2-22 下午12:00:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *  ***************************************************************
 */
public class LoginActivity extends Activity implements  OnClickListener{

	/**
	 * 账号
	 */
	private ClearEditText accountNumber;
	
	/**
	 * 密码
	 */
	private ClearEditText password;
	
	/**
	 * 登录
	 */
	private TextView login;
	
	/**
	 * 注册
	 */
	private TextView register;
	
	/**
	 * 忘记密码
	 */
	private  TextView forgetPassword;
	
	/**
	 * QQ登录
	 */
	private LinearLayout qqLogin;
	
	/**
	 * 微信登录
	 */
	private LinearLayout wechatLogin;
			
	/**
	 * 新浪微博登录
	 */
	private LinearLayout sinaLogin;
	
	/**
	 * 网络请求
	 */
	private NetService netService;
	
	/**
	 * 账号登录
	 */
	public static final int ACCOUNT_LOGIN=1;
	
	/**
	 * 
	 */
	private Boolean isvfLogin;
	
	/**
	 * 授权Handler
	 */
	public SsoHandler mSsoHandler;
	
	/**
	 * 新浪微博登录锦亭
	 */
	private SinaWbAuthListener mSinaWbAuthListener;
	
	/**
	 * 用户名
	 */
	private String mUsername;
	
	/**
	 * 密码
	 */
	private String mPassword;
	
	/**
	 * 平台
	 */
	private String plat;
	
	/**
	 * IP地址
	 */
	private String ip;
	/**
	 * videoView
	 */
//	private VideoView mVideoView;
	
	public static final String VIDEO_NAME = "welcome_video.mp4";
	
	 private int mThirdFlag;
	 /**
	  * 存储需要输入验证码的账号
	  */
	 private ArrayList<String> codePhone = new ArrayList<>();
	 
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			BackData result=(BackData)msg.obj;
			switch (msg.what) {
			case ACCOUNT_LOGIN:				
				if (result.getObject() == null) {
					ToastUtil.showToast(LoginActivity.this,
							R.string.load_fail);
					return;
				} else {
					useWayHandleLoginData(result);
				}			
			break;
			default:
				break;
			}
		}
		/**
		 * 处理登陆结果数据
		 * @param result
		 */
		private void useWayHandleLoginData(BackData result) {
			// TODO Auto-generated method stub
			LoginResultData datas =  LetvLoginResultParser.parseReponseToLoginUserInfo(String.valueOf(result.getObject()));
			if(datas==null)
			{	
				Toast.makeText(getApplicationContext(), "登录异常", Toast.LENGTH_SHORT).show();
				return;
			}
			if (datas!=null&&datas.getErrorCode() != null) {
//				GlobalParams.SSO_TOKEN=datas.getSso_tk();
				if (datas.getErrorCode().equals("1000")) {
					ToastUtil.showToast(
							LoginActivity.this,
							R.string.param_error);
				}
				if (datas.getErrorCode().equals("1002")) {
					ToastUtil.showToast(
							LoginActivity.this,
							R.string.account_pwd_error);
				}
				if (datas.getErrorCode().equals("1003")) {
					ToastUtil.showToast(
							LoginActivity.this,
							R.string.user_shield);
				}
			
				if (datas.getErrorCode().equals("1005")) {
					ToastUtil.showToast(
							LoginActivity.this,
							R.string.request_illegal);
				}
				if (datas.getErrorCode().equals("1038")) {
					ToastUtil.showToast(
							LoginActivity.this,
							R.string.request_overnumber);
				}
				if (datas.getErrorCode().equals("1035")) {
					ToastUtil.showToast(
							LoginActivity.this,
							R.string.login_tomuch);
					loadimage();
					params.setMargins(0, DensityUtil.dip2px(getApplicationContext(), 20), 0, 0);
					login.setLayoutParams(params);
					rl_edtcodes.setVisibility(View.VISIBLE);
					if(!codePhone.contains(mUsername)){
						codePhone.add(mUsername);
					}
				}
			}
			if (datas.getBean() != null) {
				LoginUserInfo loginUser = datas.getBean();
				if (datas.getSso_tk() != null
						&& !"".equals(datas.getSso_tk())) {
					loginUser.sso_tk = datas.getSso_tk();
				}
				if (datas.getTv_token() != null
						&& !"".equals(datas.getTv_token())) {
					loginUser.tv_token = datas
							.getTv_token();
				}
				LoginUtil.getInstance().operLoginResponse(
						LoginActivity.this, loginUser,
						Constants.LETV);
//				Intent intent=new Intent(LoginActivity.this, MainActivity.class);
//				startActivity(intent);
//				finish();
			}
		};
	};

	private ImageView back;
	private InputMethodManager inputManager;// 软键盘管理类
	/**
	 * 验证码图片
	 */
	private ImageView mVCodeImage;
	/**
	 * 输入验证码
	 */
	private EditText codes;
	/**
	 * 包含验证码的布局
	 * 
	 */
	private RelativeLayout rl_edtcodes;
	/**
	 * 请求id
	 */
	private String mCookid;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		
		initBaseData();
		initView();
		initListener();
		inputManager = (InputMethodManager) this.getSystemService(
				"input_method");
		isInstallMQ();
	}

	/**
	 * 初始化基础数据
	 */
	private void initBaseData(){
		isvfLogin = true;
		Constants.loginActivity = this;
		netService=NetService.getInStance();
		mSinaWbAuthListener = new SinaWbAuthListener(this);
		AuthInfo mWeiboAuth = new AuthInfo(this,
				Constants.getSINA_APP_ID(this), Constants.URL_REDIRECT,Constants.SCOPE);
		mSsoHandler = new SsoHandler(this, mWeiboAuth);
	}
	
	/**
	 * 初始化View
	 */
	private void initView() {
//		 mVideoView = (VideoView) findViewById(R.id.videoView);
		
		accountNumber=(ClearEditText)findViewById(R.id.edt_account_number);
	    password=(ClearEditText)findViewById(R.id.edt_password);
	    login =(TextView)findViewById(R.id.tv_login);
	    register=(TextView)findViewById(R.id.tv_register);
		forgetPassword=(TextView)findViewById(R.id.tv_forget_password);
		qqLogin=(LinearLayout)findViewById(R.id.llyt_qq_login);
		wechatLogin=(LinearLayout)findViewById(R.id.llyt_wechat_login);
		sinaLogin=(LinearLayout)findViewById(R.id.llyt_sina_login);
		mVCodeImage = (ImageView)findViewById(R.id.iv_image);
		codes = (EditText)findViewById(R.id.edt_codes);
		rl_edtcodes = (RelativeLayout)findViewById(R.id.rl_edtcodes);
		
		back = (ImageView) findViewById(R.id.back);
//		
//		File videoFile = getFileStreamPath(VIDEO_NAME);
//        if (!videoFile.exists()) {
//            videoFile = copyVideoFile();
//        }
//
//        playVideo(videoFile);
	}
	
//	  private File copyVideoFile() {
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
	    
	    @Override
	    protected void onDestroy() {
	    	if(null!=codePhone){
	    		codePhone.clear();
	    		codePhone = null;
	    	}
	    	Constants.loginActivity = null;
	        super.onDestroy();
//	        mVideoView.stopPlayback();
	    }
	   private LayoutParams params;

	/**
	 * 初始化监听事件
	 */
	private void initListener(){
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		forgetPassword.setOnClickListener(this);
		qqLogin.setOnClickListener(this);
		wechatLogin.setOnClickListener(this);
		sinaLogin.setOnClickListener(this);
		back.setOnClickListener(this);
		mVCodeImage.setOnClickListener(this);
		params = (LayoutParams) login.getLayoutParams();
		accountNumber.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String phone = accountNumber.getText().toString();
				if(codePhone.contains(phone)){
					loadimage();
					params.setMargins(0, DensityUtil.dip2px(getApplicationContext(), 20), 0, 0);
					rl_edtcodes.setVisibility(View.VISIBLE);
				}else{
					params.setMargins(0, DensityUtil.dip2px(getApplicationContext(), 50), 0, 0);
					rl_edtcodes.setVisibility(View.GONE);
				}
				login.setLayoutParams(params);
			}
		});
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_login:
			if (!LetvUtil.isConnect(this)) {
				Toast.makeText(this, R.string.toast_net_null,
						Toast.LENGTH_SHORT).show();
				return;
			}
			inputManager.hideSoftInputFromWindow(LoginActivity.this.getWindow()
					.getDecorView().getWindowToken(), 0);
			if(rl_edtcodes.getVisibility()==View.VISIBLE){
				String condetext = codes.getText().toString();
				if(TextUtils.isEmpty(condetext)){
					Toast.makeText(this, R.string.vfcode_is_not_null,
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			logIn();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.tv_register:
			Intent registerIntent=new Intent();
			registerIntent.setClass(LoginActivity.this, RegisterActivity.class);
			startActivity(registerIntent);
			break;
		case R.id.tv_forget_password:
			Intent frogetPasswordIntent=new Intent();
			frogetPasswordIntent.setClass(LoginActivity.this, ForgetPasswordActivity.class);
			startActivity(frogetPasswordIntent);
			break;
		case R.id.llyt_qq_login:
			if (!LetvUtil.isConnect(this)) {
				Toast.makeText(this, R.string.toast_net_null,
						Toast.LENGTH_SHORT).show();
				return;
			}
			if(!GlobalParams.ISINSTALL_QQ){
				Toast.makeText(getApplicationContext(),R.string.n_qq , 0).show();
				return;
			}
			LoginUtil.loginByAct(this, Constants.APPQQ, null);
			break;
		case R.id.llyt_wechat_login:
			if(!GlobalParams.ISINSTALL_WX){
				Toast.makeText(getApplicationContext(),R.string.n_wx , 0).show();
				return;
			}
			
			IWXAPI api=WXAPIFactory.createWXAPI(LoginActivity.this, Constants.APP_ID);
			api.registerApp(Constants.APP_ID);
			SendAuth.Req req = new SendAuth.Req();
		    req.scope = "snsapi_userinfo";
		    req.state = "wechat_sdk_demo_test";
		    api.sendReq(req);
			
			
			break;
		case R.id.llyt_sina_login:			
		   		//mThirdFlag = 2;
		   		if (!LetvUtil.isConnect(this)) {
					Toast.makeText(this, R.string.toast_net_null,
							Toast.LENGTH_SHORT).show();
					return;
				}
		   		if(!GlobalParams.ISINSTALL_SN){
		   			Toast.makeText(this, R.string.n_sn,
							Toast.LENGTH_SHORT).show();
		   			return;
		   		}
				if (LoginUtil.isSupportSinaSso(this)) {
					mSsoHandler.authorize(mSinaWbAuthListener);
				} else
				{
		        String url = "http://sso.letv.com/oauth/appsina";
		        LetvThirdLoginActivity.launch(LoginActivity.this, 
		          url, 
		          getResources().getString(
		          R.string.login_weibo), 
		          "appsina");
		      }
			/*if (!LetvUtil.isConnect(this)) {
				Toast.makeText(this, R.string.toast_net_null,
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (LoginUtil.isSupportSinaSso(this)) {
				mSsoHandler.authorize(mSinaWbAuthListener);
			} else {
				String plat = Constants.PLAT;
				String url = "http://sso.letv.com/oauth/appsina?display="
						+ Constants.DISPLAY + "&plat=" + plat;
				LetvQQH5LoginActivity
						.launch(this,
								url,
								this.getResources().getString(
										R.string.login_weibo),
										Constants.APPSINA);
			}*/
			break;
		case R.id.iv_image:
			loadimage();
			break;
		default:
			break;
		}
	}

	/**
	 * 登录
	 */
	private void logIn() {
		plat = Constants.PLAT;
		mUsername = accountNumber.getText().toString();
		mPassword = password.getText().toString();
		ip = Constants.getIpAddress(this);

		if (checkLogin()) {
			netService.setParams("loginname", mUsername);
			//netService.setParams("password", CommonUtils.md5(mPassword));
			netService.setParams("password", mPassword);
			netService.setParams("plat", plat);
			netService.setParams("ip", ip);
			Log.i("wxn", "ip:"+ip);
			if(rl_edtcodes.getVisibility() ==View.VISIBLE){
				netService.setParams("verify", codes.getText().toString().trim()); //用户输入的验证码
				netService.setParams("captchaId", mCookid);
			}
			netService.setHttpMethod(HttpSetting.POST_MODE);
			netService.setUrl(Constants.BASE_LOGIN_URL);			
			netService.loader(new NetLoadListener() {				
				@Override
				public void onloaderListener(BackData result) {
					Message msg=new Message();
					msg.what=ACCOUNT_LOGIN;
					msg.obj=result;
					handler.sendMessage(msg);
				}
			});
		}
	}
	
	/**
	 * 检测登录
	 * @return
	 */
	@SuppressLint("ResourceAsColor")
	private boolean checkLogin() {
		if (TextUtils.isEmpty(mUsername)) {
			String text = getString(R.string.name_null);
			ToastUtil.showToast(this, text);
			accountNumber.requestFocus();
			return false;
		} 
		
		if (TextUtils.isEmpty(mPassword)) {
			String text = getString(R.string.pword_null);
			ToastUtil.showToast(this, text);
			password.requestFocus();
			return false;
		} 
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//DebugLog.log(TAG, "onActivityResult");
		if (mSsoHandler != null&& data!=null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
		//DebugLog.log(TAG, "onActivityResult2");
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
	
	/**
	 * 判断是否安装微信 和ＱＱ
	 */
	private void isInstallMQ(){
		List<ResolveInfo> sharApps = CommonUtils.getShareApps(this);
		if(sharApps!=null){
			GlobalParams.ISINSTALL_WX = false;
			GlobalParams.ISINSTALL_QQ = false;
			GlobalParams.ISINSTALL_SN = false;
			for(ResolveInfo apps:sharApps){
				if("com.tencent.mm".equals(apps.activityInfo.packageName)){
					//已安装微信
					GlobalParams.ISINSTALL_WX = true;
				}else if("com.tencent.mobileqq".equals(apps.activityInfo.packageName)){
					//已安装QQ
					GlobalParams.ISINSTALL_QQ = true;
				}else if("com.sina.weibo".equals(apps.activityInfo.packageName)){
					//已安装新浪
					GlobalParams.ISINSTALL_SN = true;
				}
			}
		}
	}
	
	

}
