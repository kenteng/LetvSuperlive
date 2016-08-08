package com.lesports.stadium.utils;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * 
 * @ClassName:  SharedPreferencesUtils   
 * @Description:使用SharedPreferences 保存临时数据  
 * @author: 王新年 
 * @date:   2015-12-28 下午5:59:02   
 *
 */
public class SharedPreferencesUtils {
	private static SharedPreferences sharedPreferences;

	/**
	 * 存储字符串类型数据
	 * 
	 * @param SPName
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveData(Context context, String SPName, String key,
			String value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SPName,
					Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putString(key, value).commit();
	}

	/**
	 * 读取字符串类型数据
	 * 
	 * @param SPName
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getData(Context context, String SPName, String key,
			String defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SPName,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.getString(key, defValue);
	}

	/**
	 * 存储boolean类型的值
	 * 
	 * @param SPName
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveData(Context context, String SPName, String key,
			boolean value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SPName,
					Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putBoolean(key, value).commit();
	}

	/**
	 * 读取boolean类型的值
	 * 
	 * @param SPName
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getData(Context context, String SPName, String key,
			boolean defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SPName,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.getBoolean(key, defValue);
	}

	/**
	 * 存储int类型的值
	 * 
	 * @param SPName
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveData(Context context, String SPName, String key,
			Long value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SPName,
					Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putLong(key, value).commit();
	}

	/**
	 * 读取int类型的值
	 * 
	 * @param SPName
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static Long getData(Context context, String SPName, String key,
			Long defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SPName,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.getLong(key, defValue);
	}
	/**
	 * 存储int类型的值
	 * 
	 * @param SPName
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveData(Context context, String SPName, String key,
			int value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SPName,
					Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().putInt(key, value).commit();
	}
	
	/**
	 * 读取int类型的值
	 * 
	 * @param SPName
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getData(Context context, String SPName, String key,
			int defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SPName,
					Context.MODE_PRIVATE);
		}
		return sharedPreferences.getInt(key, defValue);
	}

	/**
	 * 清空指定sp文件中的数据
	 * 
	 * @param SPName
	 * @param context
	 */
	public static void clear(Context context, String SPName) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(SPName,
					Context.MODE_PRIVATE);
		}
		sharedPreferences.edit().clear().commit();
	}

	/**
	 * 保存用户登录的基本信息
	 * 
	 * @param context
	 * @param SPName
	 * @param userId
	 * @param gender
	 * @param likegender
	 * @param photoUrl
	 * @param signature
	 */
	public static void saveUserLoginInfo(Context context, String SPName,
			String userId, String phone, String password, String gender,
			String likegender, String photoUrl, String signature,
			String nickname) {
		saveData(context, SPName, "userId", userId);
		saveData(context, SPName, "phone", phone);
		saveData(context, SPName, "password", password);
		saveData(context, SPName, "gender", gender);
		saveData(context, SPName, "likegender", likegender);
		saveData(context, SPName, "photoUrl", photoUrl);
		saveData(context, SPName, "signature", signature);
		saveData(context, SPName, "nickname", nickname);
	}

	/**
	 * 保存老师登录的基本信息
	 * 
	 * @param context
	 * @param userType
	 *            类型
	 * @param userId
	 *            id
	 * @param userPhone
	 *            电话号
	 * @param userPassword
	 *            密码
	 * @param nickname
	 *            昵称
	 * @param signature
	 *            签名
	 * @param photo
	 *            照片url
	 * @param gender
	 *            性别
	 * @param personCardName
	 * @param personCardUrl
	 */
	public static void saveTchLogin(Context context, String userType,
			String userId, String userPhone, String userPassword,
			String nickname, String signature, String photo, String gender,
			String personCardName, String personCardUrl, String city) {

	}

	/**
	 * 保存用户登录的基本信息
	 * 
	 * @param context
	 * @param userType
	 * @param userId
	 * @param userPhone
	 * @param userPassword
	 * @param grade
	 * @param nickname
	 * @param signature
	 * @param photo
	 * @param gender
	 */
	public static void saveLogin(Context context, String userType,
			String userId, String userPhone, String userPassword,
			String nickname, String signature, String photo, String gender,
			String city) {
	}
}
