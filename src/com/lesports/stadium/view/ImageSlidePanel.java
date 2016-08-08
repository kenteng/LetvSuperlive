package com.lesports.stadium.view;

import java.util.ArrayList;
import java.util.List;

import com.lesports.stadium.activity.LiveDetialActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;


@SuppressLint({ "HandlerLeak", "NewApi" })
public class ImageSlidePanel extends FrameLayout {
	private List<RelativeLayout> viewList = new ArrayList<RelativeLayout>();
	private RelativeLayout lastView;
	private Handler uiHandler;


	private final ViewDragHelper mDragHelper;
	private int initCenterViewX = 0;
	private int screenWidth = 0;

	private int rotateAnimTime = 100; 

	private static final int MSG_TYPE_IN_ANIM = 1; 
	private static final int MSG_TYPE_ROTATION = 2;
	private static final int XVEL_THRESHOLD = 100; 
	private boolean isRotating = false;

	public ImageSlidePanel(Context context) {
		this(context, null);
	}

	public ImageSlidePanel(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@SuppressWarnings("deprecation")
	public ImageSlidePanel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		screenWidth = wm.getDefaultDisplay().getWidth();

		uiHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle data = msg.getData();
				int cycleNum = data.getInt("cycleNum");

				if (msg.what == MSG_TYPE_IN_ANIM) {
					processInAnim(cycleNum);
				} else if (msg.what == MSG_TYPE_ROTATION) {
					processRotaitonAnim(cycleNum);
					
				}
			}
		};

		mDragHelper = ViewDragHelper
				.create(this, 10f, new DragHelperCallback());
		mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
	}

	@Override
	protected void onFinishInflate() {
		initViewList();
	}

	class XScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx,
				float dy) {
			return Math.abs(dy) > Math.abs(dx);
		}
	}

//初始化子布局
	private void initViewList() {
		viewList.clear();
		int num = getChildCount();
		for (int i = 0; i < num; i++) {
			RelativeLayout tv = (RelativeLayout) getChildAt(i);
	//		tv.setRotation((num - 1 - i) * rotateDegreeStep);
			ObjectAnimator obj1 = ObjectAnimator.ofFloat(tv, "translationX", 0f,-(num - 1 - i)*30);
			ObjectAnimator obj2 = ObjectAnimator.ofFloat(tv, "translationY", 0f,(num - 1 - i)*30);
			AnimatorSet set = new AnimatorSet();
			set.setDuration(400);
			set.setInterpolator(new LinearInterpolator());
			set.playTogether(obj1,obj2);
			set.start();
			viewList.add(tv);
		}

		lastView = viewList.get(viewList.size() - 1);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		initCenterViewX = lastView.getLeft();
	}

	private class DragHelperCallback extends ViewDragHelper.Callback {

		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			if (left == -lastView.getWidth() || left == screenWidth) {

				if (!isRotating) {
					isRotating = true;
					mDragHelper.abort();


					int offsetLeftAndRight;
					if (left < 0) {
						offsetLeftAndRight = Math.abs(left) + initCenterViewX;
					} else {
						offsetLeftAndRight = initCenterViewX - left;
					}

					lastView.offsetLeftAndRight(offsetLeftAndRight);
					orderViewStack();
				}
			} else if (!isRotating && changedView.getRotation() == 0) {
				processAlphaGradual(changedView, left);
			}
		}

		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			if (child == lastView) {
				return true;
			}
			return false;
		}

		@Override
		public int getViewHorizontalDragRange(View child) {
			return 256;
		}

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			animToFade(xvel);
		}

		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			return left;
		}

		@Override
		public int clampViewPositionVertical(View child, int top, int dy) {
			return child.getTop();
		}
	}

