package com.lesports.stadium.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.LetvQQH5LoginActivity;
import com.lesports.stadium.activity.MainActivity;
import com.lesports.stadium.bean.LoginUserInfo;
import com.lesports.stadium.common.Constants;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.Code.HttpSetting;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.sharedpreference.LoginSpManager;
import com.letv.component.utils.DebugLog;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class LoginUtil {
	/**
	 * 网络请求
	 */
	private NetService netService;
	public static final String TAG = "LetvLoginActivity";
	private static Intent afterLoginTointent = null;
	// 消息同步到后台成功
	private final int SUCCESS_SYNC = 1111;
	private final int SUCCESS_GET_MESSAGE = 1000;
	private final int FILE = 4444;
	private static LoginUtil instance = new LoginUtil();
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_SYNC:
				// 获取用户消息
				getUserMessage((String) msg.obj);
				break;
			case SUCCESS_GET_MESSAGE:
				analyDate(msg.obj);
				break;
			case FILE:
				GlobalParams.USER_ID = "";
				GlobalParams.SSO_TOKEN = "";
				Toast.makeText(MainActivity.instance, "服务器异常", 0).show();
				break;

			default:
				break;
			}
		};
	};

	private LoginUtil() {
	}

	public static LoginUtil getInstance() {
		return instance;
	}

	/**
	 * 设置登陆成功后的跳转目的inten，如果afterLoginTointent==null，则不进行跳转
	 * 
	 * @param intent
	 */
	private static void setAfterLoginTointent(Intent intent) {
		afterLoginTointent = intent;
	}

	@SuppressWarnings("unused")
	private static void setAfterLoginTointent() {
		setAfterLoginTointent(null);
	}

	public static Intent getAfterLoginTointent() {
		return afterLoginTointent;
	}

	/**
	 * 验证注册手机号码是否正确
	 */
	public static boolean isMobileNO(String mobiles) {
		if (mobiles == null) {
			return false;
		}
		Pattern p = Pattern.compile("^(1)\\d{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		// 得到画布
		Canvas canvas = new Canvas(output);

		// 将画布的四角圆化
		final int color = Color.RED;
		final Paint paint = new Paint();
		// 得到与图像相同大小的区域 由构造的四个值决定区域的位置以及大小
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		// 值越大角度越明显
		final float roundPx = 18;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// drawRoundRect的第2,3个参数一样则画的是正圆的一角，如果数值不同则是椭圆的一角
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 验证密码是否是 数字和字母，长度6-16,区分大小写
	 */
	public static boolean passwordFormat(String password) {
		if (password == null)
			return false;
		String regular = "^[a-zA-Z0-9_]{6,16}$";
		Pattern pattern = Pattern.compile(regular);
		Matcher matcher = pattern.matcher(password);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 验证邮箱格式是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean emailFormats(String email) {
		if (email == null)
			return false;
		String regular = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		Pattern pattern = Pattern.compile(regular);
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 短信找回密码
	 */
	public static void retrievePwdBySMS(Context context, String phonenumber) {
		Uri smsToUri = Uri.parse("smsto:" + phonenumber);
		Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		mIntent.putExtra("sms_body", "");
		context.startActivity(mIntent);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static boolean packageIsExist(Context context, String packageName) {
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> apps = pm.getInstalledApplications(0);
		if (null != apps) {
			int size = apps.size();
			for (int i = 0; i < size; i++) {
				if (packageName.equals(apps.get(i).packageName))
					return true;
			}

		}
		return false;
	}

	/**
	 * 通过新浪微博登陆
	 * 
	 * @param context
	 */
	public static void loginByAct(final Activity context, final String act,
			Dialog dialog) {
		if (act == null) {
			return;
		}
		if (act.equals(Constants.APPSINA)) {
			// Intent intent = new Intent(context, SinaLoginActivity.class);
			// context.startActivity(intent);
		} else if (act.equals(Constants.APPWX)) {
			String wx_app_id = Constants.getWX_APP_ID(context);
			IWXAPI api = WXAPIFactory.createWXAPI(context, wx_app_id, false);
			api.registerApp(wx_app_id);
			if (!api.isWXAppInstalled()) {
				Toast.makeText(context, "请去下载微信", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!api.isWXAppSupportAPI()) {
				Toast.makeText(context, "请更新您的微信版本", Toast.LENGTH_LONG).show();
			} else {
				final SendAuth.Req req = new SendAuth.Req();
				req.scope = "snsapi_userinfo";
				req.state = "letv_lepai";
				api.sendReq(req);
			}
		} else if (act.equals(Constants.APPQQ)) {
			if (LoginUtil.packageIsExist(context, Constants.PACKAGE_NAME_QQ)) {
				QQLoginUtil.getInstance(context).logIn(dialog);
			} else {
				String plat = Constants.PLAT;
				String url = "https://sso.letv.com/oauth/appqq?display="
						+ Constants.DISPLAY + "&plat=" + plat;
				LetvQQH5LoginActivity.launch(context, url, context
						.getResources().getString(R.string.login_qq), act);
			}
		}
	}

	public static Properties getNetConfigProperties() {
		Properties props = new Properties();
		InputStream in = LoginUtil.class
				.getResourceAsStream("/loginsdk.properties");
		try {
			props.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}

	public static void setAuthSinaInfo(Context context, String accessSinaToken) {
		if (accessSinaToken != null && !accessSinaToken.equals("")) {
			LoginSpManager.setSinaToken(context, accessSinaToken);
		}
	}

	public static boolean isSupportSinaSso(Context context) {
		IWeiboShareAPI iWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context,
				Constants.getSINA_APP_KEY(context), false);
		iWeiboShareAPI.registerApp();
		boolean weiboAppInstalled = iWeiboShareAPI.isWeiboAppSupportAPI();
		// Class pluginUtil_clazz = JarLoader.loadClass(context,
		// JarConstant.LETV_SHARE_NAME, JarConstant.LETV_SHARE_PACKAGENAME,
		// "sina.util.PluginUtil");
		// boolean weiboAppInstalled = (Boolean)
		// JarLoader.invokeStaticMethod(pluginUtil_clazz,
		// "isWeiboAppSupportAPI", new Class[] { Context.class, String.class,
		// boolean.class }, new Object[] {
		// context, Constants.APP_KEY, false });
		return weiboAppInstalled;
	}

	public void operLoginResponse(Context context, LoginUserInfo loginUser,
			String state) {
		// 将token赋值给全局静态变量，用于网络请求时候传递
		GlobalParams.SSO_TOKEN = loginUser.sso_tk;
		// synchroMessage(loginUser);
		getUserMessage(loginUser.uid);
		LoginSpManager.clearLoginCache(context);
		String nickname = loginUser.nickname;
		try {
			nickname = new String(nickname.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LoginSpManager.setLoginAct(context, state);
		LoginSpManager.setLoginUserID(context, loginUser.uid);
		LoginSpManager.setLoginUserName(context, loginUser.username);
		LoginSpManager.setLoginNickName(context, nickname);
		LoginSpManager.setLoginSsoToken(context, loginUser.sso_tk);
		LoginSpManager.setLoginAvator(context, loginUser.avatar);
		LoginSpManager.setLoginGender(context, loginUser.gender);
		if (loginUser.sinaToken != null && !loginUser.sinaToken.equals("")) {
			LoginSpManager.setSinaToken(context, loginUser.sinaToken);
		}
		if (loginUser.sinaUid != null && !loginUser.sinaUid.equals("")) {
			LoginSpManager.setSinaUid(context, loginUser.sinaUid);
		}
		if (afterLoginTointent != null) {
			DebugLog.log(TAG, "start to afterLoginTointent"
					+ afterLoginTointent.getAction());
			context.startActivity(afterLoginTointent);
			afterLoginTointent = null;
		}
		JPushUtils.setAlias(loginUser.uid);
		// if (Constants.loginActivity != null) {
		// DebugLog.log(TAG, "loginActivity finish");
		// Constants.loginActivity.finish();
		// Constants.loginActivity=null;
		// }
	}

	/**
	 * 将乐视消息同步到后台
	 */
	@SuppressWarnings("unused")
	private void synchroMessage(final LoginUserInfo loginUser) {
		netService = NetService.getInStance();
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(loginUser.uid);
		if (isNum.matches())
			netService.headers.setUid(Integer.parseInt(loginUser.uid));
		netService.setParams("uid", loginUser.uid);
		netService.setParams("username", loginUser.username);
		netService.setParams("nickname", loginUser.nickname);
		netService.setParams("email", loginUser.email);
		netService.setParams("mobile", loginUser.mobile);
		// netService.setParams("gender", loginUser.gender);
		netService.setParams("picture", loginUser.picArray == null ? ""
				: loginUser.picArray[0]);
		netService.setHttpMethod(HttpSetting.POST_MODE);
		netService.setUrl(ConstantValue.LS_MESSAGE);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				if (result != null && result.getNetResultCode() == 0) {
					Message msg = new Message();
					msg.what = SUCCESS_SYNC;
					msg.obj = loginUser.uid;
					handler.sendMessage(msg);
				} else {
					handler.sendEmptyMessage(FILE);
				}
			}
		});
	}

	/**
	 * 获取后台用户消息
	 */
	private void getUserMessage(String uid) {
		GlobalParams.USER_ID = uid;
		netService = NetService.getInStance();
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(uid);
		if (isNum.matches()) {
			netService.headers.setUid(Integer.parseInt(uid));
		}
		netService.setHttpMethod(HttpSetting.POST_MODE);
		netService.setUrl(ConstantValue.PERSION_MESSAGE);
		Log.i("ynn", "getUserMessage..."+GlobalParams.SSO_TOKEN);
		netService.loader(new NetLoadListener() {
			@Override
			public void onloaderListener(BackData result) {
				Log.i("wxn", "后台获取消息："+result.getNetResultCode()+"..."+result.getObject());
				if (result != null && result.getNetResultCode() == 0) {
					Object obj = result.getObject();
					Message msg = new Message();
					msg.what = SUCCESS_GET_MESSAGE;
					msg.obj = obj;
					handler.sendMessage(msg);
				} else {
					handler.sendEmptyMessage(FILE);
				}
			}
		});
	}

	private void analyDate(Object obj) {
		if (obj != null) {
			try {
				String content = String.valueOf(obj);
				Log.i("wxn", "后台消息同步。。。" + obj);
				JSONObject temp = new JSONObject(content);

				if (temp.has("mobile")) {
					String mobile = temp.getString("mobile");
					GlobalParams.USER_PHONE = mobile;
					SharedPreferencesUtils.saveData(GlobalParams.context,
							"ls_user_message", "mobile", mobile);
				}
				if (temp.has("nickname")) {
					String nickname = temp.getString("nickname");
					GlobalParams.USER_NAME = nickname;
					SharedPreferencesUtils.saveData(GlobalParams.context,
							"ls_user_message", "nickname", nickname);
				}
				if (temp.has("email")) {
					String email = temp.getString("email");
					GlobalParams.USER_EMIL = email;
					SharedPreferencesUtils.saveData(GlobalParams.context,
							"ls_user_message", "email", email);
				}
				if (temp.has("picture")) {
					String pictrue = temp.getString("picture");
					GlobalParams.USER_HEADER = pictrue;
					SharedPreferencesUtils.saveData(GlobalParams.context,
							"ls_user_message", "picture", pictrue);
				}
				if (temp.has("gender")) {
					String gender = temp.getString("gender");
					GlobalParams.USER_GENDER = gender;
					SharedPreferencesUtils.saveData(GlobalParams.context,
							"ls_user_message", "gender", gender);
				}
				// if(temp.has("id")){
				// String id = temp.getString("id");
				// GlobalParams.USER_ID= id;
				// SharedPreferencesUtils.saveData(GlobalParams.context,
				// "ls_user_message", "id", id);
				// }
				if (temp.has("address")) {
					String adress = temp.getString("address");
					GlobalParams.USER_ADDRESS = adress;
					SharedPreferencesUtils.saveData(GlobalParams.context,
							"ls_user_message", "address", adress);
				}
				SharedPreferencesUtils.saveData(GlobalParams.context,
						"ls_user_message", "id", GlobalParams.USER_ID);
				SharedPreferencesUtils.saveData(GlobalParams.context,
						"ls_user_message", "sso_token", GlobalParams.SSO_TOKEN);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (Constants.loginActivity != null) {
				DebugLog.log(TAG, "loginActivity finish");
				Constants.loginActivity.finish();
				Constants.loginActivity = null;
			}
		}
	}

}
