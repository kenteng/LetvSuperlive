/**
 * 
 */
package com.lesports.stadium.view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.MyCouponActivity;
import com.lesports.stadium.adapter.MyCounponAdapter;
import com.lesports.stadium.bean.YouHuiBean;

/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc : 所有优惠卷
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
@ResID(id = R.layout.layout_all_coupon)
public class AllCouponView extends BaseView {

	/**
	 * 列表项
	 */
	@ResID(id = R.id.all_keyishiyong_all_prize_lv)
	private ListView mListview;
	/**
	 * 上下文
	 */
	private MyCouponActivity mContext;
	/**
	 * 数据源集合
	 */
	private List<YouHuiBean> mList;

	/**
	 * @param context
	 */
	public AllCouponView(final Context context) {
		super(context);
		mContext = (MyCouponActivity) context;
	}

	/**
	 * 从主界面中将优惠券数据传递过来
	 * 
	 * @param list
	 */
	public void setList(List<YouHuiBean> list) {
		this.mList = list;
		// 这里需要调用方法从服务器获取数据，目前先模拟假数据
		if (mList != null && mList.size() != 0) {
			initListviewData(mList);
		} else {

		}

	}

	/**
	 * 无数据的时候显示
	 */
	public void setEmpty() {
		mListview.setVisibility(View.GONE);
		findViewById(R.id.all_youhuiquanbukeyong_meishuju_y).setVisibility(
				View.VISIBLE);
	}

	/**
	 * 该方法用来初始化界面列表项的假数据
	 */
	private void initListviewData(final List<YouHuiBean> list) {
		final MyCounponAdapter adapter = new MyCounponAdapter(list, mContext);
		mListview.setAdapter(adapter);
		mListview
				.setEmptyView(findViewById(R.id.all_youhuiquanbukeyong_meishuju_y));
//		mListview.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//					long arg3) {
//				//先判断状态
//				if(list.get(arg2).getStatus().equals("1")){
//					// 先判断类型
//					if (list.get(arg2).getCouponType().equals("0")) {
//						// 说是餐饮
//						// 需要跳转到兽界面，然后进行餐饮选择
//						MainActivity.instance.initIntentData("canyin");
//						mContext.finish();
//					} else if (list.get(arg2).getCouponType().equals("1")) {
//						//商品
//						MainActivity.instance.initIntentData("shangpin");
//						mContext.finish();
//					}else if(list.get(arg2).getCouponType().equals("2")){
//						//用车
//						MainActivity.instance.initIntentData("yongche");
//						mContext.finish();
//					}else if(list.get(arg2).getCouponType().equals("3")){
//						//购票
//						finish();
//					}
//				}else{
//					Toast.makeText(mContext, "对不起，该优惠券已经失效",0).show();
//				}
//			
//			}
//		});

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
