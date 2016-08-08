package com.lesports.stadium.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;
import android.text.TextUtils;
import android.util.Log;
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
import com.lesports.stadium.bean.GuangGaoBean;
import com.lesports.stadium.bean.ShareModel;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.view.SharePopupWindow;
/**
 * ***************************************************************
 * 
 * @Desc : 广告详情界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class GoodsGuanggaoActivity extends BaseActivity implements OnClickListener
, PlatformActionListener, Callback {
	/**
	 * 返回按钮
	 */
	private ImageView mBack;
	/**
	 * 顶部标题
	 */
	private TextView mTitle;
	/**
	 * 右侧分享按钮
	 */
	private ImageView mShare;
	/**
	 * 加载webview
	 */
	private WebView mWebview;
	/**
	 * 分享的PopupWindow
	 */
	private SharePopupWindow share;
	/**
	 * 分享图片路径
	 */
	private String imageurl= "http://h.hiphotos.baidu.com/image/pic/item/ac4bd11373f082028dc9b2a749fbfbedaa641bca.jpg";
	/**
	 * 分享链接
	 */
	private String url;
	/**
	 * 分享标题
	 */
	private String title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goodsguanggao);
		ShareSDK.initSDK(this);
		initview();
		initdata();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	/**
	 * 初始化加载数据
	 */
	private void initdata() {
		// TODO Auto-generated method stub
		Intent intent=getIntent();
		GuangGaoBean bean=(GuangGaoBean) intent.getSerializableExtra("bean");
		if(null==bean)
			return;
		//要分享的httpurl
		url = bean.getHttpUrl();
		imageurl = ConstantValue.BASE_IMAGE_URL+bean.getUrl();
		title = bean.getTitle();
		if(!TextUtils.isEmpty(bean.getHttpUrl())){
			mWebview.loadUrl(bean.getHttpUrl());
		}
		mTitle.setText(bean.getTitle());
		
	}

	private void initview() {
		// TODO Auto-generated method stub
		mBack=(ImageView) findViewById(R.id.goods_zhongchou_title_left_iv);
		mBack.setOnClickListener(this);
		mTitle=(TextView) findViewById(R.id.goods_zhongchou_tv_title);
		mShare=(ImageView) findViewById(R.id.goods_zhongchou_tv_btn);
		mShare.setOnClickListener(this);
		mWebview=(WebView) findViewById(R.id.goods_dy_webview);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.goods_zhongchou_title_left_iv:
			finish();
			break;
		case R.id.goods_zhongchou_tv_btn:
			//这里进行分享
			showPop();
			break;

		default:
			break;
		}
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
	
	private void showPop() {
		share = new SharePopupWindow(this);
		share.setPlatformActionListener(this);
		ShareModel model = new ShareModel();
		model.setImageUrl(imageurl);
		 model.setText(title);
		 model.setTitle(title);
		model.setUrl(imageurl);
		share.initShareParams(model);
		share.setWebUrl(url);
		share.showShareWindow(false);
		share.setTopShow("分享", false);
		// 显示窗口 (设置layout在PopupWindow中显示的位置)
		share.showAtLocation(this.findViewById(R.id.goods_main_share), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);

	}

}
