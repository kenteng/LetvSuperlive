/**
 * ***************************************************************
 * @ClassName:  LEDTextShowActivity 
 * 
 * @Desc : 展示led编辑的文字
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 司明
 * 
 * @data:2016-2-3 上午10:53:02
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */

package com.lesports.stadium.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.utils.GlobalParams;

/**
 * @author simon
 *
 */
public class LEDTextShowActivity extends Activity {
	/**
	 * 显示的textview
	 */
	private TextView tv_show;
	/**
	 * 平移动画
	 */
	private TranslateAnimation tlAnim;
	/**
	 * 颜色
	 */
	private String color;
	/**
	 * 速度
	 */
	private String speed;
	/**
	 * 文本
	 */
	private String text;
	private HorizontalScrollView hs_width;
	private boolean first=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ledtext_show);
		Intent intent = getIntent();
		color = intent.getStringExtra("color");
		speed = intent.getStringExtra("speed");
		text = intent.getStringExtra("text");
		Log.e("color", color);
		Log.e("speed", speed);
		Log.e("text", text);
		initView();
		initListener();
	}

	/**
	 * 
	 */
	private void initListener() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	private void initView() {
		hs_width = (HorizontalScrollView) findViewById(R.id.hs_width);
		tv_show = (TextView) findViewById(R.id.tv_show);
		tv_show.setText(text);
		
		if("red".equals(color)){
			tv_show.setTextColor(Color.RED);
		}else if("green".equals(color)){
			tv_show.setTextColor(Color.GREEN);
		}else if("yellow".equals(color)){
			tv_show.setTextColor(Color.YELLOW);
		}
	
	
	}
	

	@Override
	protected void onStart() {
		super.onStart();
		if(first){
		int w = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		hs_width.measure(w, h);
		int height = hs_width.getMeasuredHeight();
		int width = hs_width.getMeasuredWidth();
		
		width=width/5*2;
		
		Log.e("SSSSSSSSSSSSSSSS", hs_width.getWidth()+"");
		Log.e("SSSSSSSSSSSSSSSS", width+"");
		tlAnim = new TranslateAnimation(width+GlobalParams.WIN_HIGTH,-(width+GlobalParams.WIN_HIGTH),0,0);
		if("1".equals(speed)){
			tlAnim.setDuration(8000);
		}else if("2".equals(speed)){
			tlAnim.setDuration(6000);
		}else if("3".equals(speed)){
			tlAnim.setDuration(4000);
		}
		first=false;
//      tlAnim.setInterpolator(new DecelerateAccelerateInterpolator());
        tlAnim.setRepeatCount(9999);
		
	}
		tv_show.startAnimation(tlAnim);
	}

	
}
