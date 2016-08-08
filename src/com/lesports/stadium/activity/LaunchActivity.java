package com.lesports.stadium.activity;

import com.lesports.stadium.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class LaunchActivity extends Activity {
	private boolean isFirstIn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		setContentView(R.layout.activity_launch);
		 //时间延迟3000ms后执行。
        new Handler().postDelayed(new Runnable() {
             
            @Override
            public void run() {

            	
            	SharedPreferences preferences = getSharedPreferences(
        				"guideFirst", MODE_PRIVATE);

        		isFirstIn = preferences.getBoolean("isFirstIn", true);
        		if(isFirstIn){
        			Intent intent=new Intent();
        				intent.setClass(LaunchActivity.this, GuideActivity.class);
        				startActivity(intent);
        		}
        		
        		LaunchActivity.this.finish();
        		
            }
        }, 2000);
		
	}
	
	 //屏蔽返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return false;
        }
        return false;
    }
}
