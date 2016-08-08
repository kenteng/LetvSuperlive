package com.lesports.stadium.cache;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.provider.CalendarContract.Calendars;
import android.util.Log;

public class FileUtil {

	/**
	 * @Description 获取缓存的根目录
	 * 
	 * @return String 返回缓存的目录.
	 */
	public static String getCacheRootDirPath(Context context) {
		String imageCacheRootPath = null;
		File file = null;
		// 检查有SD卡并且存储空间至少有3Mb的可用空间
		if (hasSDCard() && getAvailableExternalMemory() > 3) {
			String cacheDir = "/Android/data/" + context.getPackageName() + "/";
			imageCacheRootPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + cacheDir;
			file = new File(imageCacheRootPath);
		} else {
			// /data/data/包名/cache/
			imageCacheRootPath = "cache";
			file = new File(context.getCacheDir(), imageCacheRootPath);
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.toString();
	}

	/**
	 * @Description 判断是否有sd卡
	 * 
	 * @return boolean 如果有sd卡则返回true.
	 */
	public static boolean hasSDCard() {
		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	/**
	 * @Description 判断是否有sd卡是否有剩余空间
	 * 
	 * @return boolean 如果有sd卡的存储空间大于20MB则返回true.
	 */
	public static boolean isSDHasFree() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		long freeSize = (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
		if (freeSize >= 20) {
			return true;
		}
		return false;
	}

	/**
	 * 得到手机SDcard外部可用内存(剩余空间)
	 * 
	 */
	public static double getAvailableExternalMemory() {
		double availableExternalMemorySize = 0;

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File externalPath = Environment.getExternalStorageDirectory();
			StatFs externaStat = new StatFs(externalPath.getPath());
			// 参数
			long externaBlockSize = externaStat.getBlockSize();
			// 外部可用内存
			long externaAvailableBlocks = externaStat.getAvailableBlocks();

			availableExternalMemorySize = (externaBlockSize * externaAvailableBlocks)
					/ (1024 * 1024);
			return availableExternalMemorySize;
		}
		return availableExternalMemorySize;

	}

	/*
	 * 根据文件目录创建目录 return 创建成功返回true
	 */
	public static boolean createDirectory(String directoryPram) {
		String cacheDirectory = directoryPram;
		File file = new File(cacheDirectory);
		if (!file.exists()) {
			return file.mkdirs();
		}
		return true;
	}

	/**
	 * 根据文件目录清空缓存文件
	 * 
	 * @param cacheDirectory
	 */
	public static synchronized void clear(File cacheDirectory) {
		if (cacheDirectory == null)
			return;
		// 获取所有文件
		File[] files = cacheDirectory.listFiles();
		if (files == null)
			return;
		Long timer = Calendar.getInstance().getTimeInMillis()
				- cacheDirectory.lastModified();
		if (timer > 72000000) {
			boolean isdis = deleteDir(cacheDirectory);
		}
	}

	/**
	 * 返回值是 0 今天，< 0 昨天
	 */
	private static int formatDateTime(Long time) {

		Date date = null;
		try {
			date = new Date(time);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Calendar current = Calendar.getInstance();

		Calendar today = Calendar.getInstance(); // 今天

		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
		// Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		Calendar yesterday = Calendar.getInstance(); // 昨天

		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH,
				current.get(Calendar.DAY_OF_MONTH) - 1);
		yesterday.set(Calendar.HOUR_OF_DAY, 0);
		yesterday.set(Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);

		current.setTime(date);

		if (current.after(today)) {
			return 0;
		} else if (current.before(today) && current.after(yesterday)) {

			return -1;
		} else {

			return -1;
		}
	}

	/**
	 * 根据文件的最后修改时间进行升序排序
	 */
	private static class FileLastModifSort implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			long d1 = arg0.lastModified();
			long d2 = arg1.lastModified();
			if (d1 == d2) {
				return 0;
			} else {
				return d1 > d2 ? 1 : -1;
			}
		}
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			if(children==null)
				return true;
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
//		return true;
	}
}
