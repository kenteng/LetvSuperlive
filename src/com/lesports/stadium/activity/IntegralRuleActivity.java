package com.lesports.stadium.activity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.bean.IntegralRuleBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.JsonUtil;

/**
 * 积分规则
 * 
 * @author Administrator
 * 
 */
public class IntegralRuleActivity extends Activity implements OnClickListener {
	IntegralRuleBean integralRuleBean=new IntegralRuleBean();
	/**
	 * webview
	 */
	private WebView dy_webview;
	/**
	 * 标题
	 */
	private TextView title;
	
	private Handler integralHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 100:
//			Log.e("html", shakeRuleBean.getLotteryRule());
				changeWebBg();
				break;

			default:				
				break;
			}
		}

		private void changeWebBg() {
			//更改标题名称
			title.setText(integralRuleBean.getIntegralRuleName());
			dy_webview.loadDataWithBaseURL(null,"<html><body background=\"#00000000\" link=\"white\">" +integralRuleBean.getIntegralRule() +"</body</html>", "text/html", "utf-8", null);
			dy_webview.setBackgroundColor(0x00000000);
			if (Build.VERSION.SDK_INT >= 11) // Android v3.0+
			 try {
			  Method method = View.class.getMethod("setLayerType", int.class, Paint.class);
			  method.invoke(dy_webview, 1, new Paint()); // 1 = LAYER_TYPE_SOFTWARE (API11)
			 } catch (Exception e) {
			}
		};
	};


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integralrule);
		initview();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
	requestRule();
	}

	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		dy_webview = (WebView) findViewById(R.id.dy_webview);
		dy_webview.getSettings().setJavaScriptEnabled(true);
		dy_webview.getSettings().setDefaultTextEncodingName("utf-8") ;
		dy_webview.setBackgroundColor(0x00000000); // 设置背景色
//		dy_webview.getBackground().setAlpha(2); // 设置填充透明度 范围：0-255
//		dy_webview.loadDataWithBaseURL(null, "加载中。。", "text/html", "utf-8",null);
//		dy_webview.loadDataWithBaseURL(mGetDetail.data.get("hostsUrl"), mGetDetail.data.get("description"), "text/html", "utf-8",null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		default:
			break;
		}

	}
	
	
	
	
	/**
	 * 获取积分规则
	 */
	private void requestRule() {
		Map<String, String> params = new HashMap<String, String>();
		GetDataFromInternet.getInStance().interViewNet(
				ConstantValue.INTEGRALRULE, params, new GetDatas() {

					@Override
					public void getServerData(BackData data) {
						// TODO Auto-generated method stub
						if (data == null) {
							// 说明网络获取无数据
						} else {
							Object obj = data.getObject();
							if (obj == null) {
							} else {
								String backdata = obj.toString();
								if (backdata == null || backdata.equals("")) {
								} else {
//									Log.e("rulerulerule", backdata);
									if(!"null".equals(backdata)){
										integralRuleBean = JsonUtil.parseJsonToBean(backdata,
												IntegralRuleBean.class);
										Message msg = new Message();
										msg.what = 100;
										integralHandler.sendMessage(msg);
									}
								}
							}
						}
					}
				}, false,true);

	}
	
}
