package com.lesports.stadium.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.lesports.stadium.R;
import com.lesports.stadium.base.BaseActivity;
/**
 * 
 * ***************************************************************
 * @ClassName:  SelectPriceActivity 
 * 
 * @Desc : 选择票价
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-1-29 上午11:38:42
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class SelectPriceActivity extends BaseActivity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.activity_selectprice, null);
		//添加布局
		addView(view);
		initView();
	}
	private void initView(){
		//添加标题
		hiddenTitleLayout(false);
		hiddenTittleContent(false, "选择票价");	
		getGoBack().setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_back_textview:
			finish();
			break;

		default:
			break;
		}
	}
}
