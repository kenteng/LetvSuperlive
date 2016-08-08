package com.lesports.stadium.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.lesports.stadium.utils.DensityUtil;

/**
 * ***************************************************************
 * 
 * @ClassName: MyVisualizerView
 * 
 * @Desc : 自定义音波控件 用于显示当前声音大小
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-2-2 上午10:54:33
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class MyVisualizerView extends View {
	/**
	 * x轴起始位置
	 */
	private int startX = 0;
	/**
	 * y轴 起始位置
	 */
	private int startY = 0;
	/**
	 * 格子的高和宽
	 */
	private int GRID_WIDTH = 12;
	/**
	 * 要画的棋盘中的线数
	 */
	private int GRID_NUM = 10;
	/**
	 * 要画的棋盘中的线数
	 */
	private int GRID_NUM_LINE = 11;
	/**
	 * 左侧标题
	 */
	private final String[] title = new String[] { "0", "20", "40", "60", "80",
			"100", "120", "140", "160", "180" };
	/**
	 * 画笔
	 */
	protected Paint mPaint = null;
	/**
	 * 用于存储当前声音计算后的分贝值
	 */
	private ArrayList<Integer> buf;
	/**
	 * 绘制baseLine
	 */
	private int baseLine;
	/**
	 * 为了节约绘画时间，每三个像素画一个数据
	 */
	int divider = 6;
	int base;
	int heightCount;

	/**
	 * @param context
	 */
	public MyVisualizerView(Context context) {
		super(context);
//		divider = DensityUtil.dip2px(context, 1);
		GRID_WIDTH = DensityUtil.dip2px(context, GRID_WIDTH);
		heightCount = GRID_WIDTH*GRID_NUM;
		base = GRID_WIDTH/10;
		mPaint = new Paint();// 初始化画笔工具
		mPaint.setAntiAlias(true);// 抗锯齿
		mPaint.setColor(Color.BLACK);// 画笔颜色
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		GRID_NUM_LINE = dm.widthPixels / GRID_WIDTH;
	}

	@Override
	public void onDraw(Canvas canvas) {
		initBg(canvas);
		SimpleDraw(canvas);
	}

	private void initBg(Canvas canvas) {
		mPaint.setColor(0xffffffff);// 设置画笔的颜色
		mPaint.setAntiAlias(true);// 去锯齿
		mPaint.setStrokeWidth(1);
		// 绘制棋盘
		for (int i = 0; i < GRID_NUM; i++) {
			// 画横线
			canvas.drawLine(startX, startY +(i+1) * GRID_WIDTH, startX
					+ GRID_NUM_LINE * GRID_WIDTH, startY + (i+1) * GRID_WIDTH,
					mPaint);

		}
		for (int i = 0; i < GRID_NUM_LINE; i++) {
			// 画纵线
			if (i < GRID_NUM_LINE - 1)
				canvas.drawLine(startX + i * GRID_WIDTH, startY+GRID_WIDTH, startX + i
						* GRID_WIDTH, startY+GRID_NUM * GRID_WIDTH,
						mPaint);
		}
		// 绘制右侧字体
		mPaint.setColor(0x66ffffff);// 设置画笔的颜色
		int size = DensityUtil.dip2px(getContext(), 7);
		mPaint.setTextSize(size);
		int z = title.length - 1;
		for (int i = 0; i < GRID_NUM; i++) {
			String text = title[z - i];
			size = (int) mPaint.measureText(text);
			int x =  GRID_WIDTH - size / 2;
			int y = size / 2+GRID_WIDTH/3;
			if(i == GRID_NUM-1)
				y =size / 2+GRID_WIDTH/2;
			canvas.drawText(text, x + (GRID_NUM_LINE - 2) * GRID_WIDTH, y +i
					* GRID_WIDTH, mPaint);
		}

	}

	/**
	 * 
	 * @Title: SimpleDraw
	 * @Description: 绘图
	 * @param: @param canvas
	 * @return: void
	 * @throws
	 */
	private void SimpleDraw(Canvas canvas) {
		if (buf == null)
			return;
		if (buf.size() > 0) {
			int count = buf.get(0)/20*GRID_WIDTH+DensityUtil.dip2px(getContext(), buf.get(0)%20);
			baseLine = buf.get(0) < 0 ? 0 : count;
		} else
			return;
//			baseLine = 0;
		baseLine = heightCount - baseLine;
//		int start = getMeasuredWidth() - buf.size() * divider; //从右向左
		int start =  buf.size() * divider;
		int py = baseLine;
		if (buf.size() > 0) {
			int count = buf.get(0)/20*GRID_WIDTH+DensityUtil.dip2px(getContext(), buf.get(0)%20);
			py += count;
		}
		int y;
		mPaint.setColor(0xff49D5ff);
		mPaint.setStrokeWidth(3);
//		canvas.drawLine(0, baseLine, start - divider, baseLine, mPaint);
		for (int i = 0; i < buf.size(); i++) {
            // 调节缩小比例，调节基准线
			y =buf.get(i)/20*GRID_WIDTH+DensityUtil.dip2px(getContext(), buf.get(i)%20);
			y = heightCount - y;
			if (py < 0 || py > heightCount) {
				py = heightCount;
			}
			if (y < 0 || y > heightCount) {
				y = heightCount;
			}
			if(y==0)
				y=20;
			if(py==0)
				py = y;
			canvas.drawLine(start - i * divider,
					py,start -(i +1) * divider, y, mPaint);
//			canvas.drawLine(start + (i - 1) * divider, py, start + i * divider,
//					y, mPaint);   //从右向左
			py = y;
		}
	}

	public void getAll(ArrayList<Integer> buf) {
		this.buf = buf;
		postInvalidate();// 刷新界面
	}

}
