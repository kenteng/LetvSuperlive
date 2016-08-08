package com.lesports.stadium.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;
import com.lesports.stadium.R;
import com.lesports.stadium.bean.IntegralBean;
import com.lesports.stadium.bean.IntegralTimeBean;
import com.lesports.stadium.engine.GetDatas;
import com.lesports.stadium.engine.impl.GetDataFromInternet;
import com.lesports.stadium.net.BackData;
import com.lesports.stadium.utils.ConstantValue;
import com.lesports.stadium.utils.JsonUtil;
import com.lesports.stadium.view.CustomDialog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * ***************************************************************
 * 
 * @Desc : 意见反馈 界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 王新年
 * 
 * @data:
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class SuggestionActivity extends Activity implements OnClickListener {
	/**
	 * 输入意见文本框
	 */
	private EditText input_et;
	/**
	 * 显示当前还能输入多少字
	 */
	private TextView text_count;
	/**
	 * 提交按钮
	 */
	private View tijiao;
	/**
	 * 信息提示
	 */
	private CustomDialog msgDialog;

	/**
	 * handle标记
	 */
	private final int HANDLE_TAG_100=100;
	private final int HANDLE_TAG_50=50;
	private Handler feedbackHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLE_TAG_100:
				input_et.setText("");
				text_count.setText("0/120");
				showDiaLog("评论成功", 1);
				break;
			case HANDLE_TAG_50:
				showDiaLog("服务器异常，稍后重试", 0);
			default:

				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestion);
		initview();
	}

	/**
	 * 初始化View
	 */
	private void initview() {
		input_et = (EditText) findViewById(R.id.input_et);
		text_count = (TextView) findViewById(R.id.text_count);
		findViewById(R.id.back).setOnClickListener(this);
		tijiao = findViewById(R.id.tijiao);
		tijiao.setOnClickListener(this);
		input_et.addTextChangedListener(new TextWatcher() {
			private int editStart;
			private int editEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				String content = input_et.getText().toString();
				if (!TextUtils.isEmpty(content)) {
					if (content.length() > 120) {
						// editStart = input_et.getSelectionStart();
						// editEnd = input_et.getSelectionEnd();
						Toast.makeText(getApplicationContext(), "你输入的文字过多", 0)
								.show();
						// s.delete(editStart - 1, editEnd);
						// int tempSelection = editStart;
						// input_et.setText(s);
						// input_et.setSelection(tempSelection);
						input_et.setText(s.subSequence(0, 120));
						input_et.setSelection(120);
						return;
					}
					text_count.setText(content.length() + "/120");
				}

			}
		});
	}

	/**
	 * 提交意见反馈内容
	 */
	private void requestFeedback() {
		if(!TextUtils.isEmpty(input_et.getText().toString().trim())){
			Map<String, String> params = new HashMap<String, String>();
			params.put("FeedDetail", input_et.getText().toString().trim()); // 反馈的内容
			params.put("mid", "11"); //
			params.put("deviceId", "android"); // 设备
			params.put("feedbacktype", "1"); // 反馈的类型
			GetDataFromInternet.getInStance().interViewNet(
					ConstantValue.SUGGESTION_ADD, params, new GetDatas() {

						@Override
						public void getServerData(BackData data) {
							if (data != null && data.getNetResultCode() == 0)
								feedbackHandler.sendEmptyMessage(100);
							else
								feedbackHandler.sendEmptyMessage(50);
						}
					}, false, false);
		}else{
			showDiaLog("输入内容不合法，请重新输入", 0);
		}
		

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.tijiao:
			String content = input_et.getText().toString();
			if (!TextUtils.isEmpty(content)) {
				if (content.length() > 120) {
					Toast.makeText(getApplicationContext(), "你输入的文字过多", 0)
							.show();
					return;
				} else {
					// tijiao.setBackgroundResource(R.drawable.green_tx_background);
					requestFeedback();
				}
			} else {
				showDiaLog("意见不能为空", 0);
			}
		default:
			break;
		}

	}

	/**
	 * 提示意见反馈信息成功
	 * 
	 * @param msg
	 */
	private void showDiaLog(String msg, final int type) {
		msgDialog = null;
		msgDialog = new CustomDialog(new OnClickListener() {

			@Override
			public void onClick(View v) {
				msgDialog.dismiss();
				if (type == 1)
					finish();
			}
		});
		msgDialog.setRemindMessage(msg);
		msgDialog.setRemindTitle("温馨提示");
		msgDialog.setConfirmTxt("确认");
		msgDialog.show();
	}
	
	@Override
	protected void onDestroy() {
		feedbackHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}
}
