package com.lesports.stadium.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.net.Code.NetworkStatus;

/**
 * 
 * @ClassName: CommonUtils
 * @Description:公共方法
 * @author: 王新年
 * @date: 2015-12-28 下午5:44:11
 * 
 */
public class CommonUtils {

	/**
	 * 验证手机号
	 * 
	 * @param strPhone
	 * @return
	 */
	public static boolean isRegexPhone(String strPhone) {
		if (strPhone == null) {
			return false;
		}
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(14[5,7])|(17[0,6,7,8]))\\d{8}$");
		Matcher m = p.matcher(strPhone);
		System.out.println(m.matches() + "---");
		return m.matches();
	}

	/**
	 * 判断email格式是否正确
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 获取当前的网络状态 ：没有网络0：WIFI网络1：3G网络2：2G网络3
	 * 
	 * @param context
	 * @return
	 */
	public static int getAPNType(Context context) {
		int netType = 0;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = 1;// wifi
		} else if (nType == ConnectivityManager.TYPE_MOBILE) {
			int nSubType = networkInfo.getSubtype();
			TelephonyManager mTelephony = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
					&& !mTelephony.isNetworkRoaming()) {
				netType = 2;// 3G
			} else {
				netType = 3;// 2G
			}
		}
		return netType;
	}

	/**
	 * @Description 根据传入的字符串，生成md5值
	 * 
	 * @return String 返回生成的md5值
	 */
	public static String md5(String s) {
		String result = null;
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++) {
				String h = Integer.toHexString(0xFF & messageDigest[i]);
				while (h.length() < 2)
					h = "0" + h;
				hexString.append(h);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
	
    /**
    *
    * @param plainText
    *            明文
    * @return 32位密文
    */
   public static String encryption(String plainText) {
	   String result = null;
		// 首先判断是否为空
		if (TextUtils.isEmpty(plainText)) {
			return null;
		}
		try {
			// 首先进行实例化和初始化
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 得到一个操作系统默认的字节编码格式的字节数组
			byte[] btInput = null;
			try {
				btInput = plainText.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// 对得到的字节数组进行处理
			md.update(btInput);
			// 进行哈希计算并返回结果
			byte[] btResult = md.digest();
			// 进行哈希计算后得到的数据的长度
			StringBuffer sb = new StringBuffer();
			for (byte b : btResult) {
				int bt = b & 0xff;
				if (bt < 16) {
					sb.append(0);
				}
				sb.append(Integer.toHexString(bt));
			}
			result = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	return result;
   }


	/**
	 * 获取当前的网络状态 ：没有网络0：WIFI网络1：3G网络2：2G网络3
	 * 
	 * @param context
	 * @return
	 */
	public static int getAPNType() {
		int netType = NetworkStatus.NO_NET;
		ConnectivityManager connMgr = (ConnectivityManager) LApplication.instance
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NetworkStatus.WIFI;// wifi
		} else if (nType == ConnectivityManager.TYPE_MOBILE) {
			int nSubType = networkInfo.getSubtype();
			TelephonyManager mTelephony = (TelephonyManager) LApplication
					.getContext().getSystemService(Context.TELEPHONY_SERVICE);
			if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
					&& !mTelephony.isNetworkRoaming()) {
				netType = NetworkStatus.TWO_G;// 3G
			} else {
				netType = NetworkStatus.THREE_G;// 2G
			}
		}
		return netType;
	}

	/**
	 * 处理字符串中的表情
	 * 
	 * @param context
	 * @param message
	 *            传入的需要处理的String
	 * @param small
	 *            是否需要小图片
	 * @return
	 */
	private static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

	public static CharSequence convertNormalStringToSpannableString(
			Context context, String message, boolean small) {
		if (TextUtils.isEmpty(message)) {
			return "";
		}
		if (context == null)
			return "";
		String hackTxt;
		if (message.startsWith("[") && message.endsWith("]")) {
			hackTxt = message + " ";
		} else {
			hackTxt = message;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);

		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			int k = localMatcher.start();
			int m = localMatcher.end();
			if (m - k < 8) {
				if (FaceUtils.getFaceMap().containsKey(str2)) {
					int face = FaceUtils.getFaceMap().get(str2);
					Bitmap bitmap = BitmapFactory.decodeResource(
							context.getResources(), face);
					if (bitmap != null) {
						if (small) {
							int rawHeigh = bitmap.getHeight();
							int rawWidth = bitmap.getHeight();
							int newHeight = DensityUtil.dip2px(context, 24);
							int newWidth = DensityUtil.dip2px(context, 24);
							// 计算缩放因子
							float heightScale = ((float) newHeight) / rawHeigh;
							float widthScale = ((float) newWidth) / rawWidth;
							// 新建立矩阵
							Matrix matrix = new Matrix();
							matrix.postScale(heightScale, widthScale);
							// 设置图片的旋转角度
							// matrix.postRotate(-30);
							// 设置图片的倾斜
							// matrix.postSkew(0.1f, 0.1f);
							// 将图片大小压缩
							// 压缩后图片的宽和高以及kB大小均会变化
							bitmap = Bitmap.createBitmap(bitmap, 0, 0,
									rawWidth, rawHeigh, matrix, true);
						}
						ImageSpan localImageSpan = new ImageSpan(context,
								bitmap, ImageSpan.ALIGN_BASELINE);
						value.setSpan(localImageSpan, k, m,
								Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		return value;
	}
	
	
	/**
	 * 查询手机内所有支持分享的应用
	 * 
	 * @param context
	 * @return
	 */
	public static List<ResolveInfo> getShareApps(Context context) {
		List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
		Intent intent = new Intent(Intent.ACTION_SEND, null);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		PackageManager pManager = context.getPackageManager();
		mApps = pManager.queryIntentActivities(intent,
				PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		return mApps;
	}
	
	/**
	 * 将一个传入的字符串毫秒值转换成一个字符串时间值
	 * 
	 * @param str
	 * @return
	 */
	public static String getChangeToTime(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		long time = Long.parseLong(str);
		Date date = new Date();
		date.setTime(time);
		String timestring = sdf.format(date);

		return timestring;

	}
}
