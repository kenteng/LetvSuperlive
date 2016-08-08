package com.lesports.stadium.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesports.stadium.R;
import com.lesports.stadium.base.BaseActivity;
import com.lesports.stadium.bean.PhoneInfo;
/**
 * ***************************************************************
 * 
 * @ClassName: ConfirmOrderActivity
 * 
 * @Desc : 用车界面
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
public class ConfirmOrderActivity extends BaseActivity implements OnClickListener{

	/**
	 * 返回按钮
	 */
	private ImageView ivBack;
	
	/**
	 * 标题
	 */
	private TextView tvTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_order);
    	initView();
    	initData();
		initListener();
	}
	
	    
    /**
     * 初始化View
     */
	private void initView(){
		ivBack=(ImageView)findViewById(R.id.title_left_iv);
		tvTitle=(TextView)findViewById(R.id.tv_title);
	}
	
	private void  initData(){
		tvTitle.setText(this.getString(R.string.confirm_order));
	}
	
	/**
	 * 初始化监听事件
	 */
	private void initListener(){
		//返回
		ivBack.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_left_iv:
			//关闭Activity
			finish();
		break;
		default:
			break;
		}
	}

}
