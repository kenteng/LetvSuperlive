package com.lesports.stadium.common;

import com.lesports.stadium.utils.LetvUtil;
import com.lesports.stadium.utils.LoginUtil;

import android.app.Activity;
import android.content.Context;


public class Constants {
	public static final String TAG = "LetvLoginActivity";
	/**
	 * 微信APP_ID
	 */
    public static final String APP_ID = "wxfc6f6b638673ad86";   
    
    /**
     * 微信
     */
    public static final String  APP_SECRET="286e0ab9b5ad8d49e544d12532fb14f8";
    /**
     * 新浪登录
     */
	public static final String APPSINA = "appsina";
	
	/**
	 * QQ登录
	 */
	public static final String APPQQ   = "appqq";
	
	/**
	 * 微信登录
	 */
	public static final String APPWX   = "appweixin";
	
	/**
	 * 平台标示,测试使用,发布时要换成自己的plat
	 */
    public static String PLAT ="sports_jinqu";
    
    /**
     * 
     */
    public static String DISPLAY ="mobile";
    
    /**
     * key
     */
    public static String KEY ="!@ssoclientmg";
    
    /**
     * 乐视TV
     */
    public static final String LETV    = "letv";
    /**
     * 注册
     */
    public static String USER_REG_URL = "https://sso.letv.com/sdk/reg";   
    
    /**
     * 登录
     */
    public static String BASE_LOGIN_URL = "https://sso.letv.com/sdk/login";
    
    /**
     * 新浪微博
     */
    public static String PACKAGE_NAME_SINA = "com.sina.weibo";
    
    /**
     * QQ登录
     */
    public static String PACKAGE_NAME_QQ = "com.tencent.mobileqq";   
    
    /**
     * 默认地址
     */
//    public static String URL_REDIRECT = "http://m.letv.com";
    public static String URL_REDIRECT = "http://sso.letv.com/oauth/appsinacallbackdata";
    
    /**
     * 图片验证吗
     */
    public static String PIC_CODE_URL ="https://sso.letv.com/sdk/getCaptcha";
    
    /**
     * 第三方登录
     */
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    
    /**
     * 发送短信找回密码 手机号
     */
    public static final String retrievePwdPhoneNum = "1069032901305721";
    
    /**
     * 登录activity
     */
    public static Activity loginActivity = null;
    
    /**
     * 获取ip
     * @param context
     * @return
     */
    public static String getIpAddress(Context context) {
    	return LetvUtil.getLocalIpAddress(context);
    }
    
    /**
     * 获得qq登陆的appid
     * @param context
     * @return
     */
    public static String getQQ_APP_ID(Context context) {
//    	return QQ_APP_ID;
    	return LoginUtil.getNetConfigProperties().getProperty("QQ_APP_ID");
    }
    
    /**
     * 获得wx登陆的appid
     * @param context
     * @return
     */
    public static String getWX_APP_ID(Context context) {
//    	return WEIXIN_APP_ID;
    	return LoginUtil.getNetConfigProperties().getProperty("WEIXIN_APP_ID");
    }
    
    
    /**
     * 获得sina登陆的appid
     * @param context
     * @return
     */
    public static String getSINA_APP_ID(Context context) {
//    	return SINA_APP_ID;
    	return LoginUtil.getNetConfigProperties().getProperty("SINA_APP_ID");
    }
    
    /**
     * 获得sina登陆的SECRET
     * @param context
     * @return
     */
    public static String getSINA_APP_KEY(Context context) {
//    	return SINA_APP_SECRET;
    	return LoginUtil.getNetConfigProperties().getProperty("SINA_APP_SECRET");
    }
}
