package com.lesports.stadium.listener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.lesports.stadium.bean.LoginResultData;
import com.lesports.stadium.bean.LoginUserInfo;
import com.lesports.stadium.common.Constants;
import com.lesports.stadium.http.paser.LetvLoginResultParser;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.net.NetLoadListener;
import com.lesports.stadium.net.NetService;
import com.lesports.stadium.utils.AccessTokenKeeper;
import com.lesports.stadium.utils.LoginUtil;
import com.letv.component.utils.DebugLog;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;

/**
 * 新浪微博监听器
 * @author fengjianhua
 */
public class SinaWbAuthListener implements WeiboAuthListener {
	
	public static final String TAG = "SinaWbAuthListener";
	
	private Context context;
	private Activity mActivity;
	private Dialog mDialog;
	private boolean share;
	
	public SinaWbAuthListener(Context context) {
		this.context = context;
	}
	public void setDialog(Dialog dialog) {
		this.mDialog = dialog;
	}
	public SinaWbAuthListener(Context context,Activity activity) {
		this.context = context;
		this.mActivity = activity;
	}
	public SinaWbAuthListener(Context context,boolean share,Activity activity) {
		this.context = context;
		this.share =  share;
		this.mActivity = activity;
	}
    @Override
    public void onComplete(Bundle values) {
        final Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
        if (accessToken != null && accessToken.isSessionValid()) {
			AccessTokenKeeper.writeAccessToken(context, accessToken);
			String accessSinaToken = AccessTokenKeeper.readAccessToken(context).getToken();
			if(share){
			   LoginUtil.setAuthSinaInfo(context, accessSinaToken);
			   if(mActivity!=null){
		          mActivity.finish();
		       }
			   return;
			}
		    // 获取用户信息接口 
		    new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... arg0) {
	                DebugLog.log(TAG, " doInBackground onComplete");
	                UsersAPI  mUsersAPI = new UsersAPI(context, Constants.getSINA_APP_ID(context), accessToken);
			        if(null != accessToken){
			        	long uid = Long.parseLong(accessToken.getUid());
			        	mUsersAPI.show(uid, userInfoListenerListener);
			        }
					return null;
				}
				protected void onPostExecute(Void result) {
	                DebugLog.log(TAG, "onPostExecute");
				};
		   }.execute();
            
        }
    }

    @Override
    public void onWeiboException(WeiboException e) {
        Toast.makeText(context, "登录异常", Toast.LENGTH_LONG).show();
        if(mActivity!=null){
           mActivity.finish();
        }
    }

    @Override
    public void onCancel() {
    	if(mDialog!=null){
            mDialog.dismiss();
         }
        Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show();
        if(mActivity!=null){
            mActivity.finish();
         }
    }
    
    private RequestListener userInfoListenerListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
//            	Toast.makeText(context, "新浪授权成功", Toast.LENGTH_SHORT).show();
                User user = User.parse(response);
                if (user != null) {
                	String plat=Constants.PLAT;
                	String token=AccessTokenKeeper.readAccessToken(context).getToken();
                	String uid=user.id;
                	Log.i(Constants.TAG, "sinaopenId_ = " + uid);
    			    Log.i(Constants.TAG, "sinaaccess_token_ = " + token);
                	String clientip=Constants.getIpAddress(context);
                	String url="http://sso.letv.com/oauth/appssosina?plat="+plat+"&access_token="+token+"&uid="+uid+"&clientip="+clientip;
                	NetService netService=NetService.getInStance();
                	netService.setUrl(url);
                	netService.loader(new NetLoadListener() {
						
						@Override
						public void onloaderListener(BackData result) {
							if (result==null) {
								Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT).show();
								return;
							}
							LoginResultData datas=LetvLoginResultParser.parseReponseToLoginUserInfo(String.valueOf(result.getObject()));
							if(datas==null){
								Toast.makeText(context, "服务器异常",
										Toast.LENGTH_SHORT).show();
								return;
							}
							if ("1001".equals(datas.getErrorCode())) {
								Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
							}
							Log.i("ynn","sina:"+datas.getErrorCode()+"..."+datas.getBean());
							if ("0".equals(datas.getErrorCode())) {
								LoginUserInfo userInfo=datas.getBean();
								if (userInfo!=null) {
									userInfo.sso_tk=datas.getSso_tk();
//									Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
									userInfo.sinaToken=AccessTokenKeeper.readAccessToken(context).getToken();
									LoginUtil.getInstance().operLoginResponse(context, userInfo, Constants.APPSINA);
//		            		        if(mActivity!=null){
//		            		           mActivity.finish();
//		            		        }
								}
							}
						}
					});
                } else {
                	if(mDialog!=null){
                       mDialog.dismiss();
                    }
                    Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            LogUtil.e(TAG, e.getMessage());
            if(mDialog!=null){
               mDialog.dismiss();
            }

            if(mActivity!=null){
                mActivity.finish();
             }
        }
    };
}
