package com.lesports.stadium.ui;


import com.lesports.stadium.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * 绘制A-Z的索引条目
 * @author poplar
 *
 */
public class IndexSideBar extends View {
	
	private static final String[] DEFAULT_LETTERS = new String[] { "#", "A", "B", "C",
			"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
			"Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
	private String[] letters;
	private Paint paint;

	private int cellWidth;

	private int mHeight;

	private float cellHeight;
	
	/**
	 * 字母更新监听
	 * @author poplar
	 *
	 */
	public interface OnLetterChangeListener{
		void onLetterChange(String letter);
	}
	private OnLetterChangeListener onLetterChangeListener;
	

	public OnLetterChangeListener getOnLetterChangeListener() {
		return onLetterChangeListener;
	}

	public void setOnLetterChangeListener(
			OnLetterChangeListener onLetterChangeListener) {
		this.onLetterChangeListener = onLetterChangeListener;
	}

	public IndexSideBar(Context context) {
		this(context, null);
	}

	public IndexSideBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public IndexSideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(getResources().getColor(R.color.black));
		paint.setTextSize(12f * getResources().getDisplayMetrics().density);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		this.letters = DEFAULT_LETTERS;
	}
	public void setLetters(String[] letters){
		this.letters = letters;
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		
		// 计算坐标, 将A-Z字母绘制到界面上
		for (int i = 0; i < letters.length; i++) {
			String text = letters[i];
			
			float x = (int) (cellWidth * 0.5f - paint.measureText(text) * 0.5f);
			
			Rect bounds = new Rect();
			// 把矩形对象赋值
			paint.getTextBounds(text, 0, text.length(), bounds);
			int textHeight = bounds.height();
			// 宽间距
			//float y = (int) (cellHeight * 0.5f + textHeight * 0.5f + cellHeight * i);
			float y = (int) (cellHeight * 0.5f + textHeight * 0.5f + cellHeight * i);
			
			// 如果当前绘制的字母和按下的字母索引一样, 用灰色的画笔  改变字母的颜色 通过调用invalidate方法
			//paint.setColor(i == index ? Color.GRAY : Color.WHITE);
			
			canvas.drawText(text, x, y, paint);
		}
		
	}
	
	int index = -1;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		float y = -1;
		int currentIndex = -1;
		switch (MotionEventCompat.getActionMasked(event)) {
			case MotionEvent.ACTION_DOWN:
				y = event.getY();
				currentIndex  = (int) (y / cellHeight);
				if(currentIndex >= 0 && currentIndex < letters.length){
					// 健壮性处理, 在正常范围内
					if(index != currentIndex){
						// 字母的索引发生了变化
						if(onLetterChangeListener != null){
							onLetterChangeListener.onLetterChange(letters[currentIndex]);
						}
						index = currentIndex;
					}
				}
				
				break;
			case MotionEvent.ACTION_MOVE:
				y = event.getY();
				currentIndex  = (int) (y / cellHeight);
				if(currentIndex >= 0 && currentIndex < letters.length){
					// 健壮性处理, 在正常范围内
					if(index != currentIndex){
						// 字母的索引发生了变化
						if(onLetterChangeListener != null){
							onLetterChangeListener.onLetterChange(letters[currentIndex]);
						}
						index = currentIndex;
					}
				}
				
				break;
			case MotionEvent.ACTION_UP:
				index = -1;
				
				break;
	
			default:
				break;
		}
		// 相当于是调用了OnDraw方法 导致滑动的过程中 字母的颜色会改变
		invalidate();
		
		return true;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		cellWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		cellHeight = mHeight * 1.0f / letters.length;
		
	}
	
	
}
