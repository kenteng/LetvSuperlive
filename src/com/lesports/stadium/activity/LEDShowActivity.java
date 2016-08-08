package com.lesports.stadium.activity;

import com.lesports.stadium.view.LEDBoardView;
import com.lidroid.xutils.view.annotation.event.OnTouch;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class LEDShowActivity extends Activity {
	
	private Bundle mbundle;
	private LEDBoardView ledboardview;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.screenBrightness = 1.0f;
		this.getWindow().setAttributes(lp);
	}
	@Override
	public void onPause(){
		super.onPause();
		ledboardview.stopRunning();
	}
	@Override
	public void onResume(){
		super.onResume();
		mbundle = this.getIntent().getExtras();
		ledboardview = new LEDBoardView(this);
		ledboardview.setSpeed(mbundle.getInt("speed"));
		ledboardview.setHanziOrientation(mbundle.getBoolean("hanziorientation"));
		ledboardview.setMoveOrientation(mbundle.getBoolean("moveorientation"));
		ledboardview.setColor(mbundle.getInt("hanzicolor"));
		ledboardview.setLEDshape(mbundle.getBoolean("LEDshape"));
		ledboardview.setString(mbundle.getString("mstring"));
		ledboardview.setLEDBoardHeight(mbundle.getInt("hanziheight"));
		setContentView(ledboardview);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return super.onTouchEvent(event);
	}
}