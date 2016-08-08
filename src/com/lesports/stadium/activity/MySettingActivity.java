package com.lesports.stadium.activity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;

import com.lesports.stadium.R;
import com.lesports.stadium.app.LApplication;
import com.lesports.stadium.bean.ShareModel;
import com.lesports.stadium.sharedpreference.LoginSpManager;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.GlobalParams;
import com.lesports.stadium.utils.JPushUtils;
import com.lesports.stadium.utils.SharedPreferencesUtils;
import com.lesports.stadium.view.CustomDialog;
import com.lesports.stadium.view.SharePopupWindow;

/**
 * 
 * ***************************************************************
 * 
 * @ClassName: MySettingActivity
 * 
 * @Desc : 设置界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:2016-1-29 上午11:29:57
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class MySettingActivity extends Activity implements OnClickListener,
		PlatformActionListener, Callback {
	/**
	 * Popupwindow 分享选择界面
	 */
	private SharePopupWindow share;
	/**
	 * 分享链接地址
	 */
	private String weburl = "http://s3.lecloud.com/gene_static/smartGYM/app/android/smartGYM.apk";
	// private String imageurl =
	// "http://h.hiphotos.baidu.com/image/pic/item/ac4bd11373f082028dc9b2a749fbfbedaa641bca.jpg";
	/**
	 * 分享说明
	 */
	private String text = "Superlive，让你和偶像零距离 ";
	/**
	 * 当前app名称
	 */
	private String title = ConstantValue.APP_NAME;
	/**
	 * 登陆提示框
	 */
	private CustomDialog exitDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mysetting);
		ShareSDK.initSDK(this);
		initview();
	}

	/**
	 * 界面初始化
	 */
	private void initview() {
		View droup = findViewById(R.id.droup);
		droup.setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.rl_judej).setOnClickListener(this);
		findViewById(R.id.rl_suggestion).setOnClickListener(this);
		findViewById(R.id.rl_aboutus).setOnClickListener(this);
		findViewById(R.id.rl_recordment).setOnClickListener(this);
		if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
			droup.setVisibility(View.GONE);
		} else {
			droup.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.droup:
			// 退出当前账号
			GlobalParams.IS_REFRESH = true;
			droupOut();
			break;
		case R.id.back:
			finish();
			break;
		case R.id.rl_judej:
			// 给我们评价
			Uri uri = Uri.parse("market://details?id=" + getPackageName());
			intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			try {
				startActivity(intent);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "你手机没有安装市场应用 ，无法评价", 0)
						.show();
			}

			break;
		case R.id.rl_suggestion:
			// 意见反馈
			if (TextUtils.isEmpty(GlobalParams.USER_ID)) {
				createDialog();
				return;
			}
			intent = new Intent(this, SuggestionActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_aboutus:
			intent = new Intent(this, AboutUsActivity.class);
			startActivity(intent);
			break;
		case R.id.rl_recordment:
			// 推荐给小伙伴（分享）
			showPop();
			break;

		default:
			break;
		}

	}

	/**
	 * 显示分享选择界面
	 */
	private void showPop() {
		share = new SharePopupWindow(this);
		share.setPlatformActionListener(this);
		ShareModel model = new ShareModel();
		// model.setImageUrl(imageurl);
		// model.setUrl(imageurl);
		model.setText(text);
		model.setTitle(title);
		share.initShareParams(model);
		share.setWebUrl(weburl);
		share.showShareWindow(false);
		share.setTopShow("分享", false);
		// 显示窗口 (设置layout在PopupWindow中显示的位置)
		share.showAtLocation(this.findViewById(R.id.set_share), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (share != null) {
			share.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		share = null;
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		Message msg = new Message();
		msg.what = 0;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onComplete(Platform plat, int action,
			HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		Message msg = new Message();
		msg.what = 1;
		UIHandler.sendMessage(msg, this);
	}

	@Override
	public boolean handleMessage(Message msg) {
		int what = msg.what;
		if (what == 1) {
			Toast.makeText(this, "分享失败", Toast.LENGTH_SHORT).show();
		}
		if (share != null) {
			share.dismiss();
		}
		return false;
	}

	/**
	 * 退出登录初始化数据
	 */
	private void droupOut() {
		JPushUtils.setAlias("");
		GlobalParams.NICK_NAME = "";
		GlobalParams.USER_GENDER = "";
		GlobalParams.USER_HEADER = "";
		GlobalParams.USER_NAME = "";
		GlobalParams.USER_ID = "";
		GlobalParams.USER_PHONE = "";
		GlobalParams.USER_INTEGRAL = 0;
		SharedPreferencesUtils
				.clear(getApplicationContext(), "ls_user_message");
		LoginSpManager.logout(getApplicationContext());
		LApplication.dbBuyCar.delete(null, null);
		LApplication.foodbuycar.delete(null, null);
		finish();
	}

	/**
	 * 创建未登陆提示框
	 */
	private void createDialog() {
		exitDialog = new CustomDialog(this, new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MySettingActivity.this,
						LoginActivity.class);
				startActivity(intent);
				exitDialog.dismiss();
			}
		}, new OnClickListener() {

			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
		exitDialog.setRemindTitle("温馨提示");
		exitDialog.setCancelTxt("取消");
		exitDialog.setConfirmTxt("立即登录");
		exitDialog.setRemindMessage("您还没有登录哦");
		exitDialog.show();
	}

}
