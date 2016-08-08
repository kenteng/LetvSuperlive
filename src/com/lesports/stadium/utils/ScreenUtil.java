package com.lesports.stadium.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;


public class ScreenUtil {

	public static int height;
	public static int width;
	@SuppressWarnings("unused")
	private Context context;

	private static ScreenUtil instance;

	private ScreenUtil(Context context) {
		this.context = context;
		WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
	}

	public static ScreenUtil getInstance(Context context) {
		if (instance == null) {
			instance = new ScreenUtil(context);
		}
		return instance;
	}





	/**
	 * 得到手机屏幕的宽�?, pix单位
	 */
	public int getScreenWidth() {
		return width;
	}
	
	/**
	 * 获取控件宽
	 */
	public  int getWidth(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h);
		return (view.getMeasuredWidth());
	}








}