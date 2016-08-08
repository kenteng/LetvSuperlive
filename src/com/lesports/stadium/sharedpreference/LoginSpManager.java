package com.lesports.stadium.sharedpreference;


import android.content.Context;

/**
 * SharedPreference管理类
 * @author 
 */
public class LoginSpManager {

    public final static String LOGIN_ACT = "login_act";
    public final static String LOGIN_USERNAME  = "username";
    public final static String LOGIN_NICKNAME  = "nickname";
    public final static String LOGIN_USERICON  = "usericon";
    public final static String LOGIN_USERID    = "userid";
    public final static String LOGIN_SSO_TOKEN = "sso_tk";
    public final static String LOGIN_AVATOR    = "avator";
    public final static String LOGIN_GENDER    = "gender";
    public final static String LOGIN_registIp    = "registIp";
    public final static String LOGIN_registTime    = "registTime";
    public final static String LOGIN_lastModifyTime    = "lastModifyTime";
    public final static String LOGIN_birthday    = "birthday";
    public final static String LOGIN_registService    = "registService";
    public final static String LOGIN_email    = "email";
    public final static String LOGIN_mobile    = "mobile";
    public final static String LOGIN_province    = "province";
    public final static String LOGIN_city    = "city";
    public final static String LOGIN_postCode    = "postCode";
    public final static String LOGIN_address    = "address";
    public final static String LOGIN_mac    = "mac";
    public final static String LOGIN_picture    = "picture";
    public final static String LOGIN_name    = "name";
    public final static String LOGIN_contactEmail    = "contactEmail";
    public final static String LOGIN_lastModifyPwdTime    = "lastModifyPwdTime";
    
    
    public final static String THIRD_UID          = "third_uid";
    public final static String THIRD_ACCESS_TOKEN = "access_token";
    public final static String THIRD_REMIND_IN    = "remind_in";
    public final static String THIRD_EXPIRES_IN   = "expires_in";

    public final static String SINA_ACCESS_TOKEN = "sina_access_token";
    public final static String SINA_UID          = "sina_uid";
    public final static String SINA_EXPIRE_TIME  = "expire_time";
    
    
    /** 设置新浪token */
    public static void setSinaToken(Context context, String sinaToken) {
        PreferenceUtil.putString(SINA_ACCESS_TOKEN, sinaToken, context);
    }
    
    /** 获取新浪token */
    public static String getSinaToken(Context context) {
        return PreferenceUtil.getString(SINA_ACCESS_TOKEN, "", context);
    }
//
//    /** 设置新浪失效时间 */
//    public static void setSinaExpireTime(Context context, long sinaExpireTime) {
//    	PreferenceUtil.putString(SINA_ACCESS_TOKEN, sinaToken, context);
//    }
//
//    /** 获取新浪失效时间 */
//    public static long getSinaExpireTime(Context context) {
//        return LoginSpHeplper
//                .getSharedPreferences(context, SINA_EXPIRE_TIME, 0l);
//    }

    /** 设置新浪uid */
    public static void setSinaUid(Context context, String value) {
        PreferenceUtil.putString(SINA_UID, value, context);
    }

    /** 获取新浪uid */
    public static String getSinaUid(Context context) {
        return PreferenceUtil.getString(SINA_UID, "", context);
    }
    public static void setLoginAct(Context context, String value) {
    	PreferenceUtil.putString(LOGIN_ACT, value, context);
    }
    public static String getLoginAct(Context context) {
        return PreferenceUtil.getString(LOGIN_ACT, "", context);
    }
    
    public static void setLoginUserName(Context context, String value) {
    	PreferenceUtil.putString(LOGIN_USERNAME, value, context);
    }

    public static String getLoginUserName(Context context) {
        return PreferenceUtil.getString(LOGIN_USERNAME, "", context);
    }

    public static void setLoginNickName(Context context, String value) {
        PreferenceUtil.putString(LOGIN_NICKNAME, value, context);
    }

    public static String getLoginNickName(Context context) {
        return PreferenceUtil.getString(LOGIN_NICKNAME, "", context);
    }

    public static void setLoginUserID(Context context, String value) {
    	PreferenceUtil.putString(LOGIN_USERID, value, context);
    }

    public static String getLoginUserID(Context context) {
        return PreferenceUtil.getString(LOGIN_USERID, "", context);
    }

    public static void setLoginAvator(Context context, String value) {
        PreferenceUtil.putString(LOGIN_AVATOR, value, context);
    }

    public static String getLoginAvator(Context context) {
        return PreferenceUtil.getString(LOGIN_AVATOR, "", context);
    }
    
    public static void setLoginGender(Context context, String value) {
    	PreferenceUtil.putString(LOGIN_GENDER, value, context);
    }

    public static String getLoginGender(Context context) {
        return PreferenceUtil.getString(LOGIN_GENDER, "", context);
    }
    
    public static void setLoginSsoToken(Context context, String value) {
        PreferenceUtil.putString(LOGIN_SSO_TOKEN, value, context);
    }

    public static String getLoginSsoToken(Context context) {
        return PreferenceUtil.getString(LOGIN_SSO_TOKEN, "", context);
    }
    
    public static void clearLoginCache(Context context) {
    	setLoginAct(context, "");
    	setLoginUserID(context, "");
    	setLoginUserName(context, "");
    	setLoginNickName(context, "");
    	setLoginSsoToken(context, "");
    }
    public static void logout(Context context) {
    	setLoginAct(context, "");
    	setLoginUserID(context, "");
    	setLoginUserName(context, "");
    	setLoginNickName(context, "");
    	setLoginSsoToken(context, "");
    	setSinaToken(context, "");
    	setSinaUid(context, "");
    	setLoginAvator(context, "");
    	setLoginGender(context, "");
//    	setLoginThirdAccessToken(context, "");
//    	setLoginThirdUid(context, "");
    	//setLoginBean(context, "");
//    	setLoginUserEmail(context, "");
//		setLoginUserMobile(context, "");
//		setLoginUserAddress(context, "");
//		setLoginUserCity(context, "");
    }
}
