package com.lesports.stadium.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.widget.ImageView;


@SuppressLint("DrawAllocation")
public class AntiAliasImageView extends ImageView {
	private Paint paint;

	public AntiAliasImageView(Context context) {
		this(context, null);
	}

	public AntiAliasImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AntiAliasImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(7);
		paint.setStyle(Paint.Style.FILL);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0,
				Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG));
		super.onDraw(canvas);

	}
}
