package com.lesports.stadium.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.MainActivity;
import com.lesports.stadium.bean.LoginResultData;
import com.lesports.stadium.bean.LoginUserInfo;
import com.lesports.stadium.bean.WxTokenInfo;
import com.lesports.stadium.common.Constants;
import com.lesports.stadium.http.paser.GetWxTokenParser;
import com.lesports.stadium.http.paser.LetvLoginResultParser;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.utils.LoginUtil;
import com.lesports.stadium.utils.ToastUtil;
import com.letv.component.core.async.TaskCallBack;
import com.letv.component.core.http.bean.LetvBaseBean;
import com.letv.component.utils.DebugLog;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	public static final String TAG = "WXEntryActivity";
	private IWXAPI api;

	private static final int WX_LOGIN=0;
	
	private static final int LETV_LOGIN=1;
	
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			BackData result=(BackData)msg.obj;
			switch (msg.what) {
			case WX_LOGIN:				
				if (result.getObject()== null) {
					finish();
					return;
				}
				WxTokenInfo wxTokenInfo;
				try {
					wxTokenInfo = (WxTokenInfo)new GetWxTokenParser().parse(result.getObject());
					final String openid = wxTokenInfo.getOpenid();
					final String access_token = wxTokenInfo.getAccess_token();
					if (openid == null || access_token == null) {
						finish();
						return;
					}
					String plat = Constants.PLAT;
					String clientip = Constants.getIpAddress(WXEntryActivity.this);
					String url = "https://sso.letv.com/oauth/appssoweixin?plat="
							+ plat + "&access_token=" + access_token + "&openid="
							+ openid + "&clientip=" + clientip;
					letvLogin(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case LETV_LOGIN:				
				if (result.getObject() == null) {
					ToastUtil.showToast(WXEntryActivity.this,
							R.string.load_fail);
					finish();
					return;
				}
				LoginResultData datas = LetvLoginResultParser.parseReponseToLoginUserInfo(String.valueOf(result.getObject()));
				if(datas==null){
					Toast.makeText(WXEntryActivity.this, "服务器异常",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				if ("1001".equals(datas.getErrorCode())) {
					ToastUtil.showToast(WXEntryActivity.this,
							R.string.load_fail);
					return;
				}
				Log.i("ynn","wx:"+datas.getErrorCode()+"..."+datas.getBean());
				if ("0".equals(datas.getErrorCode())) {
					LoginUserInfo userInfo = datas.getBean();
					if (userInfo != null) {
						userInfo.sso_tk=datas.getSso_tk();
						LoginUtil.getInstance().operLoginResponse(
								WXEntryActivity.this, userInfo,
								Constants.APPWX);
//						closeLoginActivity();
					}
				}
				finish();
				break;
			default:
				break;
			}
			
		};
	};
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		String wx_app_id =Constants.APP_ID;
		setContentView(R.layout.login_weixin_entry_activity);
		api = WXAPIFactory.createWXAPI(this, wx_app_id, true);
		api.handleIntent(getIntent(), this);
		api.registerApp(wx_app_id);
	}

	@Override
	protected void onResume() {
		super.onResume();
		DebugLog.log(TAG, "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		DebugLog.log(TAG, "onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		DebugLog.log(TAG, "onDestroy");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		DebugLog.log(TAG, "onReq");
		finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		DebugLog.log(TAG, "wx_errorcode=" + resp.errCode);
		DebugLog.log(TAG, "type = " + resp.getType());
		int type = resp.getType();
		if (type == 1) {
			// 微信登录
			handleWeixinLoginResp(resp);
		} else if (type == 2) {
			// 微信分享
			handleWeixinShareResp(resp);
			finish();
		}

	}

	private void handleWeixinLoginResp(BaseResp resp) {
		if (resp == null)
			return;
		SendAuth.Resp authResp = (SendAuth.Resp) resp;
		String code = authResp.code;
		String result = null;
		if (authResp.errCode == BaseResp.ErrCode.ERR_OK) {
			operLogin(code);
		} else {
			switch (authResp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = "授权成功";
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = "取消授权";
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "授权失败";
				break;
			default:
				result = "授权异常";
				break;
			}
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
		}
	}

	/**
	 * 处理微信分享响应结果
	 * 
	 * @date:2014-8-16
	 * @time:下午2:20:41
	 */
	private void handleWeixinShareResp(BaseResp resp) {
		if (resp == null)
			return;
		String result = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "分享成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "取消分享";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "分享失败";
			Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	private void operLogin(String code) {
		String url ="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+Constants.APP_ID
		        +"&secret="+Constants.APP_SECRET+"&code="+code+"&grant_type=authorization_code"; 
		NetService netService=NetService.getInStance();
		netService.setUrl(url);
		netService.loader(new NetLoadListener() {
			
			@Override
			public void onloaderListener(BackData result) {
				Message  msg=new Message();
				msg.what=WX_LOGIN;
				msg.obj=result;
				handler.sendMessage(msg);
			}
		});
	}
	
	
	private void  letvLogin(String url){
		NetService netService=NetService.getInStance();
		netService.setUrl(url);
		netService.loader(new NetLoadListener() {
			
			@Override
			public void onloaderListener(BackData result) {
				Message  msg=new Message();
				msg.what=LETV_LOGIN;
				msg.obj=result;
				handler.sendMessage(msg);
			}
		});
	}

	private void closeLoginActivity() {		
		if (Constants.loginActivity != null) {
			Intent intent=new Intent(Constants.loginActivity, MainActivity.class);
			Constants.loginActivity.startActivity(intent);
			Constants.loginActivity.finish();
			Constants.loginActivity=null;
		}
	}
}
