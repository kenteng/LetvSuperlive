/**
 * 
 */
package com.lesports.stadium.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * ***************************************************************
 * 
 * @Desc : 
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */

public class MyProgressBar extends ProgressBar {
	@SuppressWarnings("unused")
	private String text_progress;
	@SuppressWarnings("unused")
	private Paint mPaint;//画笔

	public MyProgressBar(Context context) {
		super(context);
//		initPaint();
	}
	public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initPaint();
    }
	public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        initPaint();''
    }
	
	@Override
	public synchronized void setProgress(int progress) {
		super.setProgress(progress);
//		setTextProgress(progress);//这行代码是为了在进度条上面显示进度数据的
	}
//	@Override
//	protected synchronized void onDraw(Canvas canvas) {
//		// TODO Auto-generated method stub
//		super.onDraw(canvas);
//		Rect rect=new Rect();
//		this.mPaint.getTextBounds(this.text_progress, 0, this.text_progress.length(), rect);
//        int x = (getWidth() / 2) - rect.centerX();
//        int y = (getHeight() / 2) - rect.centerY();
//        canvas.drawText(this.text_progress, x, y, this.mPaint);
//	}
	/**
	 * 
	 *description: 初始化画笔
	 *Create by lll on 2013-8-13 下午1:41:49
	 */
//	private void initPaint(){
//		this.mPaint=new Paint();
//		this.mPaint.setAntiAlias(true);
//		this.mPaint.setColor(Color.WHITE);
//	}
//	private void setTextProgress(int progress){ 
//		int i = (int) ((progress * 1.0f / this.getMax()) * 100);
//        this.text_progress = String.valueOf(i) + "%";
//	}



}