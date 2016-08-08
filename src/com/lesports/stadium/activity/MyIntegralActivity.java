package com.lesports.stadium.activity;

import com.lesports.stadium.R;
import com.lesports.stadium.utils.GlobalParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @ClassName: MyIntegralActivity
 * 
 * @Description: 我的乐豆界面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @author wangxinnian
 * 
 * @date 2016-7-10 下午6:47:35
 * 
 * 
 */
public class MyIntegralActivity extends Activity implements OnClickListener {
	// 明细
	private TextView tv_detail;

	// 充值
	private TextView tv_chongzhi;
	// 规则
	private TextView tv_rule;
	// 返回键
	private ImageView ib_back;
	// 当前积分
	private TextView myldjf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myintegral);
		InitView();
		InitListener();
		InitData();
	}

	private void InitData() {
		// TODO Auto-generated method stub

	}

	private void InitListener() {
		tv_detail.setOnClickListener(this);
		tv_chongzhi.setOnClickListener(this);
		tv_rule.setOnClickListener(this);
		ib_back.setOnClickListener(this);
	}

	private void InitView() {
		tv_detail = (TextView) findViewById(R.id.tv_detail);
		tv_chongzhi = (TextView) findViewById(R.id.tv_chongzhi);
		tv_rule = (TextView) findViewById(R.id.tv_rule);
		myldjf = (TextView) findViewById(R.id.tv_myldjf);
		ib_back = (ImageView) findViewById(R.id.ib_back);
		myldjf.setText(GlobalParams.USER_INTEGRAL + "");

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		switch (v.getId()) {

		case R.id.tv_detail:
			intent = new Intent(MyIntegralActivity.this,
					IntegralDetailActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_chongzhi:
			intent = new Intent(MyIntegralActivity.this,
					RechargeintegralActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_rule:
			intent = new Intent(MyIntegralActivity.this,
					IntegralRuleActivity.class);
			startActivity(intent);
			break;
		case R.id.ib_back:
			finish();
			break;

		default:
			break;
		}
	}

}
