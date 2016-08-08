/**
 * 
 */
package com.lesports.stadium.view;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lesports.stadium.R;
import com.lesports.stadium.activity.UseCounponActivity;
import com.lesports.stadium.adapter.CounponAdapter;
import com.lesports.stadium.bean.YouHuiBean;

/**
 * ***************************************************************
 * 
 * @ClassName: ServiceFoodView
 * 
 * @Desc : 可以使用优惠券
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
@ResID(id = R.layout.layout_keyishiyong)
public class SureUseCouponView extends BaseView {
	@ResID(id = R.id.keyishiyong_all_prize_lv)
	private ListView keyishiyong_lv;
	/**
	 * 底部确定按钮
	 */
	@ResID(id=R.id.coupon_sure)
	private TextView mSure;
	/**
	 * 使用youhuiquan不u
	 */
	@ResID(id=R.id.layout_shiyongyouhuiquan)
	private RelativeLayout mBottomlayout;
	/**
	 * 上下文
	 */
	private UseCounponActivity mContext;

	/**
	 * 用来标记界面回调code
	 */
	private final int RESULT_CODE=1;
	/**
	 * 界面回传时候需要带过去的实体对象
	 */
	private YouHuiBean mResultBean;
	/**
	 * 数据源集合
	 */
	private List<YouHuiBean> mList;
	private String goodsprices;
	/**
	 * 无数据布局
	 */
	@ResID(id=R.id.youhuiquanbukeyong_meishuju_y)
	private RelativeLayout mLayout_nodata;
	/**
	 * @param context
	 */
	public SureUseCouponView(final Context context) {
		super(context);
		mContext=(UseCounponActivity) context;
		mSure.setOnClickListener(this);
	}
	
	/**
	 * 从主界面中将优惠券数据传递过来
	 * @param list
	 * @param goodsprice 
	 */
	public void setList(List<YouHuiBean> list, String goodsprice){
		this.mList=list;
		this.goodsprices=goodsprice;
		//这里需要调用方法从服务器获取数据，目前先模拟假数据
		if(mList!=null&&mList.size()!=0){
			initListviewData(mList);
			mBottomlayout.setVisibility(View.VISIBLE);
		}else{
			keyishiyong_lv.setVisibility(View.GONE);
			mLayout_nodata.setVisibility(View.VISIBLE);
			mBottomlayout.setVisibility(View.GONE);
		}
	}

	/**
	 * 该方法用来初始化界面列表项的假数据
	 */
	private void initListviewData(final List<YouHuiBean> list) {
		// TODO Auto-generated method stub
		final CounponAdapter adapter=new CounponAdapter(list, mContext);
		keyishiyong_lv.setAdapter(adapter);
		keyishiyong_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mResultBean=list.get(arg2);
				for(int i=0;i<list.size();i++){
					if(arg2==i){
						list.get(i).setChoise(true);
					}else{
						list.get(i).setChoise(false);
					}
				}
				adapter.refreshUI(list);
			}
		});
		keyishiyong_lv.setEmptyView(findViewById(R.id.youhuiquanbukeyong_meishuju_y));
	}

	public SureUseCouponView(final Context context, Bundle bunlde) {
		super(context, bunlde);
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
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.coupon_sure:
			if(mResultBean==null){
				Toast.makeText(mContext, "请先选择优惠券",0).show();
			}else{
				if(!TextUtils.isEmpty(goodsprices)&&!TextUtils.isEmpty(mResultBean.getCouponCondition())){
					double goodpricesss=Double.parseDouble(goodsprices);
					double youhui=Double.parseDouble(mResultBean.getCouponCondition());
					if(goodpricesss>=youhui){
						mContext.setBean(mResultBean);
					}else{
						Toast.makeText(mContext, "订单金额不足优惠条件，请继续下单",0).show();
					}
				}
			}
			break;

		default:
			break;
		}
	}


}
