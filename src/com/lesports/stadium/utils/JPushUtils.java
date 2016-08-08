package com.lesports.stadium.utils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Notification;
import android.util.Log;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.lesports.stadium.app.LApplication;

public class JPushUtils {

	/**
	 * 设置alias
	 * @param alias
	 */
	public static void setAlias(String alias) {
		if(!isValidTagAndAlias(alias)){
			return;
		}
		JPushInterface.setAlias(LApplication.getContext(), alias,
				new TagAliasCallback() {
					@Override
					public void gotResult(int arg0, String arg1,
							Set<String> arg2) {
						// arg0 为0 代表成功
						Log.e("LLL", arg0 + "该数值为0表示alias设置成功");
					}
				});
	}

	/**
	 * 校验Tag Alias 只能是数字,英文字母和中文
	 * @param s
	 * @return
	 */
	public static boolean isValidTagAndAlias(String s) {
		Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
	/**
	 * 修改自定义通知栏消息的样式   服务器发送时指定1 说明使用该样式
	 * @param isSound	是否声音提示
	 * @param isVibrate 是否震动提示
	 */
	public static void showBasicPushNotificationBuilderStyle(boolean isSound, boolean isVibrate, int statusBarDrawable){
		BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(LApplication.getContext());
		builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;	//设置为自动消失
		if(isSound){
			builder.notificationDefaults = Notification.DEFAULT_SOUND;	// 设置为铃声与震动都要
		}
		if(isVibrate){
			builder.notificationDefaults = Notification.DEFAULT_VIBRATE;	// 设置为铃声与震动都要
		}
		if(isSound&&isVibrate){
			builder.notificationDefaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;	// 设置为铃声与震动都要
		}
		if(!isSound&&!isVibrate){
			builder.notificationDefaults = Notification.DEFAULT_LIGHTS;	// 设置为铃声与震动都要
		}
		builder.statusBarDrawable = statusBarDrawable;
		
		JPushInterface.setPushNotificationBuilder(1, builder);
	}
}