//动画之后初始化位置
	private void orderViewStack() {
		int num = viewList.size();
		for (int i = 0; i < num - 1; i++) {
			RelativeLayout tempView = viewList.get(i);
			tempView.bringToFront();
		}
		invalidate();

		lastView.setAlpha(1.0f);
		ObjectAnimator obj1 = ObjectAnimator.ofFloat(lastView, "translationX", 0f,-60);
		ObjectAnimator obj2 = ObjectAnimator.ofFloat(lastView, "translationY", 0f,60);
		AnimatorSet set = new AnimatorSet();
		set.setDuration(100);
		set.setInterpolator(new LinearInterpolator());
		set.playTogether(obj1,obj2);
		set.start();
		viewList.remove(lastView);
		viewList.add(0, lastView);
		lastView = viewList.get(viewList.size() - 1);

		new MyThread(MSG_TYPE_ROTATION, viewList.size(), rotateAnimTime).start();
	}


	private void animToFade(float xvel) {

		int finalLeft = initCenterViewX;

		if (xvel > XVEL_THRESHOLD) {
			finalLeft = screenWidth;
		}
//		else if (xvel < -XVEL_THRESHOLD) {
//			finalLeft = -lastView.getWidth();
//		} 
		else {
//			if (lastView.getLeft() > screenWidth / 2) {
//				finalLeft = screenWidth;
//			} else 
				if (lastView.getRight() < screenWidth / 2) {
				finalLeft = -lastView.getWidth();
			}
		}

		if (mDragHelper.smoothSlideViewTo(lastView, finalLeft,
				lastView.getTop())) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}
	
	

	public void onClickFade(int type) {
		int finalLeft = 0;
		if (type == -1) {
			finalLeft = -lastView.getWidth();
		}
		else if (type == 1) {
			finalLeft = screenWidth;
		}
		
		if (finalLeft != 0) {
			if (mDragHelper.smoothSlideViewTo(lastView, finalLeft,
					lastView.getTop())) {
				ViewCompat.postInvalidateOnAnimation(this);
			}
		}
	}

	@Override
	public void computeScroll() {
		if (mDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	private void processAlphaGradual(View changedView, int left) {
		float alpha = 1.0f;
		int halfScreenWidth = screenWidth / 2;
		if (left > initCenterViewX) {
			if (left > halfScreenWidth) {
				alpha = ((float) left - halfScreenWidth) / halfScreenWidth;
				alpha = 1 - alpha;
			}
		} else if (left < initCenterViewX) {
			if (changedView.getRight() < halfScreenWidth) {
				alpha = ((float) halfScreenWidth - changedView.getRight())
						/ halfScreenWidth;
				alpha = 1 - alpha;
			}
		}

		changedView.setAlpha(alpha);
	}


	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		@SuppressWarnings("unused")
		boolean shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
		int action = ev.getActionMasked();
		if (action == MotionEvent.ACTION_DOWN) {
	
			mDragHelper.processTouchEvent(ev);
		}

		return true;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		mDragHelper.processTouchEvent(e); 
		return true;
	}

//初始化视图
	private void processInAnim(int cycleNum) {
//		Animation animation = AnimationUtils.loadAnimation(getContext(),
//				R.anim.image_in);
//
//		Interpolator interpolator = new OvershootInterpolator(0.8f);
//		animation.setInterpolator(interpolator);
		View view = viewList.get(cycleNum);
		view.setVisibility(View.VISIBLE);

//		view.startAnimation(animation);
	}


	private void processRotaitonAnim(int cycleNum) {
		if (cycleNum >= viewList.size() - 1) {
			isRotating = false;
			return;
		}

		RelativeLayout tv = viewList.get(viewList.size() - 1 - cycleNum);
		float fromDegreex = tv.getTranslationX();

		float fromDegreey = tv.getTranslationY();
//		if(fromDegreex==60.0f){
//			tv.setAlpha(1.0f);
//		}
//		if(fromDegreex==90.0f){
//			tv.setAlpha(0.7f);
//		}
//		Log.e("mmmmmmmmmmmmm",  cycleNum+"");
//		Log.e("mmmmmmmmmmmmm", viewList.size() - 1 - cycleNum+"");
//		Log.e("SSSSSSSSSSSS", fromDegreex+"yyyyyyyyy"+fromDegreey);
		if(fromDegreex==-30.0f){
			int i=(int) tv.getTag();
//			LiveDetialActivity.instance.changeText(i);
		}
		ObjectAnimator obj1 = ObjectAnimator.ofFloat(tv, "translationX", fromDegreex,(fromDegreex+30));
		ObjectAnimator obj2 = ObjectAnimator.ofFloat(tv, "translationY", fromDegreey,(fromDegreey-30));
		AnimatorSet set = new AnimatorSet();
		set.setDuration(400);
		set.setInterpolator(new LinearInterpolator());
		set.playTogether(obj1,obj2);
		set.start();
		
		
	}
	boolean isFirst=true;
	
	public void startInAnim() {
		new MyThread(MSG_TYPE_IN_ANIM, viewList.size(), 100).start();
	}


	class MyThread extends Thread {
		private int num; 
		private int type; 
		private int sleepTime; 

		public MyThread(int type, int num, int sleepTime) {
			this.type = type;
			this.num = num;
			this.sleepTime = sleepTime;
		}

		@Override
		public void run() {
			for (int i = 0; i < num; i++) {
				
				Message msg = uiHandler.obtainMessage();
				msg.what = type;
				Bundle data = new Bundle();
				data.putInt("cycleNum", i);
				msg.setData(data);
				msg.sendToTarget();
				
				
				if(isFirst){					
					viewList.get(i).setTag(i);
				}
				if(i==2){
					isFirst=false;
				}
				try {
					sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
}
