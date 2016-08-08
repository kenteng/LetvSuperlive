package com.lesports.stadium.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/***
 * 自定义进度条
 * 
 */
public class SpringProgressView extends View {

	/** 分段颜色 */
	private static int[] SECTION_COLORS = { Color.GREEN, Color.RED };
	/** 进度条最大值 */
	private float maxCount;
	/** 进度条当前值 */
	private float currentCount;
	/** 画笔 */
	private Paint mPaint;
	private int mWidth, mHeight;
	/**
	 * 进度分段划分
	 */
	float[] flosts = new float[] { 0f, 1f };

	public SpringProgressView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public SpringProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SpringProgressView(Context context) {
		super(context);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		int round = mHeight / 2;
		RectF rectProgressBg = new RectF(0, 0, mWidth, mHeight);
		int[] colors = new int[2];
		System.arraycopy(SECTION_COLORS, 0, colors, 0, 2);
		LinearGradient shader = null;
		shader = new LinearGradient(0, 0, mWidth, mHeight, colors, flosts,
				Shader.TileMode.MIRROR);
		mPaint.setShader(shader);
		canvas.drawRoundRect(rectProgressBg, round, round, mPaint);
	}

	private int dipToPx(int dip) {
		float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	/***
	 * 设置最大的进度值
	 * 
	 * @param maxCount
	 */
	public void setMaxCount(float maxCount) {
		this.maxCount = maxCount;
	}

	/***
	 * 设置当前的进度值
	 * 
	 * @param currentCount
	 */
	public void setCurrentCount(int currentCount) {
		if (maxCount == 0) {
			flosts[0] = 0;
			flosts[1] = 0.5f;
		} else {
			this.currentCount = currentCount > maxCount ? maxCount
					: currentCount;
			float end = currentCount / maxCount;
			flosts[0] = 0;
			if (end == 0) {
				flosts[1] = 0;
			} else if (end == 1) {
				flosts[0] = 1;
				flosts[1] = 1;
			} else {
				if (end > 0.5) {
					flosts[0] = end;
					flosts[1] = 1;
				} else {
					flosts[0] = 0;
					flosts[1] = end;
				}

			}
		}
		invalidate();
	}

	public float getMaxCount() {
		return maxCount;
	}

	public float getCurrentCount() {
		return currentCount / maxCount;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthSpecMode == MeasureSpec.EXACTLY
				|| widthSpecMode == MeasureSpec.AT_MOST) {
			mWidth = widthSpecSize;
		} else {
			mWidth = 0;
		}
		if (heightSpecMode == MeasureSpec.AT_MOST
				|| heightSpecMode == MeasureSpec.UNSPECIFIED) {
			mHeight = dipToPx(5);
		} else {
			mHeight = heightSpecSize;
		}
		setMeasuredDimension(mWidth, mHeight);
	}

	public void setColors(int[] colors) {
		if (colors == null || colors.length < 2)
			return;
		this.SECTION_COLORS = colors;
	}

}
