package com.lesports.stadium.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.lesports.stadium.R;


public class BubbleImageView extends ImageView {

	private static final int LOCATION_LEFT = 0;
	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 1;
	private ColorFilter cf;
//	private int mAngle = dp2px(15);
//	private int mArrowTop = dp2px(0);
//	private int mArrowWidth = dp2px(5);
//	private int mArrowHeight = dp2px(5);
//	private int mArrowOffset = 0;
	private int mArrowLocation = LOCATION_LEFT;

	private Rect mDrawableRect;
	private Bitmap mBitmap;
	private BitmapShader mBitmapShader;
	private Paint mBitmapPaint;
	private Matrix mShaderMatrix;
	private int mBitmapWidth;
	private int mBitmapHeight;
	
	//属性
		private int leftTopRadius;//左上角
		private int rightTopRadius;//右上角
		private int rightBottomRadius;//右下角
		private int leftBottomRadius;//左下角
		
		private int width;
		private int height;

	public BubbleImageView(Context context) {
		super(context);
		initView(null);
	}

	public BubbleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(attrs);
	}

	public BubbleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(attrs);
	}

	private void initView(AttributeSet attrs) {
		if (attrs != null) {
//			TypedArray a = getContext().obtainStyledAttributes(attrs,
//					R.styleable.BubbleImageView);
//			mAngle = (int) a.getDimension(
//					R.styleable.BubbleImageView_bubble_angle, mAngle);
//			mArrowHeight = (int) a.getDimension(
//					R.styleable.BubbleImageView_bubble_arrowHeight,
//					mArrowHeight);
//			mArrowOffset = (int) a.getDimension(
//					R.styleable.BubbleImageView_bubble_arrowOffset,
//					mArrowOffset);
//			mArrowTop = (int) a.getDimension(
//					R.styleable.BubbleImageView_bubble_arrowTop, mArrowTop);
//			mArrowWidth = (int) a.getDimension(
//					R.styleable.BubbleImageView_bubble_arrowWidth, mAngle);
//			mArrowLocation = a.getInt(
//					R.styleable.BubbleImageView_bubble_arrowLocation,
//					mArrowLocation);
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BubbleImageView);
			int radius = (int)a.getDimension(R.styleable.BubbleImageView_radiuss, 0);
			leftTopRadius = (int)a.getDimension(R.styleable.BubbleImageView_leftTopRadius, radius);
			rightTopRadius = (int)a.getDimension(R.styleable.BubbleImageView_rightTopRadius, radius);
			rightBottomRadius = (int)a.getDimension(R.styleable.BubbleImageView_rightBottomRadius, radius);
			leftBottomRadius = (int)a.getDimension(R.styleable.BubbleImageView_leftBottomRadius, radius);
			width= (int)a.getDimension(R.styleable.BubbleImageView_roundImageW, radius);
			height= (int)a.getDimension(R.styleable.BubbleImageView_roundImageH, radius);
			a.recycle();
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}
		width = getMeasuredWidth();
		RectF rect = new RectF(getPaddingLeft(), getPaddingTop(), getRight()
				- getLeft() - getPaddingRight(), getBottom() - getTop()
				- getPaddingBottom());

		Path path = new Path();

		if (mArrowLocation == LOCATION_LEFT) {
			leftPath(rect, path);
		} else {
			rightPath(rect, path);
		}

		canvas.drawPath(path, mBitmapPaint);

		Paint paint = new Paint();

		// 去锯齿
		paint.setAntiAlias(true);
		paint.setColor(Color.parseColor("#999999"));
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);

		canvas.drawPath(path, paint);
	}

	public void rightPath(RectF rect, Path path) {
//		path.moveTo(mAngle, rect.top);
//		path.lineTo(rect.width() - 50, rect.top);
//		path.arcTo(new RectF(rect.right - mAngle * 2 - mArrowWidth, rect.top,
//				rect.right - mArrowWidth, mAngle * 2 + rect.top), 270, 90);
//		path.lineTo(rect.right - mArrowWidth, mArrowTop);
//		path.lineTo(rect.right, mArrowTop - mArrowOffset);
//		path.lineTo(rect.right - mArrowWidth, mArrowTop + mArrowHeight);
//		path.lineTo(rect.right - mArrowWidth, rect.height() - mAngle);
//		path.arcTo(new RectF(rect.right - mAngle * 2 - mArrowWidth, rect.bottom
//				- mAngle * 2, rect.right - mArrowWidth, rect.bottom), 0, 90);
//		// path.lineTo(rect.left, rect.bottom);
//		path.arcTo(new RectF(rect.left, rect.bottom - mAngle * 2, mAngle * 2
//				+ rect.left, rect.bottom), 90, 90);
//		path.lineTo(rect.left, 50);
//		path.arcTo(new RectF(rect.left, rect.top, mAngle * 2 + rect.left,
//				mAngle * 2 + rect.top), 180, 90);
//		path.close();

	}

	public void leftPath(RectF rect, Path path) {
//		path.moveTo(mAngle + mArrowWidth, rect.top);
//		path.lineTo(rect.width() - 50, rect.top);
//		path.arcTo(new RectF(rect.right - mAngle * 2, rect.top, rect.right,
//				mAngle * 2 + rect.top), 270, 90);
//		// path.lineTo(rect.right, rect.top-50);
//		path.arcTo(new RectF(rect.right - mAngle * 2, rect.bottom - mAngle * 2,
//				rect.right, rect.bottom), 0, 90);
//		path.lineTo(rect.left + mArrowWidth + 50, rect.bottom);
//		path.arcTo(new RectF(rect.left + mArrowWidth, rect.bottom - mAngle * 2,
//				mAngle * 2 + rect.left + mArrowWidth, rect.bottom), 90, 90);
//		path.lineTo(rect.left + mArrowWidth, mArrowTop + mArrowHeight);
//		path.lineTo(rect.left, mArrowTop - mArrowOffset);
//		path.lineTo(rect.left + mArrowWidth, mArrowTop);
//		// path.lineTo(rect.left + mArrowWidth, rect.top-50);
//		path.arcTo(new RectF(rect.left + mArrowWidth, rect.top, mAngle * 2
//				+ rect.left + mArrowWidth, mAngle * 2 + rect.top), 180, 90);
//		path.close();
//		Log.e("SSSSSSSSSSSSS", width+","+height);
		//左上角
		path.moveTo(0, this.leftTopRadius/2);
		if(this.leftTopRadius != 0){
			path.arcTo(new RectF(0, 0, this.leftTopRadius*2, this.leftTopRadius*2), 180, 90);
		}
		//右上角
		if(this.rightTopRadius != 0){
			path.lineTo(width-this.rightTopRadius, 0);
			path.arcTo(new RectF(width-this.rightTopRadius*2, 0, width, this.rightTopRadius*2), 270, 90);
		}else{
			path.lineTo(width, 0);
		}
		//右下角
		if(this.rightBottomRadius != 0){
			path.lineTo(width, height-this.rightBottomRadius);
			path.arcTo(new RectF(width-this.rightBottomRadius*2, height-this.rightBottomRadius*2, width, height), 0, 90);
		}else{
			path.lineTo(width, height);
		}
		//左下角
		if(this.leftBottomRadius != 0){
			path.lineTo(this.leftBottomRadius, height);
			path.arcTo(new RectF(0, height-this.leftBottomRadius*2, this.leftBottomRadius*2, height), 90, 90);
		}else{
			path.lineTo(0, height);
		}
		path.close();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		mBitmap = bm;
		setup();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		mBitmap = getBitmapFromDrawable(drawable);
		setup();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
						COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	private void setup() {
		if (mBitmap == null) {
			return;
		}

		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);

		mBitmapPaint = new Paint();
		if (cf != null) {
			mBitmapPaint.setColorFilter(cf);
		}
		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setShader(mBitmapShader);
		mBitmapHeight = mBitmap.getHeight();
		mBitmapWidth = mBitmap.getWidth();
		updateShaderMatrix();
		invalidate();
	}

	public void setColorFilter(ColorFilter cf) {
		this.cf = cf;
	}

	private void updateShaderMatrix() {
//		float scale;
//		float dx = 0;
//		float dy = 0;
//
//		mShaderMatrix = new Matrix();
//		mShaderMatrix.set(null);
//
//		mDrawableRect = new Rect(0, 0, getRight() - getLeft(), getBottom()
//				- getTop());
//
//		if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
//				* mBitmapHeight) {
//			scale = mDrawableRect.height() / (float) mBitmapHeight;
//			dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
//		} else {
//			scale = mDrawableRect.width() / (float) mBitmapWidth;
//			dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
//		}
//
//		mShaderMatrix.setScale(scale, scale);
//		mShaderMatrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
		
		float scalex;
		float scaley;
		@SuppressWarnings("unused")
		float dx = 0;
		@SuppressWarnings("unused")
		float dy = 0;

		mShaderMatrix = new Matrix();
		mShaderMatrix.set(null);

		mDrawableRect = new Rect(0, 0, getRight() - getLeft(), getBottom()
				- getTop());

//		if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
//				* mBitmapHeight) {
			scaley = mDrawableRect.height() / (float) mBitmapHeight;
//			dx = (mDrawableRect.width() - mBitmapWidth * scaley) * 0.5f;
//		} else {
			scalex = mDrawableRect.width() / (float) mBitmapWidth;
//			dy = (mDrawableRect.height() - mBitmapHeight * scalex) * 0.5f;
//		}

		mShaderMatrix.setScale(scalex, scaley);
//		mShaderMatrix.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));

		mBitmapShader.setLocalMatrix(mShaderMatrix);
	}

	@SuppressWarnings("unused")
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}

}
