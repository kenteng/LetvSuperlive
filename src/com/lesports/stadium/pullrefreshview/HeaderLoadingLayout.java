package com.lesports.stadium.pullrefreshview;

import com.lesports.stadium.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * 这个类封装了下拉刷新的布局
 * 
 * @author Li Hong
 * @since 2013-7-30
 */
public class HeaderLoadingLayout extends LoadingLayout {
	/** 旋转动画时间 */
	private static final int ROTATE_ANIM_DURATION = 350;
	/** Header的容器 */
	private RelativeLayout mHeaderContainer;
//	/** 箭头图片 */
//	private ImageView mArrowImageView;
	/** 进度条 */
	private ProgressBar mProgressBar;
	/** 状态提示TextView */
	private TextView mHintTextView;
	/** 最后更新时间的TextView */
	private TextView mHeaderTimeView;
	/** 最后更新时间的标题 */
	private TextView mHeaderTimeViewTitle;
	/** 向上的动画 */
	private Animation mRotateUpAnim;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 */
	public HeaderLoadingLayout(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public HeaderLoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 *            context
	 */
	private void init(Context context) {
		mHeaderContainer = (RelativeLayout) findViewById(R.id.pull_to_refresh_header_content);
//		mArrowImageView = (ImageView) findViewById(R.id.pull_to_refresh_header_arrow);
		mHintTextView = (TextView) findViewById(R.id.pull_to_refresh_header_hint_textview);
		mProgressBar = (ProgressBar) findViewById(R.id.pull_to_refresh_header_progressbar);
		// mHeaderTimeView = (TextView)
		// findViewById(R.id.pull_to_refresh_header_time);
		// mHeaderTimeViewTitle = (TextView)
		// findViewById(R.id.pull_to_refresh_last_update_time_text);

		float pivotValue = 0.5f; // SUPPRESS CHECKSTYLE
		@SuppressWarnings("unused")
		float toDegree = -180f; // SUPPRESS CHECKSTYLE

		// 初始化旋转动画
		mRotateUpAnim = new RotateAnimation(0, 20, Animation.RELATIVE_TO_SELF,
				pivotValue, Animation.RELATIVE_TO_SELF, pivotValue);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setRepeatMode(Animation.REVERSE);
		mRotateUpAnim.setFillAfter(true);
		mRotateUpAnim.setInterpolator(new LinearInterpolator());
		mRotateUpAnim.setRepeatCount(10);



	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		// 如果最后更新的时间的文本是空的话，隐藏前面的标题
		mHeaderTimeViewTitle
				.setVisibility(TextUtils.isEmpty(label) ? View.INVISIBLE
						: View.VISIBLE);
		mHeaderTimeView.setText(label);
	}

	@Override
	public int getContentSize() {
		if (null != mHeaderContainer) {
			return mHeaderContainer.getHeight();
		}

		return (int) (getResources().getDisplayMetrics().density * 60);
	}

	@Override
	protected View createLoadingView(Context context, AttributeSet attrs) {
		View container = LayoutInflater.from(context).inflate(
				R.layout.pull_to_refresh_header, null);
		return container;
	}

	@Override
	protected void onStateChanged(State curState, State oldState) {
//		mArrowImageView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);

		super.onStateChanged(curState, oldState);
	}

	@Override
	protected void onReset() {
//		mArrowImageView.clearAnimation();
		mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
	}

	@Override
	protected void onPullToRefresh() {
		if (State.RELEASE_TO_REFRESH == getPreState()) {
			// mArrowImageView.clearAnimation();
			// mArrowImageView.startAnimation(mRotateDownAnim);
		}

		mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
	}

	@Override
	protected void onReleaseToRefresh() {
//		mArrowImageView.clearAnimation();
		// mArrowImageView.startAnimation(mRotateUpAnim);
		mHintTextView.setText(R.string.pull_to_refresh_header_hint_ready);
	}

	@Override
	protected void onRefreshing() {
//		mArrowImageView.clearAnimation();
//		mArrowImageView.startAnimation(mRotateUpAnim);
		// mArrowImageView.startAnimation(animationSet);
//		mArrowImageView.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.GONE);
		mHintTextView.setText(R.string.pull_to_refresh_header_hint_loading);
	}

	@Override
	public void onPull(float scale) {
		// TODO Auto-generated method stub
		super.onPull(scale);
	}

	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNoMoreData() {
		// TODO Auto-generated method stub

	}
}
