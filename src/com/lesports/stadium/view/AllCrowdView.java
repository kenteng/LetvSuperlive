/**
 * 
 */
package com.lesports.stadium.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.CrowdOrderDetailActivity;
import com.lesports.stadium.adapter.OrderCrowdFundingAdapter;
import com.lesports.stadium.bean.OrderCrowdfundingBean;

/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc : 所有订单页面
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
@ResID(id = R.layout.layout_allorderview)
public class AllCrowdView extends BaseView {
	/**
	 * 数据列表项
	 */
//	@ResID(id=R.id.order_crowdfunding_all_lv)
	private ListView mListview;
	/**
	 * 数据适配器
	 */
	private OrderCrowdFundingAdapter mAdapter;
	/**
	 * @param context
	 */
	public AllCrowdView(final Context context) {
		super(context);
		//调用方法，初始化view数据源以及控件
//		initdatas(context);
	}

	/**
	 * 用来初始化listview当中需要展示的数据源以及列表项
	 * @param context
	 */
	@SuppressWarnings("unused")
	private void initdatas(final Context context) {
		int[] status={0,1,2,0,1,2,0,1,2,1};
		List<OrderCrowdfundingBean> list=new ArrayList<OrderCrowdfundingBean>();
		for(int i=0;i<10;i++){
			OrderCrowdfundingBean bean=new OrderCrowdfundingBean();
			bean.setmName("测试数据"+i);
			bean.setStatus(status[i]);
			list.add(bean);
		}
		mAdapter=new OrderCrowdFundingAdapter(list, context);
		mListview.setAdapter(mAdapter);
		mListview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent(context, CrowdOrderDetailActivity.class);
				context.startActivity(intent);
			}
		});
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
