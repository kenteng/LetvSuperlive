/**
 * 
 */
package com.lesports.stadium.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.lesports.stadium.R;
import com.lesports.stadium.adapter.OrderCrowdFundingAdapter;
import com.lesports.stadium.bean.OrderCrowdfundingBean;

/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc :待发货订单页面
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
@ResID(id = R.layout.layout_order_wait_send_view)
public class WaitSendCrowdView extends BaseView {
	/**
	 * 数据列表项
	 */
	/**
	 * 数据适配器
	 */
	private OrderCrowdFundingAdapter mAdapter;
	/**
	 * @param context
	 */
	public WaitSendCrowdView(final Context context) {
		super(context);
		//调用方法，初始化view数据源以及控件
		initdatas(context);
	}
	/**
	 * 用来初始化listview当中需要展示的数据源以及列表项
	 * @param context
	 */
	private void initdatas(Context context) {
		// TODO Auto-generated method stub
		int[] status={0,1,2,0,1,2,0,1,2,1};
		List<OrderCrowdfundingBean> list=new ArrayList<OrderCrowdfundingBean>();
		for(int i=0;i<10;i++){
			OrderCrowdfundingBean bean=new OrderCrowdfundingBean();
			bean.setmName("测试数据"+i);
			bean.setStatus(status[i]);
			list.add(bean);
		}
		List<OrderCrowdfundingBean> list_waitpay=new ArrayList<OrderCrowdfundingBean>();
		for(int i=0;i<list.size();i++){
			if(list.get(i).getStatus()==1){
				list_waitpay.add(list.get(i));
			}
		}
		mAdapter=new OrderCrowdFundingAdapter(list_waitpay, context);
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
