package com.lesports.stadium.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class LetvUtil extends Activity {

	String mmsid;

	String isHd;

	String pcode;

	String version;

	static Context mContext;
	static File mFile;
	@SuppressWarnings("unused")
	private static final String TAG = "LetvUtil";

	private static String deviceID;

	public static final int NETTYPE_NO = 0;
	public static final int NETTYPE_WIFI = 1;
	public static final int NETTYPE_2G = 2;
	public static final int NETTYPE_3G = 3;

	@SuppressWarnings("static-access")
	public LetvUtil(String mmsid, String isHd, String pcode, String version,
			Context mContext) {
		this.mmsid = mmsid;
		this.isHd = isHd;
		this.pcode = pcode;
		this.version = version;
		this.mContext = mContext;
	}

	@SuppressWarnings("static-access")
	public LetvUtil(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * 获取代理的host port
	 * 
	 * @return
	 */
	// public static String[] getProxyInfo(Context context) {
	// String[] mProxy = null;
	// // if(context!=null){
	// ConnectivityManager cm = (ConnectivityManager) context
	// .getSystemService(Context.CONNECTIVITY_SERVICE);
	// NetworkInfo info = cm.getActiveNetworkInfo();
	// if (info != null && info.isAvailable()
	// && info.getType() == ConnectivityManager.TYPE_MOBILE) {
	// String proxyHost = android.net.Proxy.getDefaultHost();
	// int port = android.net.Proxy.getDefaultPort();
	// mProxy = new String[] { proxyHost, port + "" };
	// }
	// // }
	// return mProxy;
	// }

	/**
	 * @date 2013-12-25 UrlConnction检查是否是代理
	 */
	// public static Proxy detectUrlProxy(Context mContext) {
	// Proxy urlProxy = null;
	// String[] proxyInfo = getProxyInfo(mContext);
	// if (proxyInfo != null) {
	// InetSocketAddress sa = new InetSocketAddress(
	// String.valueOf(proxyInfo[0]), Integer.valueOf(proxyInfo[1]));
	// urlProxy = new Proxy(Proxy.Type.HTTP, sa);
	// }
	// proxyInfo = null;
	// return urlProxy;
	// }

	/**
	 * httpclient 代理设置
	 * 
	 * @description
	 * @date 2013-12-25
	 * @param context
	 * @return
	 */
	// public static HttpHost getPostProxy(Context context) {
	// HttpHost hostProxy = null;
	// // String[] proxy = getProxyInfo(context);
	// String[] proxyHostAndPort = ApnChecker.getProxyHostAndPort(context);
	// if (proxyHostAndPort != null) {
	// hostProxy = new HttpHost(String.valueOf(proxyHostAndPort[0]),
	// Integer.valueOf(proxyHostAndPort[1]));
	// }
	// return hostProxy;
	// }

	public String getLocation() {
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		double mLongitude = 0.0;
		double mLatitude = 0.0;
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Location location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null) {
				mLatitude = location.getLatitude();
				mLongitude = location.getLongitude();
			}
		} else {
			LocationListener locationListener = new LocationListener() {

				@Override
				public void onLocationChanged(Location location) {
					if (location != null) {

					}

				}

				@Override
				public void onProviderDisabled(String provider) {

				}

				@Override
				public void onProviderEnabled(String provider) {

				}

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {

				}
			};
			locationManager
					.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
							1000, 0, locationListener);
			Location location2 = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (location2 != null) {
				mLatitude = location2.getLatitude(); // 经度
				mLongitude = location2.getLongitude(); // 纬度
			}
		}
		String lacation = mLatitude + "," + mLongitude;
		return lacation;

	}

	/**
	 * 检查sdcard状态
	 * 
	 * @return
	 */
	public static boolean isSdcardAvailable() {
		boolean exist = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
		return exist;
	}

	/**
	 * 删除文件(包括下载完成、未下载完成)
	 * 
	 * @param file
	 */
	public static void deleteFile(final File file) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (file != null && file.exists()) {
					file.delete();
				}
			}
		}).start();
	}

	/**
	 * 检查sdcard剩余空间
	 */

	/**
	 * 返回当前下载路径
	 * 
	 * @return
	 */

	/**
	 * 是否满足下载条件
	 * 
	 * @param context
	 * @return
	 */
	// public static boolean isDownloadConditionOk(Context context) {
	// NetworkInfo networkInfo = getAvailableNetWorkInfo(context);
	// if (networkInfo == null) {
	// Toast.makeText(context, "无网络", Toast.LENGTH_SHORT).show();
	// return false;
	// }
	// // if (!isSdcardAvailable()) {
	// // Toast.makeText(context, "sd卡不存在", Toast.LENGTH_SHORT).show();
	// // return false;
	// // }
	// long fileSize = AppUpgradeConstants.DEFAULT_SDCARD_SIZE;
	// if (fileSize >= 0) {
	// if (getSdcardAvailableSpace(context) >= fileSize) {
	// return true;
	// } else {
	// // Toast.makeText(
	// // context,
	// // ResourceUtil.getStringId(context,
	// // "upgrade_toast_sdcard_lower"),
	// // Toast.LENGTH_LONG).show();
	// return false;
	// }
	// }
	// return false;
	// }

	/**
	 * 网络状态
	 * 
	 * @return
	 */
	public static NetworkInfo getAvailableNetWorkInfo(Context context) {
		NetworkInfo activeNetInfo = null;
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			activeNetInfo = connectivityManager.getActiveNetworkInfo();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		if (activeNetInfo != null && activeNetInfo.isAvailable()) {
			return activeNetInfo;
		} else {
			return null;
		}
	}

	/**
	 * 是否wifi
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifi(Context context) {

		NetworkInfo networkInfo = getAvailableNetWorkInfo(context);

		if (networkInfo != null) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断网络情况
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean isConnect(Context mContext) {
		boolean isConnected = false;
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		isConnected = (ni != null && ni.isConnected());

		return isConnected;
	}

	private static Executor executor;

	public static Executor getExecutor() {
		synchronized (LetvUtil.class) {
			if (executor == null) {
				executor = Executors.newSingleThreadExecutor();
			}
			return executor;
		}
	}

	public static String getDataEmpty(String data) {
		if (data == null || data.length() <= 0) {
			return "";
		} else {
			return data.replace(" ", "_");
		}
	}

	/**
	 * 得到操作系统版本号
	 */
	public static String getOSVersionName() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 判断是否为URL
	 */
	public static boolean checkURL(String url) {
		String check ="^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";
		Pattern mPattern = Pattern.compile(check);
		Matcher matcher = mPattern.matcher(url);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 返回联网类型
	 * 
	 * @param context
	 * @return wifi或3G
	 */
	public static String getNetType(Context context) {
		String netType = null;
		ConnectivityManager connectivityMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityMgr != null) {
			NetworkInfo networkInfo = connectivityMgr.getActiveNetworkInfo();
			if (networkInfo != null) {
				if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
					netType = "wifi";
				} else if (ConnectivityManager.TYPE_MOBILE == networkInfo
						.getType()) {
					netType = "3G";
				} else {
					netType = "wifi";
				}
			}
		}

		return netType;
	}

	public static int getNetWorkType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isAvailable()) {
			if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
				return NETTYPE_WIFI;
			} else {
				TelephonyManager telephonyManager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);

				switch (telephonyManager.getNetworkType()) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return NETTYPE_2G;
				default:
					return NETTYPE_3G;
				}
			}
		} else {
			return NETTYPE_NO;
		}
	}

	private static String generate_DeviceId(Context context) {
		String str = getIMEI(context) + getDeviceName() + getBrandName()
				+ getMacAddress(context);
		return MD5Helper(str);
	}

	/**
	 * 生成设备ID
	 * 
	 * @param context
	 * @return
	 */
	public static String generateDeviceId(Context context) {
		String str = getIMEI(context) + getIMSI(context) + getDeviceName()
				+ getBrandName() + getMacAddress(context);
		return MD5Helper(str);
	}

	/**
	 * 获取IMEI
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		if (context == null) {
			return "";
		}
		try {
			String deviceId = ((TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
			if (null == deviceId || deviceId.length() <= 0) {
				return "";
			} else {
				return deviceId.replace(" ", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取IMSI
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMSI(Context context) {
		if (context == null) {
			return "";
		}
		String subscriberId = null;

		try {
			subscriberId = ((TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE))
					.getSubscriberId();
			if (null == subscriberId || subscriberId.length() <= 0) {
				subscriberId = generate_DeviceId(context);
			} else {
				subscriberId.replace(" ", "");
				if (TextUtils.isEmpty(subscriberId)) {
					subscriberId = generate_DeviceId(context);
				}
			}
			return subscriberId;
		} catch (Exception e) {
			e.printStackTrace();
			return subscriberId;
		}
	}

	/**
	 * 获取mac地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context) {
		if (context == null) {
			return "";
		}
		try {
			String macAddress = null;
			WifiInfo wifiInfo = ((WifiManager) context
					.getSystemService(Context.WIFI_SERVICE))
					.getConnectionInfo();
			macAddress = wifiInfo.getMacAddress();
			if (macAddress == null || macAddress.length() <= 0) {
				return "";
			} else {
				return macAddress;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String MD5Helper(String str) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
			byte[] byteArray = messageDigest.digest();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < byteArray.length; i++) {
				if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
					sb.append("0").append(
							Integer.toHexString(0xFF & byteArray[i]));
				} else {
					sb.append(Integer.toHexString(0xFF & byteArray[i]));
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		throw new RuntimeException("no device Id");
	}

	/**
	 * 得到设备名字
	 * */
	public static String getDeviceName() {
		String model = android.os.Build.MODEL;
		if (model == null || model.length() <= 0) {
			return "";
		} else {
			return model;
		}
	}

	/**
	 * 得到品牌名字
	 * */
	public static String getBrandName() {
		String brand = android.os.Build.BRAND;
		if (brand == null || brand.length() <= 0) {
			return "";
		} else {
			return brand;
		}
	}

	/**
	 * 得到客户端版本信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getClientVersionName(Context context) {
		if (context == null) {
			return "";
		}
		try {
			PackageInfo packInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			String versionName = packInfo.versionName;
			// String[] strings = versionName.trim().split("\\.");
			// if(strings.length<3){
			// versionName = versionName+".0";
			// }
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取设备分辨率
	 * 
	 * @param context
	 * @return
	 */
	public static String getResolution(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return new StringBuilder().append(dm.widthPixels).append("*")
				.append(dm.heightPixels).toString();
	}

	/**
	 * 获取设备唯一标示
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceID(Context context) {

		if (TextUtils.isEmpty(deviceID)) {
			deviceID = generateDeviceId(context);
		}

		return deviceID;
	}

	/**
	 * 检测该包名所对应的应用是否存在
	 * 
	 * @param packageName
	 * @return
	 */
	public static boolean checkPackageAndVersion(Context context,
			String packageName, String version) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			String versionName = getClientVersionName(context);
			if (version.trim().contains(versionName)) {
				return false;
			} else {
				return true;
			}
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * 检查某个程序是否已安装
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean checkAppIsInstalled(Context context,
			String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * 根据路径和名称获取文件名
	 * 
	 * @param dir
	 * @param name
	 * @return
	 */
	public static File getFile(String dir, String name) {
		File file = new File(dir + File.separator + name);
		return file;
	}

	/**
	 * 根据ListView的child设置其高度
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// params.height += 5;//if without this statement,the listview will be a
		// little short
		listView.setLayoutParams(params);
	}

	public static boolean checkHasInstallByFileName(Context context,
			String fileName) {
		boolean checkAppIsInstalled = false;
		if (fileName.contains("_")) {
			String substring = fileName.substring(0, fileName.indexOf("_"));
			checkAppIsInstalled = checkAppIsInstalled(context, substring);
			return checkAppIsInstalled;
		}
		return checkAppIsInstalled;
	}

	/**
	 * 修改文件权限
	 * 
	 * @param filePath
	 */
	public static void changeFileMode(String filePath) {
		String[] command = { "chmod", "777", filePath };
		ProcessBuilder builder = new ProcessBuilder(command);
		try {
			builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获得手机IP地址
	 * 
	 * @return
	 */
	public static String getLocalIpAddress(Context context) {
		final ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final android.net.NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		final android.net.NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (wifi.isAvailable()) {
			WifiManager wifimanage = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);// 获取WifiManager
			// 检查wifi是否开启
			if (!wifimanage.isWifiEnabled()) {
			}
			WifiInfo wifiinfo = wifimanage.getConnectionInfo();
			// 将获取的int转为真正的ip地址,参考的网上的，修改了下
			int i = wifiinfo.getIpAddress();
			return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
					+ ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
		} else if (mobile.isAvailable()) {
			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface
						.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf
							.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress()) {
							return inetAddress.getHostAddress().toString();
						}
					}
				}
			} catch (SocketException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	/**
	 * 时候需要判断App是否在前台运行; 如果app在前台返回true,否则返回false
	 */
	public static boolean isRunningForeground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		String currentPackageName = cn.getPackageName();
		String appPackageName = context.getPackageName();
		if (!TextUtils.isEmpty(currentPackageName)
				&& currentPackageName.equals(appPackageName)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取有线MAC地址
	 */
	public static String getEthMAC() {
		Reader reader = null;
		StringBuffer str = new StringBuffer();
		try {
			char[] tempchars = new char[20];
			int charread = 0;
			reader = new InputStreamReader(new FileInputStream(
					"/sys/class/net/eth0/address"));
			while ((charread = reader.read(tempchars)) != -1) {
				if ((charread == tempchars.length)
						&& (tempchars[tempchars.length - 1] != '\r')) {
					System.out.print(tempchars);
				} else {
					for (int i = 0; i < charread; i++) {
						if (tempchars[i] == '\r') {
							continue;
						} else {
							str.append(tempchars[i]);
						}
					}
				}
			}
		} catch (Exception e1) {
			// LogUtils.e(TAG, e1.getMessage());
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (Exception e1) {
				}
		}

		return str.toString().trim();
	}

	public static String getNetType2(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isAvailable()) {
			if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
				return "wifi";
			} else {
				TelephonyManager telephonyManager = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);

				switch (telephonyManager.getNetworkType()) {
				case TelephonyManager.NETWORK_TYPE_GPRS:
				case TelephonyManager.NETWORK_TYPE_CDMA:
				case TelephonyManager.NETWORK_TYPE_EDGE:
					return "2G";
				case TelephonyManager.NETWORK_TYPE_LTE:
					return "4G";
				default:
					return "3G";
				}
			}
		} else {
			return "no net";
		}
	}

	// private String getLocation() {
	// LocationManager locationManager = (LocationManager)
	// getSystemService(Context.LOCATION_SERVICE);
	// double mLongitude;
	// double mLatitude;
	// if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	// Location location = locationManager
	// .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	// if (location != null) {
	// mLatitude = location.getLatitude();
	// mLongitude = location.getLongitude();
	// }
	// } else {
	// LocationListener locationListener = new LocationListener() {
	//
	// @Override
	// public void onLocationChanged(Location location) {
	// if (location != null) {
	//
	// }
	//
	// }
	//
	// @Override
	// public void onProviderDisabled(String provider) {
	//
	// }
	//
	// @Override
	// public void onProviderEnabled(String provider) {
	//
	// }
	//
	// @Override
	// public void onStatusChanged(String provider, int status,
	// Bundle extras) {
	//
	// }
	// };
	// locationManager.requestLocationUpdates(
	// LocationManager.NETWORK_PROVIDER, 1000, 0,
	// locationListener);
	// Location location2 = locationManager
	// .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	// if (location2 != null) {
	// mLatitude = location2.getLatitude(); // 经度
	// mLongitude = location2.getLongitude(); // 纬度
	// }
	// }
	// String lacation = mLatitude + "," + mLongitude;
	// return lacation;
	// }

	public static String unixTimeToDate(long unixLong) {
		SimpleDateFormat fm1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = null;
		try {
			date = fm1.format(unixLong);
		} catch (Exception ex) {
			System.out.println("String转换Date错误，请确认数据可以转换！");
		}
		return date;
	}

}
