package com.lesports.stadium.utils;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.LoginResultData;
import com.lesports.stadium.bean.LoginUserInfo;
import com.lesports.stadium.common.Constants;
import com.lesports.stadium.http.paser.LetvLoginResultParser;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.net.Code.HttpSetting;
import com.letv.component.utils.DebugLog;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * qq 第三方登陆
 * 
 * @author fjh
 */
public class QQLoginUtil {
	private static final String TAG = "QQLoginUtil";
	private String mAppid;
	private QQAuth mQQAuth;
	private Tencent mTencent;
	private Context context;
	private static QQLoginUtil instance;

	private QQLoginUtil() {
		super();
	}

	private QQLoginUtil(Context context) {
		super();
		this.context = context;
		init();
	}

	public synchronized static QQLoginUtil getInstance(Context context) {
		if (instance == null) {
			instance = new QQLoginUtil(context);
		}
		return instance;
	}

	/**
	 * 构造完成之后，初始化操作
	 */
	private void init() {
		mAppid = Constants.getQQ_APP_ID(context);
		mQQAuth = QQAuth.createInstance(mAppid, context);
		mTencent = Tencent.createInstance(mAppid, this.context);
	}

	public void logOut() {
		if (mQQAuth.isSessionValid()) {
			mQQAuth.logout((Activity) context);
		}
	}

	public void logIn(final Dialog dialog) {
		if (mQQAuth == null) {
			return;
		}
		if (!mQQAuth.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					DebugLog.log(TAG, "doComplete=" + values.toString());
					String openid = "";
					int ret = -1;
					String access_token = "";
					try {
						ret = values.getInt("ret");
						openid = values.getString("openid");
						access_token = values.getString("access_token");
						Log.i(Constants.TAG, "qqopenId_ = " + openid);
					    Log.i(Constants.TAG, "qqaccess_token_ = " + access_token);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (ret != 0) {
						return;
					}
					String plat=Constants.PLAT;
                	String appid=Constants.getQQ_APP_ID(context);
                	String clientip=Constants.getIpAddress(context);
                	String url="https://sso.letv.com/oauth/appssoqq?plat="+plat+"&access_token="+access_token+"&openid="+openid+"&appkey="+appid+"&clientip="+clientip;
					NetService netService=NetService.getInStance();
					netService.setHttpMethod(HttpSetting.GET_MODE);
					netService.setUrl(url);
					netService.loader(new NetLoadListener() {
						
						@SuppressWarnings("unused")
						@Override
						public void onloaderListener(BackData result) {
							
							if (dialog!=null) {
								dialog.dismiss();
							}
							
							if (result == null) {
								ToastUtil.showToast(context, R.string.load_fail);
								return;
							}
							LoginResultData datas =LetvLoginResultParser.parseReponseToLoginUserInfo(String.valueOf(result.getObject()));
							Log.i("ynn", "..QQ..1"+"errorcode"+datas.getErrorCode()+"");
							if(datas==null){
								Toast.makeText(context, "服务器异常",
										Toast.LENGTH_SHORT).show();
								return;
							}
							if ("1001".equals(datas.getErrorCode())) {
								Toast.makeText(context, R.string.load_fail,
										Toast.LENGTH_SHORT).show();
							}
							if ("0".equals(datas.getErrorCode())) {
								Log.i("ynn", ".QQ...2:"+datas.getBean());
								LoginUserInfo userInfo = datas
										.getBean();
								if (userInfo != null) {
									userInfo.sso_tk=datas.getSso_tk();
									Log.i("ynn", "..@@..3");
									LoginUtil.getInstance().operLoginResponse(
											context, userInfo,
											Constants.APPQQ);
								}
							}
						}
					});
				}
			};
			mQQAuth.login((Activity) context, "all", listener);
			mTencent.login((Activity) context, "all", listener);
		} else {
			mQQAuth.logout((Activity) context);
		}
	}

	/**
	 * @param jsonObject
	 */
	// public static void initOpenidAndToken(JSONObject jsonObject) {
	// try {
	// String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
	// String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
	// String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
	// if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
	// && !TextUtils.isEmpty(openId)) {
	// mTencent.setAccessToken(token, expires);
	// mTencent.setOpenId(openId);
	// }
	// } catch (Exception e) {
	// }
	// }

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {
		}

		@Override
		public void onError(UiError e) {
		}

		@Override
		public void onCancel() {
		}
	}
}
