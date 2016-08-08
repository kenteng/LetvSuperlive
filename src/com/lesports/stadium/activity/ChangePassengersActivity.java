package com.lesports.stadium.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.PhoneInfo;
import com.lesports.stadium.fragment.ServiceFragment;
import com.lesports.stadium.utils.Utils;

/**
 * ***************************************************************
 * 
 * @ClassName: ChangePassengersActivity
 * 
 * @Desc : 换乘车人
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 盛之刚
 * 
 * @data:2016-2-15 下午12:00:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
public class ChangePassengersActivity extends BaseActivity implements
		OnClickListener {

	/**
	 * 返回按钮
	 */
	private ImageView ivBack;

	/**
	 * 标题
	 */
	private TextView tvTitle;

	/**
	 * 保存按钮
	 */
	private TextView tv_btn;

	/**
	 * 姓名
	 */
	private EditText tv_user_name;

	/**
	 * 手机号
	 */
	private EditText tv_phone;

	/**
	 * 通讯录
	 */
	private RelativeLayout rlyt_contacts;

	/**
	 * 更换乘车人
	 */
	public static final int CHANGE_PASSENGERS = 100;

	/**
	 * 乘车人信息
	 */
	public static PhoneInfo info;
	/**
	 * 是否发送信息
	 */
	private CheckBox sendSMS;
	/**
	 * 是否发送短信
	 */
	public static String sendSmsStr="1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_passengers);
		initBaseData();
		initView();
		initData();
		initListener();
	}

	private void initBaseData() {
		info = (PhoneInfo) getIntent().getSerializableExtra("phoneInfo");
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		ivBack = (ImageView) findViewById(R.id.title_left_iv);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		rlyt_contacts = (RelativeLayout) findViewById(R.id.rlyt_contacts);
		tv_btn = (TextView) findViewById(R.id.tv_btn);
		tv_user_name = (EditText) findViewById(R.id.tv_user_name);
		tv_phone = (EditText) findViewById(R.id.tv_phone);

		tvTitle.setText(getString(R.string.select_user));
		// 展示保存按钮
		tv_btn.setVisibility(View.VISIBLE);
		sendSMS=(CheckBox)findViewById(R.id.chk_sms);
		if(sendSmsStr.equals("1")){
			sendSMS.setChecked(true);
		}else{
			sendSMS.setChecked(false);
		}
	}

	private void initData() {
		if (info != null) {
			String phone = info.getPhone();
			if("未知".equals(phone)){
				tv_user_name.setHint("请输入乘车人姓名");
				tv_phone.setHint("请输入乘车人手机号");
			}else{
				tv_user_name.setText(info.getPhoneName());
				tv_phone.setText(info.getPhone());
			}
		}
	}

	/**
	 * 初始化监听事件
	 */
	private void initListener() {
		// 返回
		ivBack.setOnClickListener(this);
		// 跳转手机通讯录
		rlyt_contacts.setOnClickListener(this);
		// 保存
		tv_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_iv:
			// 关闭Activity
			if(ServiceFragment.instence!=null)
				ServiceFragment.instence.tag_views=3;
			finish();
			break;
		case R.id.rlyt_contacts:
			// 跳转到通讯录
			Intent intent = new Intent(ChangePassengersActivity.this,
					ContactsActivity.class);
			startActivityForResult(intent, CHANGE_PASSENGERS);
			break;
		case R.id.tv_btn:
			if(sendSMS.isChecked()){
				sendSmsStr="1";
			}else{
				sendSmsStr="0";
			}
			if(com.lesports.stadium.utils.CommonUtils.isRegexPhone(tv_phone.getText().toString())){
				info.setPhoneName(tv_user_name.getText().toString());
				info.setPhone(tv_phone.getText().toString());
				if(ServiceFragment.instence!=null)
					ServiceFragment.instence.tag_views=3;
				finish();
			}else{
				Toast.makeText(ChangePassengersActivity.this,"请输入正确的手机号",Toast.LENGTH_SHORT).show();
			}
			
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CHANGE_PASSENGERS) {
			if(!Utils.isNullOrEmpty(data)){
			info = (PhoneInfo) data.getSerializableExtra("phoneInfo");
			if (!Utils.isNullOrEmpty(info)) {
				tv_user_name.setText(info.getPhoneName());
				tv_phone.setText(info.getPhone()); 
			}
			}
		}
	}
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		ServiceFragment.instence.tag_views=3;
//	}

}
