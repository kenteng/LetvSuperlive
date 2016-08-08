package com.lesports.stadium.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.lesports.stadium.utils.imageLoader.FlushedInputStream;
import com.lesports.stadium.utils.imageLoader.ImageFileCache;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.Config;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 功能 ：1.展示图片（缩放压缩，质量压缩），2.保存图片（质量压缩），3.质量压缩 4. 读取图片的旋转角度  5.设置图片的旋转角度
 * @author wxn
 *
 */
public class ImageUtils {
	///1.质量压缩 2. 读取图片的旋转角度  3.设置图片的旋转角度
	
	
	/**
	 * 将制定路径的图片保存到本地
	 * @param imgPath 原图片路径
	 * @param quality 将图片进行质量压缩
	 * @param outPath 保存图片的路径
	 * @return
	 */
	public static String compressAndGenImage(String imgPath,int quality,String outPath){
		Bitmap bitmap = GenerateBitmap(imgPath,0,0,quality);
		return compressAndGenImage(bitmap, outPath, bitmap.getWidth(), bitmap.getHeight());
	}
	
	/**
	 * 将图片Bitmap保存到本地
	 * @param image
	 * @param outPath
	 * @return
	 */
	public static String compressAndGenImage(Bitmap image, String outPath){
		return compressAndGenImage(image,outPath,image.getWidth(),image.getHeight());
	}
	
	/**
	 * 将图片Bitmap保存到本地
	 * @param image
	 * @param outPath 保存的路径
	 * @param width 期望保存的宽度
	 * @param height 期望保存到本地的高度
	 * @return
	 */
	public static String compressAndGenImage(Bitmap image, String outPath,
			float width, float height) {
		if(image==null)
			return null;
		try {
			int bitmapWidth = image.getWidth();
			int bitmapHeight = image.getHeight();
			// 缩放图片的尺寸
			float scaleWidth = width / bitmapWidth;
			float scaleHeight = height / bitmapHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 产生缩放后的Bitmap对象
			Bitmap resizeBitmap = Bitmap.createBitmap(image, 0, 0, bitmapWidth,
					bitmapHeight, matrix, false);
			// save file
			FileOutputStream fos = new FileOutputStream(outPath);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			if (resizeBitmap.compress(Bitmap.CompressFormat.JPEG, 50, os)) {
				fos.write(os.toByteArray());
			}
			if (!image.isRecycled()) {
				image.recycle();// 记得释放资源，否则会内存溢出
			}
			if (!resizeBitmap.isRecycled()) {
				resizeBitmap.recycle();
			}
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return outPath;
	}
	
	/**
	 * 根据图片路径展示图片
	 * @param imgPath 图片路径
	 * @param pixelW 展示的宽度
	 * @param pixelH 展示的高度
	 * @param quality 要显示的图片最大为多少  如设置为 100  最大为100K
	 * @return
	 */
	public static Bitmap GenerateBitmap(String imgPath, float pixelW, float pixelH,int quality){
		Log.i("123", "imgPath :"+imgPath);
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
		newOpts.inJustDecodeBounds = true;
		newOpts.inPreferredConfig = Config.RGB_565;
		// Get bitmap info, but notice that bitmap is null now
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		int degree = getBitmapDegree(imgPath);
		Log.i("wxn", "旋转角度："+degree);
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		if (degree == 90 || degree == 180 || degree == 270) {
			w = newOpts.outHeight;
			h = newOpts.outWidth;
		}
		// 想要缩放的目标尺寸
		int be = 1;// be=1表示不缩放
		if(pixelH!=0&&pixelW!=0){
			float hh = pixelH;
			float ww = pixelW;
			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
				be = (int) (w / ww);
			} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
				be = (int) (h / hh);
			}
			if (be <= 0){
				be = 1;
			}
		}
		newOpts.inSampleSize = be;// 设置缩放比例
//		bitmap = BitmapFactory.decodeFile(imgPath, newOpts); 有的手机上会返回null
		InputStream is;
		try {
			is = new FileInputStream(new File(imgPath));
			bitmap = BitmapFactory.decodeStream(is, null, newOpts);
			is.close();
			if (degree == 90 || degree == 180 || degree == 270) {
				bitmap = rotateBitmapByDegree(bitmap, degree);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return compressImage(bitmap,quality);
	}
	
	/**
	 * 将Bitmap图片进行一定的宽高显示以及质量压缩
	 * @param image
	 * @param width
	 * @param height
	 * @param quality 要压缩的质量
	 * @return
	 */
	public static Bitmap GenerateBitmap(Bitmap image, float width, float height,
			int quality) {
		Bitmap resizeBitmap = null;
		try {
			int bitmapWidth = image.getWidth();
			int bitmapHeight = image.getHeight();
			// 缩放图片的尺寸
			float scaleWidth = width / bitmapWidth;
			float scaleHeight = height / bitmapHeight;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// 产生缩放后的Bitmap对象
			resizeBitmap = Bitmap.createBitmap(image, 0, 0, bitmapWidth,
					bitmapHeight, matrix, false);
			if (!image.isRecycled()) {
				image.recycle();// 记得释放资源，否则会内存溢出
			}
			resizeBitmap = compressImage(resizeBitmap,quality);

		} catch (OutOfMemoryError oom) {
			resizeBitmap= image;
		}

		return resizeBitmap;

	}
	
	
	/**
	 * 质量压缩
	 * @param image
	 * @param quality
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image,int quality) {
		if(image==null){
			Log.i("123", "传入的image为null 不能压缩");
			return null;
		}
		if(quality==0)
			return image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > quality) { // compress = 100 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			if(options<0)
				options =0;
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			if(options==0)
				break;
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
	/**
	 * 读取图片的旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}
	
	
	public static Bitmap getBitMap(ImageFileCache fileCache,String url,boolean isMatch){
		File file = fileCache.getFile(url);

		Bitmap bitmap = null;
		if (file.exists()) {
			bitmap = decodeFile(file,isMatch);
		}else{
			bitmap = fromWebLoadNormalImage(url, file,isMatch);
		}
		return bitmap;
	}
	
	/**
	 * 从网络下载一般图片
	 * 
	 * @param url
	 * @param widthHightPixels
	 * @param f
	 * @return
	 */

	public static Bitmap fromWebLoadNormalImage(String url,
			File file,boolean isMatch) {
		InputStream is = null;
		OutputStream os = null;
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			is = conn.getInputStream();
			if (!file.exists()) {
				file.createNewFile();
			}
			os = new FileOutputStream(file);
			CopyStream(is, os);
			bitmap = decodeFile(file,isMatch);
			return bitmap;
		} catch (Exception ex) {
			if (file.exists()) {
				file.delete();
			}
			// ex.printStackTrace();
			return null;
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static Bitmap decodeFile(File f,boolean isMatch) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FlushedInputStream(
					new FileInputStream(f)), null, o);
			int width_tmp = o.outWidth, height_tmp = o.outHeight;

			// Find the correct scale value. It should be the power of 2.
			int scale = 4;
			if(isMatch)
				scale = 1;
			final int REQUIRED_SIZE = GlobalParams.WIN_WIDTH/8;
			boolean tag = true;
			while (tag&&!isMatch) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE){
					tag = false;
					break;
				}
				width_tmp /= 2;
				height_tmp /= 2;
				if(!isMatch)
					scale += 4;
					
			}
			// scale = computeSampleSize(o,-1,width_tmp * height_tmp);
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FlushedInputStream(
					new FileInputStream(f)), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
