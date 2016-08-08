package com.lesports.stadium.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class LetvBaseActivity extends FragmentActivity {


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	/**
	 * 退出应用
	 */
	@SuppressWarnings("unused")
	private void exitProcess() {
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
