/**
 * 
 */
package com.lesports.stadium.view;

import android.content.Context;
import android.widget.ListView;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.OrderCrowdFundingAdapter;

/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc :待支付订单页面
 * 
 * @Copr : 北京晶朝科技有限责任公司 版权所有 (c) 2016
 * 
 * @Author : liuwc
 * 
 * @data:2016-2-14 下午5:12:05
 * 
 * @Version : v1.0
 * 
 * 
 * @Modify : null
 * 
 *         ***************************************************************
 */
@ResID(id = R.layout.layout_order_wait_pay_view)
public class WaitPayCrowdView extends BaseView {
	/**
	 * 数据列表项
	 */
	private ListView mListview;
	/**
	 * 数据适配器
	 */
	private OrderCrowdFundingAdapter mAdapter;
	/**
	 * @param context
	 */
	public WaitPayCrowdView(final Context context) {
		super(context);
		//调用方法，初始化view数据源以及控件
	}


	@Override
	public void initBaseData() {
		
	}

	@Override
	public void initView() {
		
	}

	@Override
	public void initData() {
		
	}

	@Override
	public void initListener() {
		
	}

}
