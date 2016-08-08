package com.lesports.stadium.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

import com.lesports.stadium.R;

/**
 * ***************************************************************
 * 
 * @ClassName: AgreementActivity
 * 
 * @Desc : 乐视服务使用协议
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 司明
 * 
 * @data:2016-3-23 下午3:11:43
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class AgreementActivity extends Activity implements OnClickListener {
	/**
	 * WebView 对象
	 */
	private WebView webview_agreement;
	/**
	 * 返回按钮
	 */
	private ImageView agreement_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);
		initView();
		initListener();
		initData();

	}

	/**
	 * 加载数据
	 */
	private void initData() {
		localHtml();
	}

	/**
	 * 设置监听
	 */
	private void initListener() {
		agreement_back.setOnClickListener(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		webview_agreement = (WebView) findViewById(R.id.webview_agreement);
		agreement_back = (ImageView) findViewById(R.id.agreement_back);
	}

	/**
	 * 显示本地网页文件
	 */
	private void localHtml() {
		try {
			// 本地文件处理(如果文件名中有空格需要用+来替代)
			webview_agreement
					.loadUrl("file:///android_asset/UserProtocol.html");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.agreement_back:
			finish();
			break;

		default:
			break;
		}
	}
}
