package com.lesports.stadium.utils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lesports.stadium.cache.CacheManager;
import com.lesports.stadium.cache.CacheManagerConst.CacheType;


import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

/**
 * 日志记录
 * 
 */
public class LogUtil {
	/**
	 * 开发阶段
	 */
	private static final int DEVELOP = 0;
	/**
	 * 内部测试阶段
	 */
	private static final int DEBUG = 1;
	/**
	 * 公开测试
	 */
	private static final int BATE = 2;
	/**
	 * 正式版
	 */
	private static final int RELEASE = 3;

	/**
	 * 当前阶段标示
	 */
	private static int currentStage = DEVELOP;

	private static String path;
	private static File file;
	private static FileOutputStream outputStream;
	// 用于格式化日期,作为日志文件名的一部分
	@SuppressLint("SimpleDateFormat")
	private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	static {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			@SuppressWarnings("unused")
			File externalStorageDirectory = Environment.getExternalStorageDirectory();
			path =  CacheManager.getInstance().getCachePath(CacheType.CACHE_LOG);
			File directory = new File(path);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = time + "-" + timestamp + ".log";
			file = new File(new File(path), fileName);
			android.util.Log.i("SDCAEDTAG", path);
			try {
				outputStream = new FileOutputStream(file, true);
			} catch (FileNotFoundException e) {

			}
		} else {

		}
	}

	public static void i(String msg) {
		i("LogUtil", msg);
	}

	public static void i(String tag, String msg) {
		switch (currentStage) {
			case DEVELOP:
				// 控制台输出
				Log.i(tag, msg);
				break;
			case DEBUG:
				// 在应用下面创建目录存放日志
				
				break;
			case BATE:
				// 写日志到sdcard
				String time = formatter.format(new Date());
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					if (outputStream != null) {
						try {
							outputStream.write(time.getBytes());
							outputStream.write(("    " + tag + "\r\n").getBytes());
							outputStream.write(msg.getBytes());
							outputStream.write("\r\n".getBytes());
							outputStream.flush();
						} catch (IOException e) {

						}
					} else {
						android.util.Log.i("SDCAEDTAG", "file is null");
					}
				}
				break;
			case RELEASE:
				// 一般不做日志记录
				break;
		}
	}
}
