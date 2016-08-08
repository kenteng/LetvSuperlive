package com.lesports.stadium.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.net.URL;
import com.lesports.stadium.R;
import com.lesports.stadium.bean.LoginResultData;
import com.lesports.stadium.bean.LoginUserInfo;
import com.lesports.stadium.http.paser.LetvLoginResultParser;
import com.lesports.stadium.utils.LoginUtil;

public class LetvThirdLoginActivity extends Activity
  implements View.OnClickListener
{
  private WebView mWebView;
  private String baseUrl;
  private String title;
  private static String Loginsrc;
  private ImageView back;
  private ImageView forward;
  private ImageView refresh;
  private Context mContext;
  private String path;
  private TextView address;
  private ProgressBar progressBar;
  public static String TAG = "LetvOpenIDOAuthLoginActivity";

  public static void launch(Activity context, String url, String title, String src)
  {
    Loginsrc = src;
    Intent intent = new Intent(context, LetvThirdLoginActivity.class);
    intent.putExtra("url", url);
    intent.putExtra("title", title);
    intent.putExtra("src", src);
    context.startActivityForResult(intent, 0);
  }

  @SuppressLint({"InlinedApi"})
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.mContext = this;
    setContentView(R.layout.activity_letv_third_login);
    getWindow().addFlags(
      16777216);
    this.baseUrl = getIntent().getStringExtra("url");
    this.title = getIntent().getStringExtra("title");
    findView();
  }
  @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
  private void findView() {
    this.back = ((ImageView)findViewById(R.id.back));
    this.forward = ((ImageView)findViewById(R.id.forward));
    this.refresh = ((ImageView)findViewById(R.id.refresh));
    this.address = ((TextView)findViewById(R.id.letv_webview_title));
    this.progressBar = ((ProgressBar)findViewById(R.id.loading_progress));
    findViewById(R.id.back_iv).setOnClickListener(this);
    this.address.setText(this.title);
    this.mWebView = ((WebView)findViewById(R.id.webView));
    this.mWebView.getSettings().setUseWideViewPort(true);
    this.mWebView.getSettings().setSupportZoom(true);
    this.mWebView.getSettings().setBuiltInZoomControls(true);
    //this.mWebView.getSettings().setUserAgentString(
      //LetvUtils.createUA(this.mWebView.getSettings().getUserAgentString(), 
     // this));
    this.mWebView.addJavascriptInterface(new Handler(), "handler");
    this.mWebView.setVerticalScrollBarEnabled(true);
    this.mWebView.setHorizontalScrollBarEnabled(true);
    this.mWebView.getSettings().setJavaScriptEnabled(true);
    this.mWebView.setWebViewClient(new LetvWebViewClient());
    this.mWebView.setWebChromeClient(new LetvWebViewChromeClient());
    this.mWebView.loadUrl(this.baseUrl);
    this.back.setOnClickListener(this);
    this.forward.setOnClickListener(this);
    this.refresh.setOnClickListener(this);
  }

  public void onClick(View v)
  {
    int id = v.getId();
    if (id == R.id.back) {
      if (this.mWebView.canGoBack())
        this.mWebView.goBack();
    }
    else if (id == R.id.forward) {
      if (this.mWebView.canGoForward())
        this.mWebView.goForward();
    }
    else if (id == R.id.refresh)
      this.mWebView.reload();
    else if (id == R.id.back_iv)
      if (this.mWebView.canGoBack())
        this.mWebView.goBack();
      else
        finish();
  }

  @SuppressLint({"InlinedApi"})
  protected void onDestroy()
  {
    super.onDestroy();
    try {
      getWindow().clearFlags(
        16777216);
      if (this.mWebView != null) {
        this.mWebView.stopLoading();

        this.mWebView.setVisibility(8);
        this.mWebView.destroy();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void onPause()
  {
    super.onPause();
    callHiddenWebViewMethod("onPause");
  }

  protected void onResume()
  {
    super.onResume();
    callHiddenWebViewMethod("onResume");
  }

  private void callHiddenWebViewMethod(String name) {
    if (this.mWebView != null)
      try {
        Method method = WebView.class.getMethod(name, new Class[0]);
        method.invoke(this.mWebView, new Object[0]);
      }
      catch (Exception localException)
      {
      }
  }

  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == 4) {
      if (this.mWebView.canGoBack())
        this.mWebView.goBack();
      else {
        finish();
      }
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  class Handler
  {
    Handler()
    {
    }

    @JavascriptInterface
    public void show(String data)
    {   	
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
            e.printStackTrace();
        }
    }
  }

  private class LetvWebViewChromeClient extends WebChromeClient
  {
    private LetvWebViewChromeClient()
    {
    }

    public void onProgressChanged(WebView view, int newProgress)
    {
      super.onProgressChanged(view, newProgress);
      if (LetvThirdLoginActivity.this.progressBar.getVisibility() != 0) {
        LetvThirdLoginActivity.this.progressBar.setVisibility(0);
      }
      LetvThirdLoginActivity.this.progressBar.setProgress(newProgress);
      if (newProgress == 100)
        LetvThirdLoginActivity.this.progressBar.setVisibility(8);
    }
  }

  private class LetvWebViewClient extends WebViewClient
  {
    private LetvWebViewClient()
    {
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
      return super.shouldOverrideUrlLoading(view, url);
    }

    public void onPageStarted(WebView view, String url, Bitmap favicon)
    {
      LetvThirdLoginActivity.this.progressBar.setVisibility(0);
      LetvThirdLoginActivity.this.progressBar.setProgress(0);
    }

    public void onPageFinished(WebView view, String url)
    {
      try
      {
        URL mUrl = new URL(url);
        String path = mUrl.getPath();
        if (path.contains("callbackdata")) {
          view.setVisibility(4);
          view.loadUrl("javascript:window.handler.show(document.body.innerText);");
          view.stopLoading();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
    {
      super.onReceivedError(view, errorCode, description, failingUrl);
    }

    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
    {
      handler.proceed();
    }
  }
}
