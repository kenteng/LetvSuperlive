package com.lesports.stadium.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
/**
 * 
 * ***************************************************************
 * @ClassName:  CameraCropBorderView 
 * 
 * @Desc : 自定义界面 我要上颜值 中界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-2-14 下午2:39:16
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class CameraCropBorderView extends View {
	/**
	 * 绘制的矩形的宽度
	 */
	private int mWidth = 640;
	/**
	 * 绘制的矩形的高度
	 */
	private int mHeight = 640;
	/**
	 * 边框的颜色，默认为白色
	 */
	private int mBorderColor = Color.parseColor("#FFFFFF");
	/**
	 * 边框以外的颜色
	 */
	private int mFillColor = Color.parseColor("#d6000000");
	/**
	 * 边框的宽度 单位dp
	 */
	private int mBorderWidth = 1;
	/**
	 * border的画笔
	 */
	private Paint mPaint;
	/**
	 * fill的画笔
	 */
	private Paint mPaintFill;
	/**
	 * 绘制的矩形的范围
	 */
	private Rect rect = new Rect();
	/**
	 * 第一次绘制时取得rect的范围
	 */
	private boolean isFirst = true;

	public CameraCropBorderView(Context context) {
		this(context, null);
	}

	public CameraCropBorderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CameraCropBorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mBorderWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources().getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(mBorderColor);
		mPaint.setStrokeWidth(mBorderWidth);
		mPaint.setStyle(Style.STROKE);

		mPaintFill = new Paint();
		mPaintFill.setAntiAlias(true);
		mPaintFill.setColor(mFillColor);
		mPaintFill.setStyle(Style.FILL);
	}

	public Rect getRect() {
		return rect;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isFirst) {
			int srceenW = getWidth();
			int screenH = getHeight();
			mWidth = mHeight = srceenW > screenH ? screenH : srceenW;
			int left = (srceenW - mWidth) / 5*3;
			int top = (screenH - mHeight) / 5*3;
			int right = left + mWidth;
			int bottom = top + mHeight;
//			rect.set(0, 0, right, bottom);
			rect.set(left/3, top/3, right, bottom);
			isFirst = false;
		}
		// 绘制外边框
		canvas.drawRect(rect, mPaint);
		//绘制上边
		canvas.drawRect(0, 0, getWidth(), rect.top, mPaintFill);
		//绘制下边
		canvas.drawRect(0, rect.bottom, getWidth(), getHeight(), mPaintFill);
		//绘制左边
		canvas.drawRect(0, rect.top, rect.left, rect.bottom, mPaintFill);
		//绘制右边
		canvas.drawRect(rect.right, rect.top, getWidth(), rect.bottom, mPaintFill);
	}
}
