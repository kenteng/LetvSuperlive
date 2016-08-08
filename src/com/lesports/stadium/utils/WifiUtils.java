package com.lesports.stadium.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiUtils {
	// 定义WifiManager对象
	private WifiManager mWifiManager;
	// 定义WifiInfo对象
	private WifiInfo mWifiInfo;

	public WifiUtils(Context context) {
		// 取得WifiManager对象
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 取得WifiInfo对象
		mWifiInfo = mWifiManager.getConnectionInfo();
	}

	// 得到MAC地址
	public String getMacAddress() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
	}

	// 得到接入点的BSSID
	public String getBSSID() {
		return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
	}

	// 得到IP地址
	public int getIPAddress() {
		return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
	}

	// 得到IP地址
	public String getIpAddress() {
		return intToIp(getIPAddress());
	}

	private String intToIp(int paramInt) {
		return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "."
				+ (0xFF & paramInt >> 16) + "." + (0xFF & paramInt >> 24);
	}

}
