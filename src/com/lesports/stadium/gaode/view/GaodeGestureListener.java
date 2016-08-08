package com.lesports.stadium.gaode.view;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

public class GaodeGestureListener implements  OnTouchListener,OnGestureListener {
	Context context;
	PopupWindow pop;
	public GaodeGestureListener(Context context){
		this.context=context;
	}

	public GaodeGestureListener(Context context,
			PopupWindow pop) {
		this.context=context;
		this.pop=pop;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		 Intent intent = new Intent("android.settings.BLUETOOTH_SETTINGS");
		 context.startActivity(intent);
		 pop.dismiss();
		return false;
	}



}
