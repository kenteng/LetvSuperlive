package com.lesports.stadium.activity;

import com.lesports.stadium.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * ***************************************************************
 * @ClassName:  RideTimeActivity 
 * 
 * @Desc : 时间轮
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : 邓聪聪
 * 
 * @data:2016-3-22 下午1:53:20
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 * ***************************************************************
 */
public class RideTimeActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.populwindow_datepicker);
	}
}
