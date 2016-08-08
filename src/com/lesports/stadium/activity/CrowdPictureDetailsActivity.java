package com.lesports.stadium.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;

import com.lesports.stadium.R;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.ShareModel;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.view.SharePopupWindow;

/**
 * ***************************************************************
 * 
 * @ClassName: CrowdPayActivty
 * 
 * @Desc : 众筹图文详情界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : lwc
 * 
 * @data:2016-4-2 下午5:55:37
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class CrowdPictureDetailsActivity extends BaseActivity implements
		OnClickListener, PlatformActionListener, Callback {

	/**
	 * 加载图文详情的webview
	 */
	private WebView mWeb;
	/**
	 * 顶部返回按钮
	 */
	private ImageView mBack;
	/**
	 * 顶部分享按钮
	 */
	private ImageView mShared;
	/**
	 * 顶部标题
	 */
	private TextView mTitle;
	/**
	 * 分享PopupWindow
	 */
	private SharePopupWindow share;
	/**
	 * 分享url
	 */
	private String imageurl;
	/**
	 * 分享内容
	 */
	private String content;
	/**
	 * 从奖品详情界面跳转该界面 奖品id 分享用到
	 */
	private String prizeid;
	private String title;
	/**
	 * 分享出去的 图片
	 */
	private String shared_imager_url;
	/**
	 * 众筹id
	 * 
	 */
	private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ShareSDK.initSDK(this);
		setContentView(R.layout.activity_pictureandwenzi);
		intviews();
		initdatas();
	}

	/**
	 * 初始化界面数据
	 */
	private void initdatas() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		imageurl = intent.getStringExtra("share_rul");
		id = intent.getStringExtra("id");
		prizeid = intent.getStringExtra("prizeid");
		String name = intent.getStringExtra("name");
		shared_imager_url = intent.getStringExtra("shared_imager_url");
		title = name;
		content = name;
		if (name != null && !TextUtils.isEmpty(name)) {
			mTitle.setText(name);
		}
		if (url != null && !TextUtils.isEmpty(url)
				&& !url.equals("tuwenlianjie")) {
			mWeb.loadDataWithBaseURL(null,
					"<html><body background=\"#111a32\" link=\"white\">" + url
							+ "</body</html>", "text/html", "utf-8", null);
			mWeb.setBackgroundColor(0x00000000);
		} else if (!TextUtils.isEmpty(imageurl)) {
			mWeb.loadUrl(imageurl);
		}
	}

	/**
	 * 初始化view
	 */
	private void intviews() {
		// TODO Auto-generated method stub
		mTitle = (TextView) findViewById(R.id.zhongchou_tv_title);
		mShared = (ImageView) findViewById(R.id.zhongchou_tv_btn);
		mShared.setOnClickListener(this);
		mBack = (ImageView) findViewById(R.id.zhongchou_title_left_iv);
		mBack.setOnClickListener(this);
		mWeb = (WebView) findViewById(R.id.dy_webview);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zhongchou_title_left_iv:
			CrowdPictureDetailsActivity.this.finish();
			break;
		case R.id.zhongchou_tv_btn:
			showPop();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

	/**
	 * 分享页面
	 */
	private void showPop() {
		if (share == null) {
			share = new SharePopupWindow(this);
			share.setPlatformActionListener(this);
		}
		ShareModel model = new ShareModel();
		model.setImageUrl(shared_imager_url);
		model.setText(content);
		model.setTitle(title);
		model.setUrl(shared_imager_url);
		share.initShareParams(model);
		if (!TextUtils.isEmpty(prizeid)) {
			share.setWebUrl(ConstantValue.SHARED_JIANGPINGPICTURE + prizeid);
		} else {
			share.setWebUrl(ConstantValue.SHARED_ZHONGPICTURE + id);
		}
		share.showShareWindow(false);
		// 显示窗口 (设置layout在PopupWindow中显示的位置)
		share.showAtLocation(this.findViewById(R.id.main_share), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);

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
		} else if (what == 2) {

		}

		if (share != null) {
			share.dismiss();
		}
		return false;
	}
}
