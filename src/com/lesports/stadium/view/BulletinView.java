package com.lesports.stadium.view;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class BulletinView extends TextView {
	@SuppressWarnings("unused")
	private int textWidth;
	private List<String> mList;
	private int currentNews = 0;

	public BulletinView(Context context) {
		super(context);
		init();
	}

	public BulletinView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BulletinView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init() {
		setClickable(true);
		setSingleLine(true);
		setEllipsize(TruncateAt.MARQUEE);
		setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
	}

	public void setData(List<String> mList) {
		if (mList == null || mList.size() == 0) {
			return;
		}
		this.mList = mList;
		currentNews = 0;
		String n = mList.get(currentNews);
		setText(n);
		setTag(n);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		MeasureTextWidth();
	}



	/**
	 * 获取文字宽度
	 */
	private void MeasureTextWidth() {
		Paint paint = this.getPaint();
		String str = this.getText().toString();
		textWidth = (int) paint.measureText(str);
	}


	private void nextNews() {
		String n = mList.get(currentNews);
		setText(n);
		setTag(n);
	}

}
