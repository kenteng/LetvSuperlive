package com.lesports.stadium.activity;

import java.lang.reflect.Method;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.lesports.stadium.R;
import com.lesports.stadium.base.LetvBaseActivity;
import com.lesports.stadium.bean.LoginResultData;
import com.lesports.stadium.bean.LoginUserInfo;
import com.lesports.stadium.http.paser.LetvLoginResultParser;
import com.lesports.stadium.utils.LoginUtil;
import com.letv.component.utils.DebugLog;

/**
 * ***************************************************************
 * 
 * @ClassName: LetvQQH5LoginActivity
 * 
 * @Desc : 登录
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 盛之刚
 * 
 * @data:2016-2-22 下午12:00:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : 乐视提供SDK
 * 
 *  ***************************************************************
 */
public class LetvQQH5LoginActivity extends LetvBaseActivity implements View.OnClickListener {
    
	private WebView mWebView;

	private String baseUrl;

	private String title;
	
	private static String Loginsrc;
	
	private ImageView back;

	private ImageView forward;

	private ImageView refresh;
	private Context mContext;
	private TextView address;
	private ProgressBar progressBar;
	public static String TAG = "LetvOpenIDOAuthLoginActivity";
	
	public static void launch(Activity context, String url, String title, String src) {
		Loginsrc=src;
		Intent intent = new Intent(context, LetvQQH5LoginActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", title);
		intent.putExtra("src", src);
		context.startActivity(intent);
	}
	
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.login_webview);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		baseUrl = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		findView();
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	private void findView() {
		back = (ImageView) findViewById(R.id.back);
		forward = (ImageView) findViewById(R.id.forward);
		refresh = (ImageView) findViewById(R.id.refresh);
		address = (TextView) findViewById(R.id.letv_webview_title);
		progressBar = (ProgressBar) findViewById(R.id.loading_progress);
		findViewById(R.id.back_iv).setOnClickListener(this);
		address.setText(title);
		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.addJavascriptInterface(new Handler(), "handler");
		mWebView.setVerticalScrollBarEnabled(true);
		mWebView.setHorizontalScrollBarEnabled(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new LetvWebViewClient());
		mWebView.setWebChromeClient(new LetvWebViewChromeClient());
		mWebView.loadUrl(baseUrl);
		DebugLog.log(TAG, "onCreate*****baseUrl=" + baseUrl);
		this.back.setOnClickListener(this);
		forward.setOnClickListener(this);
		refresh.setOnClickListener(this);
	}
	
	private class LetvWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			//view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			DebugLog.log(TAG, "LetvWebViewClient*****onPageFinished="+url);
			 try {
	                URL mUrl = new URL(url);
	                String path = mUrl.getPath();
	                if (path.contains("callbackdata")) {
	                    view.setVisibility(View.INVISIBLE);
	                    view.loadUrl("javascript:window.handler.show(document.body.innerText);");
	                    view.stopLoading();
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
			super.onTooManyRedirects(view, cancelMsg, continueMsg);
		}
	}
	class Handler {
        public void show(String data) {
            try {
            	LoginResultData datas=LetvLoginResultParser.parseReponseToLoginUserInfo(data);
            	if (datas == null) {
					Toast.makeText(mContext, "授权失败",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if ("1001".equals(datas.getErrorCode())) {
					Toast.makeText(mContext, "登录失败",
							Toast.LENGTH_SHORT).show();
				}
				Log.i("ynn","QQH5:"+datas.getErrorCode()+"..."+datas.getBean());
				if ("0".equals(datas.getErrorCode())) {
					LoginUserInfo userInfo = datas
							.getBean();
					if (userInfo != null) {
						userInfo.sso_tk=datas.getSso_tk();
						//===========================ToDO=================
						LoginUtil.getInstance().operLoginResponse(
								mContext, userInfo,
								Loginsrc);
					}
					finish();
				}
                
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

	private class LetvWebViewChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			if (progressBar.getVisibility() != View.VISIBLE) {
				progressBar.setVisibility(View.VISIBLE);
			}
			progressBar.setProgress(newProgress);
			if (newProgress == 100) {
				progressBar.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.back) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			}
		} else if(id == R.id.forward) {
			if (mWebView.canGoForward()) {
				mWebView.goForward();
			}
		} else if(id == R.id.refresh) {
			mWebView.reload();
		} else if(id == R.id.back_iv) {
			finish();
		}
	}

	@SuppressLint("InlinedApi")
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			if (mWebView != null) {
				mWebView.stopLoading();
				/**
				 * 3.0以上系统编译，如果不隐藏webview会出现崩溃
				 */
				mWebView.setVisibility(View.GONE);
				mWebView.destroy();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		callHiddenWebViewMethod("onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		callHiddenWebViewMethod("onResume");
	}

	private void callHiddenWebViewMethod(String name) {
		if (mWebView != null) {
			try {
				Method method = WebView.class.getMethod(name);
				method.invoke(mWebView);
			} catch (Exception e) {
			}
		}
	}
	
}
