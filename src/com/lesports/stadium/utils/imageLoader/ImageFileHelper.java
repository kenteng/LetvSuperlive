package com.lesports.stadium.utils.imageLoader;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lesports.stadium.utils.LogUtil;

import android.text.TextUtils;
/**
 * 
 * @ClassName:  ImageFileHelper   
 * @Description:加载图片的帮助类   
 * @author: 王新年 
 * @date:   2015-12-28 下午5:40:56   
 *
 */
public class ImageFileHelper {
	/*
	 * log 标识
	 */
	private static final String TAG = "ImageFileHelper";
	/**
	 * 类对象
	 */
	private static ImageFileHelper fileHelper;
	/**
	 * 私有化
	 */
	private ImageFileHelper() {
		super();
	}
	/**
	 * 
	 * @Title: getInstance   
	 * @Description:获取当前类对象  
	 * @param: @return      
	 * @return: ImageFileHelper      
	 * @throws
	 */
	public static ImageFileHelper getInstance() {
		if (fileHelper == null)
			fileHelper = new ImageFileHelper();
		return fileHelper;
	}
	
	/**
	 * 删除文件
	 */
	public static void deleteFile(String sFileName) {
		final File file = new File(sFileName);
		if (file.exists()) {
			boolean is = file.delete();
			LogUtil.i(TAG,"是否删除配置文件" + is);
		}
	}

	/**
	 * 
	 * @Title: ReadStringFromFile   
	 * @Description:读取文件
	 * @param: @param sFileName
	 * @param: @return      
	 * @return: String      
	 * @throws
	 */
	public static String ReadStringFromFile(String sFileName) {
		if (TextUtils.isEmpty(sFileName))
			return null;
		String sDest = null;
		File f = new File(sFileName);
		if (!f.exists()){
			return null;
		}
		try {
			FileInputStream is = new FileInputStream(f);
			ByteArrayOutputStream bais = new ByteArrayOutputStream();
			try {
				byte[] buffer = new byte[1];// [512];
				while (is.read(buffer) != -1) {
					bais.write(buffer);
				}
				sDest = bais.toString().trim();
			} catch (IOException ioex) {
				LogUtil.i(TAG,"Excetion : ioexception  at read string from file! ");
			} finally {
				is.close();
				bais.close();
			}
		} catch (Exception ex) {
			LogUtil.i(TAG,"Exception : read string from file!" + ex.getMessage());
		}
		return sDest;
	}

	/**
	 * 保存文件
	 * 
	 * @param sToSave
	 * @param sFileName
	 * @param isAppend
	 * @return
	 */
	public static boolean WriteStringToFile(String content, String fileName, boolean isAppend) {
		boolean bFlag = false;
		if(content==null){
			return false;
		}
		final int iLen = content.length();
		final File file = new File(fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			final FileOutputStream fos = new FileOutputStream(file, isAppend);
			byte[] buffer = new byte[iLen];
			try {
				buffer = content.getBytes();
				fos.write(buffer);
				fos.flush();
				bFlag = true;
			} catch (IOException ioex) {
				LogUtil.i(TAG,"Excetion : ioexception  at write string to file! ");
			} finally {
				fos.close();
			}
		} catch (Exception ex) {
			LogUtil.i(TAG,"Exception : write string to file");
		}
		return bFlag;
	}
}
