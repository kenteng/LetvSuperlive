package com.lesports.stadium.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
public class MyScrollViews extends ScrollView {
	private OnScrollListener onScrollListener;
	
	public MyScrollViews(Context context) {
		this(context, null);
	}
	
	public MyScrollViews(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyScrollViews(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}
	

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(onScrollListener != null){
			onScrollListener.onScrollView(t);
		}
	}
	public interface OnScrollListener{
		public void onScrollView(int scrollY);
	}
	

}
