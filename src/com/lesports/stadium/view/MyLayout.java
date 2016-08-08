package com.lesports.stadium.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class MyLayout extends RelativeLayout {

	private AbstractPathAnimator mAnimator;
	Context context;

	public MyLayout(Context context) {
		super(context);
		this.context = context;
		init(null, 0);
	}

	public MyLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(attrs, 0);
	}

	public MyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs, defStyleAttr);
	}

	private void init(AttributeSet attrs, int defStyleAttr) {
		mAnimator = new PathAnimator(this.getMeasuredHeight());
	}
	
	public void setStartPoint(int point){
		mAnimator.setPoint(point);
	}

	public void clearAnimation() {
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).clearAnimation();
		}
		removeAllViews();
	}

	public void addChildView(View view) {
		this.removeView(view);
		mAnimator.start(view, this);
	}
	public void addHeartView(View view,int pointX, int pointY) {
		mAnimator.startHeart(view, this, pointX, pointY);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

}
