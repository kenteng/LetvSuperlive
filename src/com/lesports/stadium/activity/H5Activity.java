package com.lesports.stadium.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lesports.stadium.R;

/**
 * 
 * @ClassName: H5Activity
 * 
 * @Description: H5 界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @author wangxinnian
 * 
 * @date 2016-7-18 下午6:55:24
 * 
 * 
 */
public class H5Activity extends Activity implements OnClickListener {

	/**
	 * 返回按钮
	 */
	private ImageView back;

	/**
	 * 标题
	 */
	private TextView tvTitle;

	/**
	 * 需要展示webWeil
	 */
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_h5);
		initView();
		initData();
		initListener();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.title_left_iv:
			finish();
			break;
		default:
			break;
		}
	}

	private void initListener() {
		back.setOnClickListener(this);

	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	public void initView() {
		back = (ImageView) findViewById(R.id.title_left_iv);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		webView = (WebView) findViewById(R.id.cuartcarouse_webview);
		webView.setVerticalScrollBarEnabled(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.getSettings().setJavaScriptEnabled(true); // 加上这句话才能使用javascript方法
		webView.getSettings().setDomStorageEnabled(true);
		webView.requestFocus();
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSaveFormData(false);
		webView.getSettings().setSupportZoom(false);

		WebChromeClient wvcc = new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);

				if (title != null && title.length() > 12) {
					tvTitle.setText(title.substring(0, 12) + "...");
				} else {
					tvTitle.setText(title);
				}
			}
		};
		// 设置setWebChromeClient对象
		webView.setWebChromeClient(wvcc);
		// webView.setWebViewClient(new WebViewClient() {
		//
		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// view.loadUrl(url);
		// return true;
		// }
		// });

	}

	public void initData() {
		// title.setText(R.string.app_name);
		Intent intent = getIntent();
		if (intent != null) {
			String url = intent.getStringExtra("url");
			if (!TextUtils.isEmpty(url)) {
				if (url.startsWith("http")) {
					webView.loadUrl(url);
				} else {
					webView.loadDataWithBaseURL(null,
							"<html><body background=\"#111a32\" link=\"white\">"
									+ url + "</body</html>", "text/html",
							"utf-8", null);
				}
			}
		}

	}

}
